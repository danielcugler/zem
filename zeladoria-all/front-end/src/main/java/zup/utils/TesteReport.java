package zup.utils;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

public class TesteReport {
public static void main(String[] args) {
	JasperReportBuilder report = DynamicReports.report();
	FileInputStream zem = null;
	FileInputStream ptc = null;

	try {
		zem = new FileInputStream(new File("src/main/java/images/logoZem.png"));
		ptc = new FileInputStream(new File("src/main/java/images/patrocinio.jpg"));
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	StyleBuilder boldStyle = stl.style().bold();
	StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
	StyleBuilder titleStyle = stl.style(boldCenteredStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
			.setFontSize(15);

	report.addTitle(cmp.horizontalList()
			.add(cmp.image(ptc)
							.setHorizontalAlignment(HorizontalAlignment.RIGHT).setFixedWidth(90))
			.add(cmp.text("consolidado").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.CENTER))
			.add(cmp.image(zem).setHorizontalAlignment(HorizontalAlignment.LEFT).setFixedWidth(90)
					)
			.add(cmp.text("Zeladoria Municipal").setStyle(titleStyle)
					.setHorizontalAlignment(HorizontalAlignment.LEFT).setFixedWidth(90))
								.newRow().add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)));
try {
	report.show();
} catch (DRException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}
}
