/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
@WebServlet(name = "CSVServlet", urlPatterns =
{
    "/CSVServlet"
})
public class CSVServlet extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CSVServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CSVServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String strData = request.getParameter("hidden_CSVData");
        String[] strSplitData = strData.split("###");
        String strBerichtname = strSplitData[0];
        String strTable = strSplitData[1];

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + strBerichtname + ".csv");
        String [] strRows = erstelleCSVString(strTable);
        writeCsv(strRows, response.getOutputStream());
    }

    /**
     * Zerlegt den HTML String damit es möglich ist ein CSV File zu erstellen
     * Zurückgegeben wird ein Sting Feld an Zeilen mit Informationen geteilt durch einen Strichpunkt
     * @param strTable
     * @return 
     */
    public String [] erstelleCSVString(String strTable)
    {
        String strCSV = strTable.replace("<table id=\"table\" class=\"ui sortable celled table\"> <thead>     ", "");
        strCSV = strCSV.replace("</tbody></table>", "");
        strCSV = strCSV.replace("</thead><tbody>", "");
        strCSV = strCSV.replaceAll("\\<th[^>]*>", ""); //Wie funktioniert das??
        strCSV = strCSV.replaceAll("</th>", ";");
        strCSV = strCSV.replaceAll("<td>", "");
        strCSV = strCSV.replaceAll("</td>", ";");
        strCSV = strCSV.replaceAll("<tr>", "");
        strCSV = strCSV.replaceAll("<b>", "");
        strCSV = strCSV.replaceAll("</b>", "");
        String[] strRows = strCSV.split("</tr>");
        
        return strRows;
    }

    /**
     * Schreibt die Informationen von strRows in den BufferedWriter
     * strRows sind die Zeilen die in das Dokument geschrieben werden
     * output ist der von der Response bekommene OutputStream
     * @param strRows
     * @param output
     * @throws IOException 
     */
    public void writeCsv(String[] strRows, OutputStream output) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
        for (String strRow : strRows)
        {
            writer.append(strRow);
            writer.newLine();
        }
        writer.flush();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
