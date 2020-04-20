package zup.report;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.FillerBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

public class DynamicReportGettingStarted {

	public static void main(String[] args) throws FileNotFoundException, DRException {

		// dynamic report
		JasperReportBuilder report = DynamicReports.report();

		// styles
		StyleBuilder boldStyle = DynamicReports.stl.style().bold();
		StyleBuilder boldCentered = DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder columnHeaderStyle = DynamicReports.stl.style(boldCentered).setBorder(DynamicReports.stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);

		// add title
		TextFieldBuilder<String> title = DynamicReports.cmp.text("Zeladoria Urbana Municipal"
				+ ""
				+ "");
		title.setStyle(boldCentered).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		// get image
		InputStream stream = DynamicReportGettingStarted.class.getResourceAsStream("/login-logo.png");
		ImageBuilder img = DynamicReports.cmp.image(stream).setFixedDimension(80, 80).setStyle(DynamicReports.stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT));
		FillerBuilder filler = DynamicReports.cmp.filler().setStyle(DynamicReports.stl.style().setTopBorder(DynamicReports.stl.pen2Point())).setFixedHeight(2);
		report.title(DynamicReports.cmp.horizontalFlowList(img, title).newRow().add(filler));

		// add tables
		// add columns
		TextColumnBuilder<String> customerNameColumn = Columns.column("Usuário", "customerName", DynamicReports.type.stringType());
		TextColumnBuilder<String> itemColumn = Columns.column("Bairro", "item", DynamicReports.type.stringType());
		TextColumnBuilder<Integer> quantityColumn = Columns.column("Quantidade de Chamados", "quantity", DynamicReports.type.integerType());
	

		// total price column
		

		// row num
		TextColumnBuilder<Integer> rowNumColumn = Columns.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalAlignment(HorizontalAlignment.CENTER);

		// add columns
		report.columns(rowNumColumn, customerNameColumn, itemColumn, quantityColumn);
		report.addColumn(customerNameColumn);
		// column title style
		report.setColumnTitleStyle(columnHeaderStyle);
		// add data source
		report.setDataSource("", null);

		// footer
		report.pageFooter(DynamicReports.cmp.pageXofY());

		/* // charts
		Bar3DChartBuilder chart1 = DynamicReports.cht.bar3DChart().setTitle("Usuários").setCategory(customerNameColumn).addSerie(DynamicReports.cht.serie(quantityColumn))
				.setUseSeriesAsCategory(true);
		Bar3DChartBuilder chart2 = DynamicReports.cht.bar3DChart().setTitle("Bairro").setCategory(itemColumn).addSerie(DynamicReports.cht.serie(quantityColumn))
				.setUseSeriesAsCategory(true);
		// add chart to summary
		report.summary(DynamicReports.cmp.horizontalFlowList(chart1, chart2));

		// report.groupBy(itemColumn); */

		// higligh rows
		report.highlightDetailEvenRows();

		report.show();
		// pdf
		report.toPdf(new FileOutputStream(new File("c:/temp/report.pdf")));

		// word
		report.toDocx(new FileOutputStream(new File("c:/temp/report.docx")));
		// html
		report.toXhtml(new FileOutputStream(new File("c:/temp/report.html")));

		// text
		report.toText(new FileOutputStream(new File("c:/temp/report.txt")));

		System.out.println("Report completed...");

	}

	

}
