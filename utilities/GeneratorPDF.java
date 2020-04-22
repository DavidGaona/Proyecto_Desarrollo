package utilities;


import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;



public class GeneratorPDF {

    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;

    public void createPDF (String pdfFilename){

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            String path = pdfFilename;
            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(path));
            doc.addAuthor("MobilePlan");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("ModiPlanSolutions");
            doc.addTitle("Factura");
            doc.setPageSize(PageSize.LETTER);

            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();
            generateLayout(doc, cb);
            generateHeader(doc, cb);
            generateDetail(doc, cb, 1, 615);
            generateBarCode(doc,cb,"1144186919");
            printPageNumber(cb);
            printPageNumber(cb);

        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
            if (docWriter != null)
            {
                docWriter.close();
            }
        }
    }

    private void generateBarCode(Document doc, PdfContentByte cb, String code){
            try {
                Barcode128 code128 = new Barcode128();
                code128.setCode(code);
                code128.setCodeType(Barcode128.CODE128);
                code128.setX(1.1f);
                PdfTemplate template = code128.createTemplateWithBarcode(
                        cb, BaseColor.BLACK, BaseColor.BLACK);

                cb.addTemplate(template,50,380);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

    }

    private void generateLayout(Document doc, PdfContentByte cb)  {

        try {

            cb.setLineWidth(1f);

            // Invoice Header box layout
            cb.rectangle(420,700,150,60);
            cb.moveTo(420,720);
            cb.lineTo(570,720);
            cb.moveTo(420,740);
            cb.lineTo(570,740);
            cb.moveTo(480,700);
            cb.lineTo(480,760);
            cb.stroke();

            // Invoice Header box Text Headings
            createHeadings(cb,422,743,"Cuenta ID.");
            createHeadings(cb,422,723,"Plan ID.");
            createHeadings(cb,422,703,"Generado en");

            // Invoice Detail box layout
            cb.rectangle(20,500,550,150);
            cb.moveTo(20,500);
            cb.lineTo(20,650);
            cb.moveTo(50,500);
            cb.lineTo(50,650);
            cb.moveTo(150,500);
            cb.lineTo(150,650);
            cb.moveTo(430,500);
            cb.lineTo(430,650);
            cb.moveTo(500,500);
            cb.lineTo(500,650);
            cb.stroke();

            // Invoice Detail box Text Headings
            createHeadings(cb,22,633,"Item");
            createHeadings(cb,52,633,"Nombre del plan");
            createHeadings(cb,152,633,"Descripcion del plan");
            createHeadings(cb,432,633,"Extras");
            createHeadings(cb,502,633,"Total");

            //add the images
            //Image companyLogo = Image.getInstance("images/olympics_logo.gif");
            //companyLogo.setAbsolutePosition(25,700);
            //companyLogo.scalePercent(25);
            //doc.add(companyLogo);

        }

        //catch (DocumentException dex){
          //  dex.printStackTrace();
        //}
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateHeader(Document doc, PdfContentByte cb)  {

        try {
            createHeadings(cb,200,750,"Alexander Gonzalez");
            createHeadings(cb,200,735,"Numero: 3338877445");
            createHeadings(cb,200,720,"Address Line 2");
            createHeadings(cb,200,705,"Cali, Valle del Cauca");
            createHeadings(cb,200,690,"Colombia");

            createHeadings(cb,482,743,"ABC0001");
            createHeadings(cb,482,723,"123456");
            createHeadings(cb,482,703, "10-03-2020");

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void generateDetail(Document doc, PdfContentByte cb, int index, int y)  {
        DecimalFormat df = new DecimalFormat("0.00");

        try {

            createContent(cb,48,y,String.valueOf(index+1),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,52,y, "ITEM" + (index+1),PdfContentByte.ALIGN_LEFT);
            createContent(cb,152,y, "Product Description - SIZE " + (index+1),PdfContentByte.ALIGN_LEFT);

            double price = Double.valueOf(df.format(Math.random() * 10));
            double extPrice = price * (index+1) ;
            createContent(cb,498,y, df.format(price),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,568,y, df.format(extPrice),PdfContentByte.ALIGN_RIGHT);

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void createHeadings(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void printPageNumber(PdfContentByte cb){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 25, 0);
        cb.endText();

        pageNumber++;

    }

    private void createContent(PdfContentByte cb, float x, float y, String text, int align){


        cb.beginText();
        cb.setFontAndSize(bf, 8);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    private void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
