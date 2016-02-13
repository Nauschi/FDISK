<%-- 
    Document   : vordefiniert
    Created on : 02.08.2015, 13:14:24
    Author     : user
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="Beans.Feuerwehr"%>
<%@page import="Beans.Abschnitt"%>
<%@page import="Beans.Bezirk"%>
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
        <link rel="stylesheet" type="text/css" href="css/jquery-ui_1.css">
        <link rel="stylesheet" type="text/css" href="css/jquery-ui_2.css">
        <title>Vordefiniert</title>
    </head>
    <body>
        <%!
            private int intIDGruppe;
        %>
        <%
            intIDGruppe = -1;
            session.setAttribute("lastPage", "vordefiniert");
        %>

        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="res/logo_oben.png">
                </br>
            </div>

            <div class="ui menu" id="div_menu">
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

                            <div class="column" id="div_bezirk" disabled>
                                <fieldset id="fieldset_bezirk">
                                    <legend><b>Bezirk</b></legend>
                                    <select  name="select_bezirk" class="ui fluid dropdown" id="select_bezirk" onchange="bezirkChanged(this)">
                                        <%=generiereBezirk(session)%>
                                    </select>
                                </fieldset>
                            </div>
                            <%
                                if (intIDGruppe == 5)
                                {
                                    out.println(generiereHiddenBezirkDiv(session));
                                }
                            %>
                            <div class="column" id="div_abschnitt">
                                <fieldset id="fieldset_abschnitt">
                                    <legend><b>Abschnitt</b></legend>
                                    <select name="select_abschnitt" class="ui fluid dropdown" id="select_abschnitt" onchange="abschittChanged(this)">
                                        <%=generiereAbschnitt(session)%>
                                    </select>
                                </fieldset>
                            </div>

                            <%
                                if (intIDGruppe == 15 || intIDGruppe == 5)
                                {
                                    out.println(generiereHiddenAbschnittDiv(session));
                                }
                            %>
                            <div class="column" id="div_feuerwehr">
                                <fieldset id="fieldset_feuerwehr">
                                    <legend><b>Feuerwehr</b></legend>
                                    <select name="select_feuerwehr" class="ui fluid disabled dropdown" id="select_feuerwehr">
                                        <%=generiereFeuerwehr(session)%>
                                    </select>
                                </fieldset>
                            </div>
                        </div>
                        <div class="ui equal width grid">
                            <div class="column" id="div_input_von_datum" style="display: none">
                                <fieldset id="fieldset_datum_von">
                                    <legend><b>Datum von</b></legend>
                                    <div class="ui input" style="width: 100%">
                                        <%String str_input_von_datum = request.getParameter("input_von_datum");%>
                                        <input name="input_von_datum" id="input_von_datum" placeholder="von..." autocomplete="off" readonly="true" type="text"
                                               <%=(str_input_von_datum != null) ? "value='" + str_input_von_datum + "'" : ""%>
                                               >
                                        <div id="div_remove_von_datum" style="display:  <%=(str_input_von_datum != null && !str_input_von_datum.isEmpty()) ? "block" : "none"%>;">
                                            <button type="button" class="ui button styleRot" onclick="removeDateAndSetDivHidden('div_remove_von_datum', 'input_von_datum')" title="Lösche 'Datum von'">X</button>
                                        </div>
                                    </div>
                                </fieldset>
                            </div>
                            <div class="column" id="div_input_bis_datum" style="display: none">
                                <fieldset id="fieldset_datum_bis">
                                    <legend><b>Datum bis</b></legend>
                                    <div class="ui input" style="width: 100%">
                                        <%String str_input_bis_datum = request.getParameter("input_bis_datum");%>
                                        <input name="input_bis_datum" id="input_bis_datum" placeholder="bis..." autocomplete="off" readonly="true" type="text" 
                                               <%=(str_input_bis_datum != null) ? "value='" + str_input_bis_datum + "'" : ""%>
                                               >
                                        <div id="div_remove_bis_datum" style="display: <%=(str_input_bis_datum != null && !str_input_bis_datum.isEmpty()) ? "block" : "none"%>;">
                                            <button type="button" class="ui button styleRot" onclick="removeDateAndSetDivHidden('div_remove_bis_datum', 'input_bis_datum')" title="Lösche 'Datum bis'">X</button>
                                        </div>
                                    </div>
                                </fieldset>
                            </div >
                            <div class="column" id="div_kein_datum_1" style="display: none">
                            </div>
                            <div class="column" id="div_kein_datum_2" style="display: none">
                            </div>

                            <div class="column" id="div_select_jahr" style="display: none">
                                <fieldset id="fieldset_jahr">
                                    <legend><b>Jahr</b></legend>
                                    <select name="select_jahr" class="ui fluid dropdown" id="select_jahr" style="display: none">
                                        <%

                                            int intYear = LocalDate.now().getYear();
                                            if (request.getParameter("select_jahr") != null)
                                            {
                                                int intLastSelectedYear = Integer.parseInt(request.getParameter("select_jahr"));
                                                for (int i = -1; i < 2; i++)
                                                {
                                        %>
                                        <option value="<%=(intYear + i)%>" <%=((intYear + i) == intLastSelectedYear) ? "selected" : ""%>><%=(intYear + i)%></option>
                                        <%
                                            }
                                        } else
                                        {
                                            for (int i = -1; i < 2; i++)
                                            {
                                        %>
                                        <option value="<%=(intYear + i)%>" <%=(i == 0) ? "selected" : ""%>><%=(intYear + i)%></option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </fieldset>
                            </div>
                            <div class="column" id="div_kennzeichen" style="display: none;">
                                <fieldset id="fieldset_kennzeichen">
                                    <legend><b>Kennzeichen</b></legend>
                                    <div class="ui input">
                                        <input id="input_kennzeichen" name="input_kennzeichen" placeholder="Kennzeichen" type="text">
                                    </div>
                                </fieldset>
                            </div>

                            <div class="column">
                                <fieldset id="fieldset_button">
                                    <legend>&nbsp;</legend>
                                    <button type="submit" name="button_vorschau" class="ui button styleGrau" onclick="document.getElementById('div_loader').className = 'ui active inverted dimmer';" title="Vorschau erstellen" style="width: 100%;">Vorschau</button>
                                </fieldset>
                            </div>
                        </div>
                    </form>

                    <form id="formPDF" name="formPDF" action="PDFServlet" method="POST" target="_blank">
                        <input type="hidden" name="hidden_pdfData" id="hidden_pdfData"/>
                    </form>
                    <form id="formCSV" name="formCSV" action="CSVServlet" method="POST">
                        <input type="hidden" name="hidden_CSVData" id="hidden_CSVData"/>
                    </form>

                    <%
                        if (request.getAttribute("zusatz_informationen") != null)
                        {
                    %>
                    <div id="div_zusatzDaten">
                        <%=request.getAttribute("zusatz_informationen")%>
                    </div>
                    <%
                        }
                    %>
                    <div id="div_table">
                    </div>
                    <div id="div_csv_pdf" style="display:none" class="ui segment">
                        <div class="ui equal width grid">
                            <div class="column">
                                <button type="button" class="ui button styleRot" onClick="saveDataForPDF()" title="Generiere PDF" style="width: 100%;">PDF</button>
                            </div>
                            <div class="column">
                                <button type="button" class="ui button styleGruen" onClick="saveDataForCSV()" title="Generiere CSV" style="width: 100%;">CSV</button>
                            </div>
                        </div>
                    </div>  
                </div>
            </div>
        </div>
        <br/>

        <script src="js/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script src="js/jquery-ui.js"></script> 
        <!-- <script src="js/tablesort.js"></script>-->
        <script src="js/vordefiniert.js"></script>
        <script src="js/datepicker-de.js"></script>
        <script src="tablesorter/jquery.tablesorter.js"></script> 
        <!--  <script src="tablesorter/jquery.tablesorter.min.js"></script>  -->
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
                    String strHTML = "";
                    int i = 0;
                    while (i < liBerichtDaten.size())
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
                    LinkedList<Object> liFahrzeug = (LinkedList<Object>) request.getAttribute("zusatz_liste");
                    String strZusatzHTML = "";
                    i = 0;
                    while (i < liFahrzeug.size())
                    {
                        if (i % 3 == 0)
                        {
                            strZusatzHTML += "<tr>";
                        }
                        Object zeile = liFahrzeug.get(i);
                        strZusatzHTML += zeile.toString();
                        i++;
                        if (i % 3 == 0)
                        {
                            strZusatzHTML += "</tr>";
                        }
                    }
                    if (i % 3 != 0)
                    {
                        strZusatzHTML += "</tr>";
                    }

            %>

                                        document.getElementById("hidden_pdfData").value = "<%=request.getParameter("input_aktbericht")%>###<%=strHTML%>###<%=strZusatzHTML%>";
                                                document.formPDF.submit();
            <%
            } else
            {
            %>
                                                document.getElementById("div_csv_pdf").style.display = "block";
                                                document.getElementById("div_table").getElementsByTagName("tbody")[0].innerHTML = "<%=strHTML%>";
                                                //     $('.sortable.table').tablesort();


                                                $('table').addClass('tablesorter');

                                                <% if (request.getParameter("input_aktbericht") != null 
                                                        && (request.getParameter("input_aktbericht").equals("Geburtstagsliste") 
                                                        || request.getParameter("input_aktbericht").equals("Dienstzeitliste")))
                                                { %>
                                                    $('table').tablesorter({
                                                        headers: {5: {sorter: 'germandate'}}
                                                    });
                                                <%} else
                                                {%>
                                                    $('table').tablesorter();
                                                <%}%>


                                                $('.sort').popup();



            <%
                    }
                }
            %>
                                                document.getElementById("div_loader").className = "ui disabled loader";
                                                fixDropdowns("select_bezirk");
                                                fixDropdowns("select_abschnitt");
                                                fixDropdowns("select_feuerwehr");
                                            });



        </script>        

    </body>
</html>

<%!
    private String generiereHiddenAbschnittDiv(HttpSession session)
    {
        if (intIDGruppe == 15)
        {
            Abschnitt abschnitt = (Abschnitt) session.getAttribute("abschnitt");
            return abschnitt.generiereHiddenDiv();
        } else
        {
            String strAbschnittDivs = "";
            Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
            LinkedList<Abschnitt> liAbschnitte = bezirk.getLiAbschnitte();
            for (Abschnitt abschnitt : liAbschnitte)
            {
                strAbschnittDivs += abschnitt.generiereHiddenDiv();
            }
            return strAbschnittDivs;
        }
    }

    private String generiereHiddenBezirkDiv(HttpSession session)
    {
        Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
        return bezirk.generiereHiddenDiv();
    }

    private String generiereBezirk(HttpSession session)
    {
        if (session.getAttribute("bezirk") != null)
        {
            intIDGruppe = 5;
            Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
            return bezirk.toString();
        } else
        {
            String strName = (String) session.getAttribute("bezirkName");
            return "<option value='-1'>" + strName + "</option>";
        }
    }

    private String generiereAbschnitt(HttpSession session)
    {
        if (session.getAttribute("abschnitt") != null)
        {
            intIDGruppe = 15;
            Abschnitt abschnitt = (Abschnitt) session.getAttribute("abschnitt");
            return abschnitt.toString();
        } else if (intIDGruppe != -1)
        {
            return "";
        } else
        {
            String strName = (String) session.getAttribute("abschnittName");
            return "<option value='-1'>" + strName + "</option>";
        }
    }

    private String generiereFeuerwehr(HttpSession session)
    {

        if (session.getAttribute("feuerwehr") != null)
        {
            System.out.println("vordefiniert.generiereFeuerwehr: if");
            intIDGruppe = 9;
            Feuerwehr feuerwehr = (Feuerwehr) session.getAttribute("feuerwehr");
            return feuerwehr.toString();
        } else
        {
            System.out.println("vordefiniert.generiereFeuerwehr: if");
            return "";
        }
    }

%>
