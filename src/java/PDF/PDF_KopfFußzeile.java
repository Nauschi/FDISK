/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class PDF_KopfFußzeile extends PdfPageEventHelper
{

    int pagenumber;

    @Override
    public void onStartPage(PdfWriter writer, Document document)
    {
        pagenumber++;
    }
    


    @Override
    public void onEndPage(PdfWriter writer, Document document)
    {
        
                    Rectangle rect = writer.getBoxSize("footer");
         ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("Seite %d", pagenumber)),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18,0);
//         try
//        {
//            PdfPTable table = new PdfPTable(3);
//
//            table.setWidths(new int[]
//            {
//                24, 24, 2
//            });
//            table.setTotalWidth(527);
//            table.setLockedWidth(true);
//            table.getDefaultCell().setFixedHeight(20);
//            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
//            table.addCell("123");
//            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//            table.addCell(String.format("Page %d of", writer.getPageNumber()));
//            PdfPCell cell = new PdfPCell(new Phrase(pagenumber));
//            cell.setBorder(Rectangle.BOTTOM);
//            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            table.addCell(cell);
//            table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
//        } catch (DocumentException ex)
//        {
//            Logger.getLogger(PDF_KopfFußzeile.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

}
