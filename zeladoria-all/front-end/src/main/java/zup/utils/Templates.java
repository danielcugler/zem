package zup.utils;


import static net.sf.dynamicreports.report.builder.DynamicReports.*;

 

import java.awt.Color;

import java.util.Locale;

 

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;

import net.sf.dynamicreports.report.builder.style.StyleBuilder;

import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;

import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import net.sf.dynamicreports.report.definition.ReportParameters;

 

/**
042
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
043
 */

public class Templates {

   public static final StyleBuilder rootStyle;

   public static final StyleBuilder boldStyle;

   public static final StyleBuilder italicStyle;

   public static final StyleBuilder boldCenteredStyle;

   public static final StyleBuilder bold12CenteredStyle;

   public static final StyleBuilder bold18CenteredStyle;

   public static final StyleBuilder bold22CenteredStyle;

   public static final StyleBuilder columnStyle;

   public static final StyleBuilder columnTitleStyle;

   public static final StyleBuilder groupStyle;

   public static final StyleBuilder subtotalStyle;
   public static final StyleBuilder footerStyle;

 

   public static final ReportTemplateBuilder reportTemplate;

   public static final CurrencyType currencyType;

   public static final ComponentBuilder<?, ?> dynamicReportsComponent;

   public static final ComponentBuilder<?, ?> footerComponent;

 

   static {

      rootStyle           = stl.style().setPadding(2);

      boldStyle           = stl.style(rootStyle).bold();

      italicStyle         = stl.style(rootStyle).italic();

      boldCenteredStyle   = stl.style(boldStyle)

                               .setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);

      bold12CenteredStyle = stl.style(boldCenteredStyle)

                               .setFontSize(12);

      bold18CenteredStyle = stl.style(boldCenteredStyle)

                               .setFontSize(18);

      bold22CenteredStyle = stl.style(boldCenteredStyle)

                             .setFontSize(22);

      columnStyle         = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)	.setBorder(DynamicReports.stl.pen1Point());;

      columnTitleStyle    = stl.style(columnStyle).setBackgroundColor(new Color(104, 195, 159))

                              .setBorder(stl.pen1Point())

                               .setHorizontalAlignment(HorizontalAlignment.CENTER)

                              

                               .bold();

      groupStyle          = stl.style(boldStyle)

                               .setHorizontalAlignment(HorizontalAlignment.LEFT);

      subtotalStyle       = stl.style(boldStyle)

                               .setTopBorder(stl.pen1Point());
      footerStyle= stl.style(boldStyle).setForegroundColor(Color.LIGHT_GRAY)
    			  .setHorizontalAlignment(HorizontalAlignment.CENTER);
    			 
 

      StyleBuilder crosstabGroupStyle      = stl.style(columnTitleStyle);

      StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)

                                                .setBackgroundColor(new Color(170, 170, 170));

      StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)

                                                .setBackgroundColor(new Color(140, 140, 140));

      StyleBuilder crosstabCellStyle       = stl.style(columnStyle)

                                                .setBorder(stl.pen1Point());

 

      TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()

         .setHeadingStyle(0, stl.style(rootStyle).bold());

 

      reportTemplate = template()

    		  .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                         .setColumnStyle(columnStyle)
                         .setColumnTitleStyle(columnTitleStyle)

                         .setGroupStyle(groupStyle)

                        .setGroupTitleStyle(groupStyle)

                         .setSubtotalStyle(subtotalStyle)
                         .setColumnFooterStyle(footerStyle)

                         .highlightDetailEvenRows()

                         .crosstabHighlightEvenRows()

                         .setCrosstabGroupStyle(crosstabGroupStyle)

                         .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)

                         .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)

                         .setCrosstabCellStyle(crosstabCellStyle)

                         .setTableOfContentsCustomizer(tableOfContentsCustomizer);

 

      currencyType = new CurrencyType();

 

      HyperLinkBuilder link = hyperLink("http://www.dynamicreports.org");

      dynamicReportsComponent =

        cmp.horizontalList(

         cmp.image(Templates.class.getResource("zemLogo.png")).setFixedDimension(60, 60),

         cmp.verticalList(
            cmp.text("DynamicReports").setStyle(bold22CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
            cmp.text("http://www.dynamicreports.org").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(300);
 
      footerComponent = cmp.pageXofY()
                           .setStyle(
                              stl.style(boldCenteredStyle)
                                 .setTopBorder(stl.pen1Point()));
   }
   public static ComponentBuilder<?, ?> createTitleComponent(String label) {
      return cmp.horizontalList()
              .add(
               dynamicReportsComponent,
               cmp.text(label).setStyle(bold18CenteredStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT))
              .newRow()
              .add(cmp.line())
              .newRow()
              .add(cmp.verticalGap(10));
   }
   public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
      return new CurrencyValueFormatter(label);
   }
   public static class CurrencyType extends BigDecimalType {
      private static final long serialVersionUID = 1L;
      @Override
      public String getPattern() {
         return "$ #,###.00";
      }
   }
   private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {
      private static final long serialVersionUID = 1L;
      private String label;
 
      public CurrencyValueFormatter(String label) {
         this.label = label;
      }
 
      
      public String format(Number value, ReportParameters reportParameters) {
         return label + currencyType.valueToString(value, reportParameters.getLocale());

      }

   }

}