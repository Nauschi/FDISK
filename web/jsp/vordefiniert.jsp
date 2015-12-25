<%-- 
    Document   : vordefiniert
    Created on : 02.08.2015, 13:14:24
    Author     : user
aaaaasdfsdf
--%>

<%@page import="Beans.Berechtigung"%>
<%@page import="java.lang.Object"%>
<%@page import="Beans.Rohbericht"%>
<%@page import="java.util.LinkedList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="semantic/dist/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="css/standardDesign.css">
        <link rel="stylesheet" type="text/css" href="css/vordefiniert.css">
        <link rel="stylesheet" href="css/jquery-ui_1.css">
        <link href="css/jquery-ui_2.css" rel="Stylesheet" type="text/css"/>
        <title>Vordefiniert</title>
    </head>
    <body>
        <%
            session.setAttribute("lastPage", "vordefiniert");
        %>

        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="res/logo_oben.png">
                </br>
            </div>

            <div class="ui menu" style="background-color: #C00518; width: 100%">

                <a class="item active linkMenu">
                    Vordefiniert
                </a>
                <form action="MainServlet" method="POST" name="form_dynamisch">
                    <input type="hidden" name="dynamisch">
                    <a href="#" onclick="document.form_dynamisch.submit();" class="item linkMenu">
                        Dynamisch
                    </a>
                </form>
                <div class="right menu">
                    <form action="MainServlet" method="POST" name="form_logout">
                        <input type="hidden" name="logout">
                        <a href="#" onclick="document.form_logout.submit();" class="ui item linkMenu">
                            Logout
                        </a>
                    </form>
                </div>

            </div>
        </div>        
        <h1>Vordefiniert</h1>
        <div class="ui grid" id="div_mitte">
            <div id="div_loader" class="ui active inverted dimmer">
                <div class="ui large text loader">Loading..</div>
            </div>
            <div class="four wide column">
                <div class="ui vertical fluid tabular menu" id="div_liste">

                    <%
                        LinkedList<Rohbericht> liRohberichte = (LinkedList<Rohbericht>) application.getAttribute("rohberichte");
                        for (int i = 0; i < liRohberichte.size(); i++)
                        {
                            Rohbericht rohbericht = liRohberichte.get(i);
                            out.println(rohbericht.toHTMLString());
                        }
                    %>

                </div>
            </div>

            <div class="twelve wide stretched column">
                <div class="ui segment" id="div_daten">
                    <form action="MainServlet" method="POST">
                        <h2 id="h2_bericht"></h2>
                        <input type="hidden" name="input_aktbericht" id="input_hidden"/>

                        <div class="ui equal width grid">
                            <div class="column">
                                <select name="select_kA" class="ui fluid dropdown" id="select_kA">
                                    <option value="">Abschnitt</option>
                                    <option value="Test1">Test1</option>
                                    <option value="Test2">Test2</option>
                                    <option value="Test3">Test3</option>
                                    <option value="Test4">Test4</option>
                                    <option value="Test5">Test5</option>
                                    <option value="Test6">Test6</option>
                                    <option value="Test7">Test7</option>
                                    <option value="Test8">Test8</option>
                                </select>
                            </div>
                            <div class="column">
                                <select name="select_bezirk" class="ui fluid dropdown" id="select_bezirk">
                                    <option value="">Bezirk</option>
                                    <option value="Bezirk1">Bezirk1</option>
                                    <option value="Bezirk2">Bezirk2</option>
                                    <option value="Bezirk3">Bezirk3</option>
                                    <option value="Bezirk4">Bezirk4</option>
                                    <option value="Bezirk5">Bezirk5</option>
                                    <option value="Bezirk6">Bezirk6</option>
                                    <option value="Bezirk7">Bezirk7</option>
                                    <option value="Bezirk8">Bezirk8</option>
                                </select>
                            </div>
                            <div class="column">
                                <select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr">
                                    <option value="">Feuerwehr</option>
                                    <option value="Feuerwehr1">Feuerwehr1</option>
                                    <option value="Feuerwehr2">Feuerwehr2</option>
                                    <option value="Feuerwehr3">Feuerwehr3</option>
                                </select>
                            </div>
                            <!--<div class="column">

                                <select name="select_berechtigung" class="ui fluid dropdown" id="select_feuerwehr">
                            <%
                                //LinkedList<Berechtigung> liBerechtigung = (LinkedList<Berechtigung>) session.getAttribute("berechtigungen");
                                //for (Berechtigung be : liBerechtigung)
                                //{
                                //   out.println("<option value=" + be.getIntIDGruppe() + ">" + be.getStrBerechtigung() + "</option>");
                                //}
                            %>
                        </select>
                    </div>-->
                        </div>
                        <div class="ui equal width grid">
                            <div class="column" id="div_input_von_datum" style="display: none">
                                <div class="ui input" style="width: 100%">
                                    <input name="input_von_datum" id="input_von_datum" placeholder="von..." autocomplete="off" type="text">
                                </div>
                            </div>
                            <div class="column" id="div_input_bis_datum" style="display: none">
                                <div class="ui input" style="width: 100%">
                                    <input name="input_bis_datum" id="input_bis_datum" placeholder="bis..." autocomplete="off" type="text">
                                </div>
                            </div >
                            <div class="column" id="div_kein_datum_1" style="display: none">
                            </div>
                            <div class="column" id="div_kein_datum_2" style="display: none">
                            </div>
                            <div class="column" id="div_select_jahr" style="display: none">
                                <select name="select_jahr" class="ui fluid dropdown" id="select_jahr" style="display: none">
                                    <option value="">Jahr</option>
                                    <option value="Feuerwehr1">2015</option>
                                    <option value="Feuerwehr2">2014</option>
                                    <option value="Feuerwehr3">2013</option>
                                </select>
                            </div>
                            
                            <div class="column">
                                <button type="submit" name="button_vorschau" class="ui button styleGrau" onclick="document.getElementById('div_loader').className = 'ui active inverted dimmer';" style="background-color: #707173; width: 100%; color: white;">Vorschau</button>
                            </div>
                        </div>
                    </form>

                    <form id="formPDF" name="formPDF" action="PDFServlet" method="POST" target="_blank">
                        <input type="hidden" name="hidden_pdfData" id="hidden_pdfData"/>
                    </form>
                    <form id="formCSV" name="formCSV" action="CSVServlet" method="POST">
                        <input type="hidden" name="hidden_CSVData" id="hidden_CSVData"/>
                    </form>
                    <div id="div_table">
                    </div>
                    <div id="div_abbrechen_bestaetigen" style="display:none" class="ui segment">
                        <div class="ui equal width grid">
                            <div class="column">
                                <button type="button" class="ui button styleRot" onClick="saveDataForPDF()" style="background-color: #C00518; width: 100%; color: white;">PDF</button>
                            </div>
                            <div class="column">
                                <button type="button" class="ui button styleGruen" onClick="saveDataForCSV()"  style="background-color: #007336; width: 100%; color: white;">CSV</button>
                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>
        <br/>

        <script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script src="js/jquery-ui.js"></script> 
        <script src="js/tablesort.js"></script>
        <script src="js/vordefiniert.js"></script>
        <script>

                                    $(document).ready(function () {
            <%                if (request.getParameter("input_aktbericht") == null)
                {
            %>
                                        var item = document.getElementById("div_liste").getElementsByTagName("a")[0];
            <%
            } else
            {
            %>
                                        var item = document.getElementById("<%=request.getParameter("input_aktbericht")%>");
            <%
                }
            %>

                                        item.className = "item active";
                                        var strBerichtname = item.getElementsByTagName("span")[0].innerHTML;
                                        document.getElementById("div_daten").getElementsByTagName("h2")[0].innerHTML = strBerichtname;
                                        document.getElementById("input_hidden").value = strBerichtname;
                                        var strTable = item.getElementsByTagName("div")[0].innerHTML;
                                        document.getElementById("div_table").innerHTML = strTable;
                                        var intTypeOfDateUI = item.getElementsByTagName("div")[1].innerHTML;
                                        onChangeTypeOfDateUI(intTypeOfDateUI);
                                        
            <%
                if (request.getAttribute("liste") != null)
                {
                    LinkedList<Object> liBerichtDaten = (LinkedList<Object>) request.getAttribute("liste");
                    System.out.println("Vordefiniert: Liste.size: " + liBerichtDaten.size());
                    String strHTML = "";
                    int i = 0;
                    while (i < liBerichtDaten.size() - 1)
                    {
                        if (request.getParameter("input_aktbericht").contains(" leer") && i % 3 == 0)
                        {
                            strHTML += "<tr>";
                        }
                        Object zeile = liBerichtDaten.get(i);
                        strHTML += zeile.toString();
                        i++;
                        if (request.getParameter("input_aktbericht").contains(" leer") && i % 3 == 0)
                        {
                            strHTML += "</tr>";
                        }
                    }
                    if (request.getParameter("input_aktbericht").contains(" leer") && i % 3 != 0)
                    {
                        strHTML += "</tr>";
                    }
                    System.out.println("Vordefiniert: strHTML: " + strHTML);
            %>


            <%
                if (request.getParameter("input_aktbericht") != null && request.getParameter("input_aktbericht").contains(" leer"))
                {
            %>

                                        document.getElementById("hidden_pdfData").value = "<%=request.getParameter("input_aktbericht")%>###<%=strHTML%>";
                                                document.formPDF.submit();
            <%
            } else
            {%>
                                                document.getElementById("div_abbrechen_bestaetigen").style.display = "block";
                                                document.getElementById("div_table").getElementsByTagName("tbody")[0].innerHTML = "<%=strHTML%>";
                                                $('.sortable.table').tablesort();
                                                $('th').popup();
            <%
                    }
                }
            %>
                                                document.getElementById("div_loader").className = "ui disabled loader";
                                            });



        </script>        

    </body>
</html>
