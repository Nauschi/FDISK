/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

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
        Rectangle rect = writer.getBoxSize("art");
         ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("Seite %d", pagenumber)),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18,0);
    }
    
}
