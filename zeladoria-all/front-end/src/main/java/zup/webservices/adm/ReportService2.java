package zup.webservices.adm;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.variable;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.hibernate.HibernateException;
import org.xmlpull.v1.XmlPullParserException;

import filters.ReportFilterBean;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.PieChartBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ValueColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.Evaluation;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import zup.bean.Configuration;
import zup.bean.EntityCategory;
import zup.bean.Report3;
import zup.business.CallClassificationBusiness;
import zup.business.EntityBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.ReportBusiness2;
import zup.enums.CallClassificationName;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.utils.MinioServer;
import zup.utils.Templates;

@Path("/report2")
@Consumes(MediaType.APPLICATION_JSON)
public class ReportService2 {

	@GET
	@Path("/ec/{entityId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEC(@PathParam("entityId") Integer entityId)
			throws HibernateException, SQLException, ValidationException, ModelException {
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		try {
			List<EntityCategory> list = ecb.findByEntity(entityId);
			return Response.ok(list).build();
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		}
	}

	@GET
	@Path("/combos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCombos() throws HibernateException, SQLException, ValidationException, ModelException {
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		EntityBusiness eb = new EntityBusiness();
		try {
			Map<String, List> map = new HashMap<String, List>();
			map.put("cc", ccb.findAll());
			map.put("en", eb.findActives());
			map.put("cp", Arrays.asList(CallProgress.values()));
			return Response.ok(map).build();
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		}
	}

	// Método para criação do Gráfico de Barras.
	public BarChartBuilder createBarChart(String title, ValueColumnBuilder<?, String> column) {
		return DynamicReports.cht.barChart().setTitle(title).setCategory(column)
				.setCategoryAxisFormat(cht.axisFormat().setLabel("Total"))
				.addSerie(DynamicReports.cht.serie(exp.number(1)).setLabel("")).setUseSeriesAsCategory(true)
				.setShowValues(true);
	}

	// Método para criação do Gráfico de Pizza.
	public PieChartBuilder createPieChart(String titleChart, ValueColumnBuilder<?, String> columnCategory) {
		return DynamicReports.cht.pieChart().setTitle(titleChart).setKey(columnCategory)
				.series(cht.serie(exp.number(1))).setShowValues(true).setDimension(500, 500);
	}

	public void reportC(ReportFilterBean filter, ByteArrayOutputStream buffer) {
		MinioServer minioServer = new MinioServer();
		ReportBusiness2 rb = new ReportBusiness2();
		EntityBusiness eb = new EntityBusiness();
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		List<Report3> list = new ArrayList<Report3>();
		Configuration cityConfig = getCityConfig();
		
		TextColumnBuilder<Date> dateColumn;
		TextColumnBuilder<String> callClassificationColumn = null;
		TextColumnBuilder<String> callSourceColumn = null;
		TextColumnBuilder<String> callProgressColumn = null;
		TextColumnBuilder<String> entityColumn = null;
		TextColumnBuilder<String> categoryColumn = null;
		TextColumnBuilder<String> priorityColumn = null;
		TextColumnBuilder<String> neighborhoodColumn = null;
		try {
			if (filter.getCallProgress().equals("3")){
				list = rb.searchSolved(filter.getInitDate(), filter.getEndDate(), filter.getCallClassification(),
						filter.getCallSource(), filter.getEntity(), filter.getCategory(), filter.getPriority(),
						filter.getFields());
			for(Report3 rp:list){
				rp.setEntity(eb.getById(rp.getEntityId()).getName());
				rp.setEntityCategory(ecb.getById(rp.getEntityCategoryId()).getName());
			}
			}
			if (filter.getCallProgress().isEmpty()){
				list = rb.searchAll(filter.getInitDate(), filter.getEndDate(), filter.getCallClassification(),
						filter.getCallSource(), filter.getCallProgress(), filter.getEntity(), filter.getCategory(),
						filter.getPriority(), filter.getFields());
				for(Report3 rp:list){
					rp.setEntity(eb.getById(rp.getEntityId()).getName());
					rp.setEntityCategory(ecb.getById(rp.getEntityCategoryId()).getName());
				}
			}
			if (!filter.getCallProgress().equals("3") && !filter.getCallProgress().isEmpty()){
				list = rb.searchUnsolved(filter.getInitDate(), filter.getEndDate(), filter.getCallClassification(),
						filter.getCallSource(), filter.getCallProgress(), filter.getEntity(), filter.getCategory(),
						filter.getPriority(), filter.getFields());
				for(Report3 rp:list){
					rp.setEntity(eb.getById(rp.getEntityId()).getName());
					rp.setEntityCategory(ecb.getById(rp.getEntityCategoryId()).getName());
				}
			}
			if(list.isEmpty())
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JasperReportBuilder report = DynamicReports.report();
		if (filter.getFormat().equals("pdf")) {
			StyleBuilder boldStyle = stl.style().bold();
			StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
			String titulo = "";
			if (filter.getType().equals("relacionado"))
				titulo = "RELATÓRIO RELACIONADO";
			else
				titulo = "RELATÓRIO CONSOLIDADO";
			// Titúlo
			InputStream zem = null;
			InputStream cityImage = null;
			try {
				zem = minioServer.getMinioClient().getObject("zem", "logoZem.png");
				cityImage = minioServer.getMinioClient().getObject("zem", cityConfig.getLogo());
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidBucketNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InsufficientDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InternalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidEndpointException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageBuilder img = cmp.image(Templates.class.getResourceAsStream("images/logoZem.png"));
			StyleBuilder titleStyle = stl.style(boldCenteredStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
					.setFontSize(15);
			report.addTitle(cmp.horizontalList()
					.add(cmp.image(cityImage).setHorizontalAlignment(HorizontalAlignment.RIGHT).setFixedWidth(150))
					.add(cmp.text(titulo).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.CENTER))
					.add(cmp.image(zem).setHorizontalAlignment(HorizontalAlignment.LEFT).setFixedWidth(90))
					.add(cmp.text("Zeladoria Municipal").setStyle(titleStyle)
							.setHorizontalAlignment(HorizontalAlignment.LEFT).setFixedWidth(90))
					.newRow().add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)));
			try {
			//	EntityBusiness eb = new EntityBusiness();
			//	EntityCategoryBusiness ecb = new EntityCategoryBusiness();
				Color greenZem = new Color(104, 195, 159);
				String callClassification = filter.getCallClassification().isEmpty() ? "Todas"
						: CallClassificationName.fromValue(Integer.parseInt(filter.getCallClassification())).getStr();
				String callSource = filter.getCallSource().isEmpty() ? "Todas"
						: CallSource.fromValue(Integer.parseInt(filter.getCallSource())).getStr();
				String callProgress = filter.getCallProgress().isEmpty() ? "Todas"
						: CallProgress.fromValue(Integer.parseInt(filter.getCallProgress())).getStr();
				String priority = filter.getPriority().isEmpty() ? "Todas"
						: Priority.fromValue(Integer.parseInt(filter.getPriority())).getStr();
				String entity = filter.getEntity().isEmpty() ? "Todas"
						: eb.getById(Integer.parseInt(filter.getEntity())).getName();
				String category = filter.getCategory().isEmpty() ? "Todas"
						: ecb.getById(Integer.parseInt(filter.getCategory())).getName();
				StyleBuilder styleSubreport = stl.style().setHorizontalAlignment(HorizontalAlignment.CENTER)
						.setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(stl.pen1Point()).setTabStopWidth(4);
				StyleBuilder styleTitleFilter = stl.style(stl.pen2Point()).bold()
						.setHorizontalAlignment(HorizontalAlignment.CENTER)
						.setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(stl.pen1Point())
						.setBackgroundColor(greenZem);
				StyleBuilder styleVerticalListSubreport = stl.style().setHorizontalAlignment(HorizontalAlignment.CENTER)
						.setVerticalAlignment(VerticalAlignment.MIDDLE);
				report.addTitle(cmp.verticalGap(10), cmp.text("Filtros").setStyle(styleTitleFilter));
				report.addTitle(
						cmp.horizontalList(cmp
								.verticalList(cmp.horizontalList(cmp.text("Classificação: " + (callClassification))),
										cmp.horizontalList(cmp.text("Origem: " + callSource)),
										cmp.horizontalList(cmp.text("Andamento: " + callProgress)))
								.setStyle(styleVerticalListSubreport),
								cmp
										.verticalList(cmp.horizontalList(cmp.text("Entidade: " + entity)),
												cmp.horizontalList(cmp.text("Categoria: " + category)),
												cmp.horizontalList(cmp.text("Prioridade: " + priority)))
										.setStyle(styleVerticalListSubreport))
								.setStyle(styleSubreport),
						cmp.verticalGap(20));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (DataAccessLayerException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ModelException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// end title
		VariableBuilder<Integer> variable2 = variable(exp.value(1), Calculation.COUNT);
		VariableBuilder<Integer> variable3 = variable(exp.value(Evaluation.LAST_GROUP), Calculation.COUNT);

		if (filter.getFields().contains(0)) {
			dateColumn = Columns.column("Data", "creationOrUpdateDate", DynamicReports.type.dateType()).setWidth(70)
					.setHorizontalAlignment(HorizontalAlignment.RIGHT).setPattern("dd/MM/yyyy");
			ColumnGroupBuilder dateGroup = grp.group(dateColumn).setHeaderLayout(GroupHeaderLayout.EMPTY).setPadding(0)
					.setHideColumn(false);
			report.addColumn(dateColumn);
			report.addGroup(dateGroup);
			variable2.setResetGroup(dateGroup);
		}
		if (filter.getFields().contains(1)) {
			callClassificationColumn = Columns
					.column("Classificação", "callClassification", DynamicReports.type.stringType()).setWidth(70);
			ColumnGroupBuilder callClassificationGroup = grp.group(callClassificationColumn)
					.setHeaderLayout(GroupHeaderLayout.EMPTY).setPadding(0).setHideColumn(false);
			report.addColumn(callClassificationColumn);
			report.addGroup(callClassificationGroup);
			variable2.setResetGroup(callClassificationGroup);
		}
		if (filter.getFields().contains(2)) {
			callSourceColumn = Columns.column("Origem", "callSource", DynamicReports.type.stringType()).setWidth(40);
			ColumnGroupBuilder callSourceGroup = grp.group(callSourceColumn).setHeaderLayout(GroupHeaderLayout.EMPTY)
					.setPadding(0).setHideColumn(false);
			report.addColumn(callSourceColumn);
			report.addGroup(callSourceGroup);
			variable2.setResetGroup(callSourceGroup);
		}
		if (filter.getFields().contains(3)) {
			entityColumn = Columns.column("Entidade", "entity", DynamicReports.type.stringType());
			ColumnGroupBuilder entityGroup = grp.group(entityColumn).setHeaderLayout(GroupHeaderLayout.EMPTY)
					.setPadding(0).setHideColumn(false);
			report.addColumn(entityColumn);
			report.addGroup(entityGroup);
			variable2.setResetGroup(entityGroup);
		}
		if (filter.getFields().contains(4)) {
			categoryColumn = Columns.column("Categoria", "entityCategory", DynamicReports.type.stringType());
			ColumnGroupBuilder categoryGroup = grp.group(categoryColumn).setHeaderLayout(GroupHeaderLayout.EMPTY)
					.setPadding(0).setHideColumn(false);
			report.addColumn(categoryColumn);
			report.addGroup(categoryGroup);
			variable2.setResetGroup(categoryGroup);
		}
		if (filter.getFields().contains(5)) {
			callProgressColumn = Columns.column("Andamento", "callProgress", DynamicReports.type.stringType())
					.setWidth(60);
			ColumnGroupBuilder callProgressGroup = grp.group(callProgressColumn)
					.setHeaderLayout(GroupHeaderLayout.EMPTY).setPadding(0).setHideColumn(false);
			report.addColumn(callProgressColumn);
			report.addGroup(callProgressGroup);
			variable2.setResetGroup(callProgressGroup);
		}
		if (filter.getFields().contains(6)) {
			priorityColumn = Columns.column("Prioridade", "priority", DynamicReports.type.stringType()).setWidth(60);
			ColumnGroupBuilder priorityGroup = grp.group(priorityColumn).setHeaderLayout(GroupHeaderLayout.EMPTY)
					.setPadding(0).setHideColumn(false);
			report.addColumn(priorityColumn);
			report.addGroup(priorityGroup);
			variable2.setResetGroup(priorityGroup);
		}
		if (filter.getFields().contains(7)) {
			neighborhoodColumn = Columns.column("Bairro", "neighborhood", DynamicReports.type.stringType());
			ColumnGroupBuilder neighborhoodGroup = grp.group(neighborhoodColumn)
					.setHeaderLayout(GroupHeaderLayout.EMPTY).setPadding(0).setHideColumn(false);
			report.addColumn(neighborhoodColumn);

			report.addGroup(neighborhoodGroup);
			variable2.setResetGroup(neighborhoodGroup);
		}
		if (filter.getType().equals("relacionado"))
			report.addColumn(
					Columns.column("Descrição", "description", DynamicReports.type.stringType()).setWidth(200));
		ComponentColumnBuilder countColumn;
		if (filter.getType().equals("consolidado")) {
			countColumn = Columns.componentColumn("Total", cmp.text(variable2)).setWidth(50);
			report.addColumn(countColumn);
		}
		// Highlight Rows
		report.highlightDetailEvenRows();
		// ignora campos duplicados para fazer o piechart
		report.addProperty("net.sf.jasperreports.chart.pie.ignore.duplicated.key", "true");
		// report.addProperty("net.sf.jasperreports.default.csv.encoding",
		// "UTF-8");
		// report.addProperty("net.sf.jasperreports.default.pdf.encoding",
		// "UTF-8");

		// Gráficos
		// Configuração do summary para que ele possa efetuar quebra de
		// página e para que sempre comece em uma nova página.
		if (filter.getFormat().equals("pdf"))
			report.pageFooter(cmp.horizontalFlowList(cmp.text("Criado por " + filter.getUser() + " em "
					+ new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(new Date()))));
		report.setDataSource(list);
		report.setTemplate(Templates.reportTemplate).pageFooter(Templates.footerComponent);
		// Gráficos
		// Configuração do summary para que ele possa efetuar quebra de
		// página e para que sempre comece em uma nova página.
		report.setSummarySplitType(SplitType.IMMEDIATE).summaryOnANewPage();
		if (filter.getType().equals("consolidado") && (filter.getFormat().equals("pdf"))) {
			// Título "Gráficos".
			report.addSummary(cmp.text("Gráficos")
					.setStyle(stl.style().bold().setFontSize(13).setHorizontalAlignment(HorizontalAlignment.CENTER)));
			if (filter.getChart().equals("bar")) {// Gráfico de Barras
				if (filter.getFields().contains(1))
					report.addSummary(cmp.verticalGap(30), createBarChart("Classificação", callClassificationColumn));
				if (filter.getFields().contains(2))
					report.addSummary(cmp.verticalGap(30), createBarChart("Origem", callSourceColumn));
				if (filter.getFields().contains(3))
					report.addSummary(cmp.verticalGap(30), createBarChart("Entidade", entityColumn));
				if (filter.getFields().contains(4))
					report.addSummary(cmp.verticalGap(30), createBarChart("Categoria", categoryColumn));
				if (filter.getFields().contains(5))
					report.addSummary(cmp.verticalGap(30), createBarChart("Andamento", callProgressColumn));
				if (filter.getFields().contains(6))
					report.addSummary(cmp.verticalGap(30), createBarChart("Prioridade", priorityColumn));
				if (filter.getFields().contains(7))
					report.addSummary(cmp.verticalGap(30), createBarChart("Bairro", neighborhoodColumn));
			} else if (filter.getChart().equals("pie")) {// Gráfico de Pizza
				if (filter.getFields().contains(1))
					report.addSummary(cmp.verticalGap(30), createPieChart("Classificação", callClassificationColumn));
				if (filter.getFields().contains(2))
					report.addSummary(cmp.verticalGap(30), createPieChart("Origem", callSourceColumn));
				if (filter.getFields().contains(3))
					report.addSummary(cmp.verticalGap(30), createPieChart("Entidade", entityColumn));
				if (filter.getFields().contains(4))
					report.addSummary(cmp.verticalGap(30), createPieChart("Categoria", categoryColumn));
				if (filter.getFields().contains(5))
					report.addSummary(cmp.verticalGap(30), createPieChart("Andamento", callProgressColumn));
				if (filter.getFields().contains(6))
					report.addSummary(cmp.verticalGap(30), createPieChart("Prioridade", priorityColumn));
				if (filter.getFields().contains(7))
					report.addSummary(cmp.verticalGap(30), createPieChart("Bairro", neighborhoodColumn));
			}
		}
		try {
			if (filter.getFormat().equals("pdf"))
				report.toPdf(buffer);
			else
				report.toCsv(buffer);
		} catch (DRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@GET
	@Path("/pdf")
	@Produces("application/pdf")
	public Response generateReportPdf(@BeanParam ReportFilterBean filter)
			throws HibernateException, SQLException, ModelException, ParseException, DRException, JRException {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		reportC(filter, buffer);
		StreamingOutput fileStream = new StreamingOutput() {
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
				try {
					byte[] data = buffer.toByteArray();
					output.write(data);
					output.flush();
					buffer.flush();
					buffer.close();
				} catch (Exception e) {
					throw new WebApplicationException("File Not Found !!");
				}
			}
		};
		return Response.ok(fileStream).header("content-disposition", "attachment; filename = report.pdf").build();
	}

	@GET
	@Path("/csv")
	@Produces("application/csv")
	public Response generateReportCsv(@BeanParam ReportFilterBean filter)
			throws HibernateException, SQLException, ModelException, ParseException, DRException, JRException {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		reportC(filter, buffer);
		StreamingOutput fileStream = new StreamingOutput() {
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
				try {
					byte[] data = buffer.toByteArray();
					output.write(data);
					output.flush();
					buffer.flush();
					buffer.close();
				} catch (Exception e) {
					throw new WebApplicationException("File Not Found !!");
				}
			}
		};
		return Response.ok(fileStream).header("content-disposition", "attachment; filename = report.csv").build();
	}
	
	public Configuration getCityConfig(){
		ConfigurationService configurationService = new ConfigurationService();
		Configuration config = configurationService.searchConfigObject();
		return config;
	}

}
