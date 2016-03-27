<%-- 
    Document   : vordefiniert
    Created on : 02.08.2015, 13:14:24
    Author     : Marcel Schmidt
--%>

<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
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
            if (session == null || session.getAttribute("loggedIn") == null)
            {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            intIDGruppe = -1;
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
                    <input type="hidden" name="hidden_berechtigungs_info" id="hidden_berechtigungs_info">
                    <a href="#" onclick="zuDynamischWeiterleiten()" class="item linkMenu">
                        Dynamisch
                    </a>
                </form>
                <div class="right menu">
                    <form action="MainServlet" method="POST" name="form_logout">
                        <input type="hidden" name="logout">
                        <a href="#" onclick="document.form_logout.submit();" class="ui item linkMenu">
                            Logout&nbsp;&nbsp;<i class="sign out icon"></i>
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

                            <div class="column" id="div_bezirk">
                                <fieldset id="fieldset_bezirk">
                                    <legend><b>Bezirk</b></legend>
                                    <select  name="select_bezirk"  class="ui fluid dropdown" id="select_bezirk" onchange="bezirkChanged(this)">
                                        <%=generiereBezirk(session,request)%>
                                    </select>
                                </fieldset>
                            </div>
                            <%
                                if (intIDGruppe == 5 || intIDGruppe == 1)
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
                                if (intIDGruppe == 15 || intIDGruppe == 5 || intIDGruppe == 1)
                                {
                                    out.println(generiereHiddenAbschnittDiv(session));
                                }
                            %>
                            <div class="column" id="div_feuerwehr">
                                <fieldset id="fieldset_feuerwehr">
                                    <legend><b>Feuerwehr</b></legend>
                                    <select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr" onchange="feuerwehrChanged(this)">
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

                                        <%
                                            String strBerichtAnfangText = "01.12." + (LocalDate.now().getYear() - 1);
                                        %>

                                        <input  name="input_von_datum" id="input_von_datum" placeholder="von..." autocomplete="off" readonly="true" type="text" 
                                                value='<%=(str_input_von_datum != null) ? str_input_von_datum : strBerichtAnfangText%>'>
                                        <div id="div_remove_von_datum" style="display:  <%=(str_input_von_datum == null || !str_input_von_datum.isEmpty()) ? "block" : "none"%>;">
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

                                        <%
                                            String strBerichtEndeText = "30.11." + LocalDate.now().getYear();
                                        %>

                                        <input name="input_bis_datum" id="input_bis_datum" placeholder="bis..." autocomplete="off" readonly="true" type="text" 
                                               value='<%=(str_input_bis_datum != null) ? str_input_bis_datum : strBerichtEndeText%>'>
                                        <div id="div_remove_bis_datum" style="display: <%=(str_input_bis_datum == null || !str_input_bis_datum.isEmpty()) ? "block" : "none"%>;">
                                            <button type="button" class="ui button styleRot" onclick="removeDateAndSetDivHidden('div_remove_bis_datum', 'input_bis_datum')" title="Lösche 'Datum bis'">X</button>
                                        </div>
                                    </div>
                                </fieldset>
                            </div >


                        </div>
                        <div class="ui grid">
                            <div class="eleven wide column" id="div_hidden_hilfe"></div>

                            <div class="eleven wide column" id="div_select_jahr" style="display: none">
                                <fieldset id="fieldset_jahr">
                                    <legend id="legend_jahr"><b>Jahr</b></legend>
                                    <select name="select_jahr" class="ui fluid dropdown" id="select_jahr">
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


                            <div class="eleven wide column" id="div_kennzeichen" style="display: none">
                                <fieldset>
                                    <legend><b>Kennzeichen</b></legend>
                                    <div class="ui search">
                                        <div class="ui input" style="width: 100%">
                                            <input id="input_kennzeichen" class="prompt" style="border-radius: .28571429rem;" name="input_kennzeichen" placeholder="Kennzeichen" autocomplete="off" type="text" 
                                                   <%=request.getParameter("input_kennzeichen") != null ? "value='" + request.getParameter("input_kennzeichen") + "'" : ""%>>
                                            <button type="submit" class="ui button styleGruen" name="button_ladeKennzeichen" title="Lade Kennzeichen">+</button>
                                        </div>
                                        <div class="results"></div>
                                    </div>

                                </fieldset>
                            </div>

                            <div class="eleven wide column" id="div_mitglied" style="display: none">
                                <fieldset>
                                    <legend><b>Mitglied</b> <i class="info circle link icon" title="Info" onclick="$('#modal_mitglieder_info').modal('show');"></i></legend>
                                    <div id="div_select_mitglieder" style="width: 90%;float:left">
                                        <select name="select_mitglied" class="ui fluid dropdown" id="select_mitglied">
                                            <option value="-2###Alle Mitglieder">Alle Mitglieder</option>
                                            <%
                                                if (request.getAttribute("select_mitglieder_hs") != null)
                                                {
                                                    LinkedHashMap<Integer, String> hsMitglieder = (LinkedHashMap<Integer, String>) request.getAttribute("select_mitglieder_hs");
                                                    Set<Integer> setKeys = hsMitglieder.keySet();
                                                    for (int key : setKeys)
                                                    {
                                            %>
                                            <option value="<%=key + "###" + hsMitglieder.get(key)%>"><%=hsMitglieder.get(key)%></option>
                                            <%
                                                }
                                            } else if (request.getParameter("select_mitglied") != null && !request.getParameter("select_mitglied").equals("-2###Alle Mitglieder"))
                                            {
                                            %>
                                            <option value="<%=request.getParameter("select_mitglied")%>" selected><%=request.getParameter("select_mitglied").split("###")[1]%></option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </div>
                                    <div id="div_mitglieder_submit" style="display: <%=(request.getAttribute("select_mitglieder_hs") != null) ? "none" : "block"%>;float:right;width: 10%">
                                        <button type="submit" style="float: left" class="ui button styleGruen" name="button_ladeMitglieder" title="Lade Mitglieder">+</button>
                                    </div>
                                </fieldset>
                            </div>

                            <div class="five wide column">
                                <fieldset>
                                    <legend>&nbsp;</legend>
                                    <button type="submit" name="button_vorschau" class="ui button styleGrau" onclick="document.getElementById('div_loader').className = 'ui active inverted dimmer';" title="Vorschau erstellen" style="width: 100%;">Vorschau</button>
                                </fieldset>
                            </div>
                        </div>
                    </form>

                    <div class="ui small modal" id="modal_fehler">
                        <div class="header">Fehler</div>
                        <div class="content">
                            <p></p>
                        </div>
                        <div class="actions">
                            <button type="button" onClick="$('#modal_fehler').modal('hide');" class="ui button styleGrau">OK</button>
                        </div>
                    </div>


                    <form id="formPDF" name="formPDF" action="PDFServlet" method="POST" target="_blank">
                        <input type="hidden" name="hidden_pdfData" id="hidden_pdfData"/>
                    </form>
                    <form id="formCSV" name="formCSV" action="CSVServlet" method="POST">
                        <input type="hidden" name="hidden_CSVData" id="hidden_CSVData"/>
                    </form>
                    <div id="div_kursstatistik" style="display: none;">
                        </br>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>Kursbezeichnung</th>
                                    <th>Anz Teiln</th>
                                </tr>
                            </thead>
                        </table>
                        </br>
                    </div>
                    <div id="div_fahrtenbuch" style="display: none;">
                        </br>
                        <fieldset>
                            <legend><b>Fahrzeugdaten</b></legend>
                            <table class="ui celled table">
                                <thead>
                                    <tr>
                                        <th>Art</th>
                                        <th>Baujahr</th>
                                        <th>Kursdatum</th>
                                        <th>Marke</th>
                                        <th>Leistung</th>
                                        <th>Treibstoff</th>
                                        <th>KM Gesamt</th>
                                    </tr>
                                </thead>
                            </table>
                        </fieldset>
                        </br>
                    </div>
                    <%                        if (request.getAttribute("zusatz_informationen") != null)
                        {
                    %>

                    <div id="div_zusatzDaten">
                        </br>
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

        <div class="ui small modal" id="modal_mitglieder_info">
            <div class="header">Info: Mitglieder</div>
            <div class="content">
                <p>Text Einfügen</p>
            </div>
            <div class="actions">
                <button type="button" onClick="$('#modal_mitglieder_info').modal('hide');" class="ui button styleGrau"  style="width: 20%;">OK</button>
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
                            $(document).ready(function ()
                    {
            <%
                if (request.getParameter("input_aktbericht") == null)
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
                        String temp = zeile.toString();

                        if (temp != null)
                        {
                            strHTML += temp;
                        }

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
                if (strHTML.split("<tr>").length > 1)
                {
            %>
                        document.getElementById("div_csv_pdf").style.display = "block";
                                document.getElementById("div_table").getElementsByTagName("tbody")[0].innerHTML = "<%=strHTML%>";
            <%=setzeTablesort(request)%>
                        $('.sort').popup();
            <%
                        }
                    }
                }
                if (request.getParameter("select_abschnitt") != null && (intIDGruppe == 5||intIDGruppe==1))
                {
            %>
                        bezirkChanged(document.getElementById("select_bezirk"),<%=request.getParameter("select_abschnitt")%>);
            <%
                }
                if (request.getParameter("select_feuerwehr") != null && (intIDGruppe == 15 || intIDGruppe == 5||intIDGruppe==1))
                {
            %>
                        abschittChanged(document.getElementById("select_abschnitt"),<%=request.getParameter("select_feuerwehr")%>);
            <%
                }
                if (request.getParameter("hidden_berechtigungs_info") != null)
                {
            %>
                        bezirkChanged(document.getElementById("select_bezirk"),<%=request.getParameter("hidden_berechtigungs_info").split(";")[1]%>);
                                abschittChanged(document.getElementById("select_abschnitt"),<%=request.getParameter("hidden_berechtigungs_info").split(";")[2]%>);
            <%
                }
                if (request.getParameter("input_aktbericht") != null && request.getParameter("input_aktbericht").equals("Digitales Fahrtenbuch"))
                {
            %>
                        if (document.getElementById("div_zusatzDaten") == null)
                        {
                        document.getElementById("div_fahrtenbuch").style.display = "block";
                        }
            <%
                }
            %>
                        document.getElementById("div_loader").className = "ui disabled loader";
                                fixDropdowns("select_bezirk");
                                fixDropdowns("select_abschnitt");
                                fixDropdowns("select_feuerwehr");
                                fixDropdowns("select_mitglied");
            <%
                if (request.getAttribute("select_kennzeichen_liste") != null)
                {
            %>
                        $('.ui.search').search({minCharacters : 0, searchFullText: false, source: [
            <%
                LinkedList<String> liKennzeichen = (LinkedList<String>) request.getAttribute("select_kennzeichen_liste");
                for (String kennzeichen : liKennzeichen)
                {
            %>
                        {title: '<%=kennzeichen%>'}<%=kennzeichen.equals(liKennzeichen.getLast()) ? "" : ","%>
            <%
                }
            %>
                        ], error : {noResults   : 'Keine Ergebnisse'}});
            <%
            } else if (request.getAttribute("select_mitglieder_hs") != null)
            {
            %>
                        setDeleteOnChange();
            <%
                }
            %>
                        });
        </script>        
    </body>
</html>

<%!
    private String generiereHiddenAbschnittDiv(HttpSession session)
    {
        String strAusgabe = "";
        if (intIDGruppe == 15)
        {
            Abschnitt abschnitt = (Abschnitt) session.getAttribute("abschnitt");
            strAusgabe = abschnitt.generiereHiddenDiv();
        } else if (intIDGruppe == 5)
        {
            String strAbschnittDivs = "";
            Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
            LinkedList<Abschnitt> liAbschnitte = bezirk.getLiAbschnitte();
            for (Abschnitt abschnitt : liAbschnitte)
            {
                strAbschnittDivs += abschnitt.generiereHiddenDiv();
            }
            strAusgabe = strAbschnittDivs;
        } else if (intIDGruppe == 1)
        {
            String strAbschnittDivs = "";
            LinkedList<Bezirk> liBezirke = (LinkedList<Bezirk>) session.getAttribute("alleBezirke");
            for (Bezirk bezirk : liBezirke)
            {
                LinkedList<Abschnitt> liAbschnitte = bezirk.getLiAbschnitte();
                for (Abschnitt abschnitt : liAbschnitte)
                {
                    strAbschnittDivs += abschnitt.generiereHiddenDiv();
                }
            }
            strAusgabe = strAbschnittDivs;
        }

        return strAusgabe;
    }

    private String generiereHiddenBezirkDiv(HttpSession session)
    {
        String strAusgabe = "";
        if (intIDGruppe == 1)
        {
            LinkedList<Bezirk> liBezirke = (LinkedList<Bezirk>) session.getAttribute("alleBezirke");
            for (Bezirk bezirk : liBezirke)
            {
                strAusgabe += bezirk.generiereHiddenDiv();
            }
        } else if (intIDGruppe == 5)
        {
            Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
            strAusgabe += bezirk.generiereHiddenDiv();
        }

        return strAusgabe;
    }

    private String generiereBezirk(HttpSession session,HttpServletRequest request)
    {
        int intLetzerBezirk=-1000;
        if(request.getParameter("select_bezirk")!=null)
        {
            intLetzerBezirk = Integer.parseInt(request.getParameter("select_bezirk"));
        }else if(request.getParameter("hidden_berechtigungs_info")!=null)
        {
            intLetzerBezirk = Integer.parseInt(request.getParameter("hidden_berechtigungs_info").split(";")[0]);
        }
        System.out.println("Bezirk: "+intLetzerBezirk);
        String strAusgabe = "";
        if (session.getAttribute("alleBezirke") != null)
        {
            intIDGruppe = 1;
            LinkedList<Bezirk> liBezirke = (LinkedList<Bezirk>) session.getAttribute("alleBezirke");
            for (Bezirk bezirk : liBezirke)
            {
                if(bezirk.getIntBezirksNummer()== intLetzerBezirk)
                {
                    strAusgabe+=bezirk.toSelectedString();
                }else
                {
                    strAusgabe += bezirk.toString();
                }
                
            }
        } else if (session.getAttribute("bezirk") != null)
        {
            intIDGruppe = 5;
            Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
            strAusgabe = bezirk.toString();
        } else
        {
            String strName = (String) session.getAttribute("bezirkName");
            strAusgabe = "<option value='-1'>" + strName + "</option>";
        }
        return strAusgabe;
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

    private String setzeTablesort(HttpServletRequest request)
    {
        if (request.getParameter("input_aktbericht") != null
                && (request.getParameter("input_aktbericht").equals("Geburtstagsliste")
                || request.getParameter("input_aktbericht").equals("Dienstzeitliste")
                || request.getParameter("input_aktbericht").equals("Einfache Mitgliederliste")
                || request.getParameter("input_aktbericht").equals("Erreichbarkeitsliste")
                || request.getParameter("input_aktbericht").equals("Adressliste")))
        {
            if (request.getParameter("input_aktbericht").equals("Geburtstagsliste")
                    || request.getParameter("input_aktbericht").equals("Dienstzeitliste"))
            {
                return "$('.tablesorter').tablesorter({headers: {1: {sorter: 'levels'},5: {sorter: 'germandate'}}});";
            } else
            {
                return "$('.tablesorter').tablesorter({headers: {1: {sorter: 'levels'}}});";
            }
        } else if (request.getParameter("input_aktbericht") != null && (request.getParameter("input_aktbericht").equals("Liste aller Tätigkeitsberichte")
                || request.getParameter("input_aktbericht").equals("Liste aller Übungsberichte")))
        {
            return "$('.tablesorter').tablesorter({headers: {3: {sorter: 'berichtdate'},4: {sorter: 'berichtdate'}}});";
        } else if (request.getParameter("input_aktbericht") != null
                && request.getParameter("input_aktbericht").equals("Liste aller Einsatzberichte"))
        {
            return "$('.tablesorter').tablesorter({headers: {2: {sorter: 'berichtdate'},3: {sorter: 'berichtdate'}}});";
        } else if (request.getParameter("input_aktbericht") != null
                && request.getParameter("input_aktbericht").equals("Liste aller Berichte"))
        {
            return "$('.tablesorter').tablesorter({headers: {0: {sorter: 'berichtdate'},1: {sorter: 'berichtdate'}}});";
        } else if (request.getParameter("input_aktbericht") != null
                && request.getParameter("input_aktbericht").equals("Kursstatistik"))
        {
//            return "$('.tablesorter2').tablesorter({headers: {3: {sorter: 'berichtdate'}}});"
            return "$('.tablesorter').tablesorter({headers: {4: {sorter: 'berichtdate'},5: {sorter: 'berichtdate'}}});";
        } else if (request.getParameter("input_aktbericht") != null
                && request.getParameter("input_aktbericht").equals("Digitales Fahrtenbuch"))
        {
            return "$('.tablesorter').tablesorter({headers: {1: {sorter: 'berichtdate'},2: {sorter: 'berichtdate'}}});";
        } else if (request.getParameter("input_aktbericht") != null
                && request.getParameter("input_aktbericht").equals("Einsatztaugliche Atemschutzgeräteträger"))
        {
            return "$('.tablesorter').tablesorter({headers: {1: {sorter: 'levels'},5: {sorter: 'germandate'},6: {sorter: 'berichtdate'},7: {sorter: 'berichtdate'}}});";
        } else if (request.getParameter("input_aktbericht") != null
                && request.getParameter("input_aktbericht").equals("Stundenauswertung je Mitglied je Instanz"))
        {
            return "$('.tablesorter').tablesorter({headers: {1: {sorter: 'levels'},6: {sorter: 'stundenauswertung'}}});";
        } else
        {
            return "$('.tablesorter').tablesorter();";
        }
    }
%>
