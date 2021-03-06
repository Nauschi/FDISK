
<%@page import="Beans.Bezirk"%>
<%@page import="Beans.Abschnitt"%>
<%@page import="Beans.Feuerwehr"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%-- 
    Document   : dynamisch_mitarbeiter
    Created on : 18.08.2015, 17:15:59
    Author     : Marcel Schmidt
--%>
<%@page import="java.util.LinkedList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="semantic/dist/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="css/standardDesign.css">
        <link rel="stylesheet" type="text/css" href="css/dynamisch.css">
        <link rel="stylesheet" type="text/css" href="css/jquery-ui_1.css">
        <link rel="stylesheet" type="text/css" href="css/jquery-ui_2.css">
        <title>Dynamisch - Mitglieder</title>
    </head>
    <body>
        <%!
            private String strAktBoxArt = "";
            private int intIDGruppe;
        %>
        <%
            if (session == null || session.getAttribute("loggedIn") == null) {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            response.setCharacterEncoding("UTF-8");
            request.setCharacterEncoding("UTF-8");
            intIDGruppe = -1;
            String[] strFeldKlammerAuf
                    = {
                        "N/A", "("
                    };
            String[] strFeldKlammerZu
                    = {
                        "N/A", ")"
                    };

            String[] strFeldOperator
                    = {
                        "N/A", "=", "<>", "<=", ">=", "<", ">"
                    };

            String[] strFeldFilter
                    = {
                        "Technischer Lehrgang 1", "Technischer Lehrgang 2"
                    };

            String[] strFeldVerknuepfung
                    = {
                        "N/A", "UND", "UND NICHT", "ODER", "ODER NICHT"
                    };
        %>


        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="res/logo_oben.png">
                </br>
            </div>
            <div class="ui menu" id="div_menu">
                <form action="MainServlet" method="POST" name="form_vordefiniert">
                    <input type="hidden" name="vordefiniert">
                    <input type="hidden" name="hidden_berechtigungs_info" id="hidden_berechtigungs_info_2">

                    <a href="#" onclick="zuVordefiniertWeiterleiten()" class="item linkMenu">
                        Vordefiniert
                    </a>
                </form>
                <a class="item active linkMenu">
                    Dynamisch
                </a>
                <div class="right menu">
                    <input type="hidden" name="instruction">
                    <a href="http://www.lfv.steiermark.at/Portaldata/1/Resources/dokumente/6_innere_angelegenheiten/4_edv/manuals/Anleitung_dynamischer_Listengenerator.pdf" target="_blank" class="ui item linkMenu">
                        Anleitung&nbsp;&nbsp;<i class="file pdf outline icon"></i>
                    </a>
                    <form action="MainServlet" method="POST" name="form_logout">
                        <input type="hidden" name="logout">
                        <a href="#" onclick="document.form_logout.submit();" class="ui item linkMenu">
                            Logout&nbsp;&nbsp;<i class="sign out icon"></i>
                        </a>
                    </form>
                </div>
            </div>
        </div>
        <%
            int intZaehler = 1;
            if (request.getAttribute("hidden_zaehler") != null) {
                intZaehler = Integer.parseInt(request.getAttribute("hidden_zaehler").toString());
            } else if (request.getParameter("hidden_action") != null) {

                String strAction = request.getParameter("hidden_action");
                if (strAction.equals("plus")) {
                    intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler")) + 1;
                } else if (strAction.equals("minus")) {
                    intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler")) - 1;
                    if (intZaehler < 1) {
                        intZaehler = 1;
                    }
                } else if (strAction.equals("vorschau") || strAction.equals("erstelle_vorlage") || strAction.equals("erstelle_vorlage_2")) {
                    intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler"));
                }

            }
        %>

        <h1>Dynamisch - Mitglieder</h1>
        <br/>
        <div style="background-color: #C00518;color: white; margin: 0 auto; width: 90%;" class="ui segment">
            <b>Basis Optionen</b>
        </div>
        <div id="div_vorlage" class="ui equal width grid">
            <div class="column" id="div_bezirk">
                <fieldset id="fieldset_bezirk">
                    <legend><b>Bereich</b></legend>
                    <select  name="select_bezirk"  class="ui fluid dropdown" id="select_bezirk" onchange="bezirkChanged(this)">
                        <%=generiereBezirk(session, request)%>
                    </select>
                </fieldset>
            </div>
            <%
                if (intIDGruppe == 5 || intIDGruppe == 1) {
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
                if (intIDGruppe == 15 || intIDGruppe == 5 || intIDGruppe == 1) {
                    out.println(generiereHiddenAbschnittDiv(session));
                }
            %>
            <div class="column" id="div_feuerwehr">
                <fieldset id="fieldset_feuerwehr">
                    <legend><b>Feuerwehr</b></legend>
                    <select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr">
                        <%=generiereFeuerwehr(session)%>
                    </select>
                </fieldset>
            </div>

            <div class="column">
                <fieldset id="fieldset_vorlage">

                    <legend><b>Vorlage</b></legend>
                    <div class="ui equal width grid">
                        <div class="column">
                            <button type="button" onclick="showLoeschenModal();" class="ui button styleRot" title="Vorlage löschen" style="width: 100%;"><p><i class="remove circle icon"></i></p></button>
                        </div>                        
                        <div class="column">
                            <button type="button" onclick="showLadenModal();" class="ui button styleGrau" title="Vorlage laden" style="width: 100%"><p><i class="folder open outline icon"></i></p></button>
                        </div>
                        <div class="column">
                            <button type="button" onclick="showErstellenModal();" class="ui button styleGruen" title="Vorlage speichern" style="width: 100%;"><p><i class="save icon"></i></p></button>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div>

        <div id="dyn_main_div" >
            <div style="background-color: #C00518;color: white;" class="ui segment">
                <b>Abfragen</b> <i class="large info circle link icon" style="float: right" title="Info" onclick="$('#modal_abfragen_info').modal('show');"></i>
            </div>
            <div class="ui grid" id="div_dyn_headers">
                <div class="two wide column" style="width: 100%;">
                    <b>Klammer auf</b>
                </div>
                <div class="three wide column" style="width: 100%;">
                    <b>Typ</b>
                </div>
                <div class="two wide column" style="width: 100%;">
                    <b>Operator</b>
                </div>
                <div class="four wide column" style="width: 100%;">
                    <b>Filter</b>
                </div>
                <div class="two wide column" style="width: 100%;">
                    <b>Klammer zu</b>
                </div>
                <div class="three wide column" style="width: 100%;">
                    <b>Verknüpfung</b>
                </div>
            </div>
            <form action="MainServlet" method="POST" name="form_plus_minus_vorschau">
                <input type="hidden" name="hidden_zaehler" value="<%=intZaehler%>">
                <input type="hidden" name="hidden_vorlage_name" id="hidden_vorlage_name">
                <input type="hidden" name="hidden_berechtigungs_info" id="hidden_berechtigungs_info">
                <input type="hidden" name="hidden_typen_daten" id="hidden_typen_daten">
                <%
                    for (int i = 1; i <= intZaehler; i++) {
                %>

                <div class="ui grid dyn_element" id="div_element_<%=i%>">

                    <div class="two wide column" id="div_klammerAuf_<%=i%>" style="width: 100%;">
                        <select class="ui fluid dropdown" id="select_klammer_<%=i%>">
                            <!--<option value=""></option>-->
                            <%=generiereSelect(0, strFeldKlammerAuf, request, i)%>
                        </select>
                    </div>

                    <div class="three wide column" id="div_typ_<%=i%>" style="width: 100%;">
                        <select class="ui fluid dropdown" onchange="onTypChanged(this, null, null)" id="select_typ_<%=i%>">
                            <%
                                String strAktTypMitBoxArt = "";
                                if (request.getAttribute("hidden_element_data_" + i) != null) {
                                    String strTyp = ((String) request.getAttribute("hidden_element_data_" + i)).split(";")[1];
                                    String strBoxArt = ((String) request.getAttribute("hidden_element_data_" + i)).split(";")[2];
                                    strAktTypMitBoxArt = strTyp + ";" + strBoxArt;
                                } else if (request.getParameter("hidden_element_data_" + i) != null) {
                                    String strTyp = request.getParameter("hidden_element_data_" + i).split(";")[1];
                                    String strBoxArt = request.getParameter("hidden_element_data_" + i).split(";")[2];
                                    strAktTypMitBoxArt = strTyp + ";" + strBoxArt;
                                }
                            %>

                            <!--<option value="">Typ</option>-->
                            <%
                                LinkedList<String> liTypen = (LinkedList<String>) application.getAttribute("Typen");
                                for (String strTypMitBoxArt : liTypen) {
                                    String[] strSplitTyp = strTypMitBoxArt.split(";");
                                    String strTyp = strSplitTyp[0];
                                    String strBoxArt = strSplitTyp[1];
                                    if (strAktTypMitBoxArt.equals(strTypMitBoxArt)) {
                                        strAktBoxArt = strBoxArt;
                            %>
                            <option value="<%=strTypMitBoxArt%>" selected><%=strTyp%></option>
                            <%
                            } else {
                            %>
                            <option value="<%=strTypMitBoxArt%>"><%=strTyp%></option>
                            <%
                                    }
                                }
                                if (strAktBoxArt.isEmpty()) {
                                    strAktBoxArt = liTypen.get(0).split(";")[1];
                                }
                            %>
                        </select>

                    </div>
                    <div class="two wide column" id="div_operator_<%=i%>" style="width: 100%;">
                        <select class="ui fluid dropdown" id="select_operator_<%=i%>">
                            <!--<option value="">Operator</option>-->
                            <%--<%=generiereSelect("select_operator_" + i, strFeldOperator, request)%>--%>
                        </select>
                    </div>

                    <div class="four wide column" id="div_filter_cb_<%=i%>" style="width: 100%; display: none;">
                       <!-- <select class="ui fluid dropdown" id="select_filter_cb_<%=i%>" onChange="onFilterChanged(this, document.getElementById('select_typ_'<%=i%>))">
                            <!--<option value="">Filter</option>-->
                            <%--<%=generiereSelect("select_filter_" + i, strFeldFilter, request)%>--%>
                        <!--</select>-->
                    </div>
                    <div class="four wide column" id="div_filter_txt_<%=i%>" style="width: 100%; display: none;">
                        <div class="ui input" style="width: 100%">
                            <input id="input_filter_<%=i%>" placeholder="Filter" autocomplete="off" type="text">
                        </div>
                    </div>
                    <div class="four wide column" id="div_filter_datepicker_<%=i%>" style="width: 100%; display: none;">
                        <div class="ui input" style="width: 100%">
                            <input id="input_filter_datepicker_<%=i%>" placeholder="Filter" autocomplete="off" readonly="true" type="text">
                        </div>
                    </div>

                    <div class="two wide column" id="div_klammerZu" style="width: 100%;">
                        <select class="ui fluid dropdown" id="select_klammer_zu_<%=i%>">
                            <!--<option value=""></option>-->
                            <%=generiereSelect(5, strFeldKlammerZu, request, i)%>
                        </select>
                    </div>
                    <div class="three wide column" id="div_verknuepfung_<%=i%>" style="width: 100%;">
                        <select class="ui fluid dropdown" onchange="onChangeVerknuepfung(<%=i%>)" id="select_verknuepfung_<%=i%>">
                            <!--<option value="">Verknüpfung</option>-->
                            <%=generiereSelect(6, strFeldVerknuepfung, request, i)%>
                        </select>
                    </div>
                </div>
                <%

                    }
                %>


                <div style="width: 40%; margin: 0 auto" class="ui equal width grid">
                    <div class="column">
                        <button name="button_minus" type="button" onclick="onActionSubmit(<%=intZaehler%>, 'minus')" class="ui button styleRot" title="Letzte Zeile entfernen" style="float: right; width: 50%;"><p><i class="remove icon"></i></p></button>
                    </div>

                    <div class="column">
                        <button name="button_plus" type="button" onclick="onActionSubmit(<%=intZaehler%>, 'plus')" class="ui button styleGruen" title="Neue Zeile einfügen" style="float: left; width: 50%;"><p><i class="plus icon"></i></p></button>
                    </div>
                </div>

            </form>
            <div style="background-color:#C00518;color:white;" class="ui segment">
                <b>Ausgabe</b> <i class="large info circle link icon" style="float: right" title="Info" onclick="$('#modal_ausgabe_info').modal('show');"></i>

            </div>
            <div id="div_typen_auswahl"  class="ui grid">
                <div id="div_typen_panel" class="four wide column">
                    <button name="button_minus_typ" type="button" class="ui button styleRot" title="Lösche letzten Spalte" onclick="deleteTypenAuswahl()" style="width: 45%;"><p><i class="delete icon"></i></p></button>
                    <button name="button_plus_typ" type="button" class="ui button styleGruen" title="Neue Spalte einfügen" onclick="addTypenAuswahl(null)" style="width: 45%;float: right"><p><i class="plus icon"></i></p></button>
                </div>
            </div>
            <br/>
            <div style="width: 30%; margin:0 auto">
                <div class="column" >
                    <button name="button_vorschau" type="button" onclick="onActionSubmit(<%=intZaehler%>, 'vorschau')" class="ui button styleGrau" title="Vorschau erstellen" style="width: 100%;"><p>Vorschau</p></button>
                </div>
            </div>

            <%
                if (request.getAttribute("dyn_table") != null) {
                    StringBuilder sbDynHTML = (StringBuilder) request.getAttribute("dyn_table");
            %>
            <div style="background-color:#C00518;color:white;" class="ui segment">
                <b>Bericht</b>
            </div>
            <%
                out.println("<div id='div_table'>");
                out.println(sbDynHTML);
                out.println("</div>");
            %>

            </br>
            <form id="formPDF" name="formPDF" action="PDFServlet" method="POST" target="_blank">
                <input type="hidden" name="hidden_pdfData" id="hidden_pdfData"/>
            </form>
            <form id="formCSV" name="formCSV" action="CSVServlet" method="POST">
                <input type="hidden" name="hidden_CSVData" id="hidden_CSVData"/>
            </form>

            <div>
                <div id="div_csv_pdf" style="display:none" class="ui segment">
                    <div class="ui equal width grid">
                        <div class="column">
                            <button type="button" class="ui button styleRot" onClick="$('#modal_setze_name').modal('show');" title="Generiere PDF" style="width: 100%;">PDF</button>
                        </div>
                        <div class="column">
                            <button type="button" class="ui button styleGruen" onClick="saveDataForCSV()" title="Generiere CSV"  style="width: 100%;">CSV</button>
                        </div>
                    </div>
                </div> 
            </div>

            <div class="ui small modal" id="modal_setze_name">
                <div class="header">Wählen sie bitte einen Namen für Ihren Bericht</div>
                <div class="content">
                    <div class="ui input" style="width: 100%">
                        <input placeholder="Berichtname" type="text" id="input_stetze_name">
                    </div>
                </div>
                <div class="actions">
                    <button type="button" onClick="saveDataForPDF()" class="ui button styleGruen"  style="width: 20%;">Bestätigen</button>
                    <button type="button" onClick="$('#modal_setze_name').modal('hide');" class="ui button styleRot"  style="width: 20%;">Abbrechen</button>
                </div>
            </div>

            <%
                } else {
                    out.println("<br/>");
                }

            %>
        </div>
        </br></br>
        <div class="ui small modal" id="modal_ausgabe_info">
            <div class="header">Info: Ausgabe</div>
            <div class="content">
                <p>In diesem Bereich können Sie auswählen, welche Spalten Sie später in Ihrem Bericht angezeigt haben wollen.</p>
                Automatisch ausgegeben werden:
                <ul>
                    <li>Standesbuchnummer</li>
                    <li>Dienstgrad</li>
                    <li>Vorname</li>
                    <li>Zuname</li>
                </ul>
                <p>Es können maximal 6 zusätzliche Spalten ausgewählt werden.</p>
            </div>
            <div class="actions">
                <button type="button" onClick="$('#modal_ausgabe_info').modal('hide');" class="ui button styleGrau"  style="width: 20%;">OK</button>
            </div>
        </div>

        <div class="ui small modal" id="modal_abfragen_info">
            <div class="header">Info: Abfragen</div>
            <div class="content">
                <p>In diesem Bereich können Sie Bedingungen zum Filtern der Daten angeben (Maximal 5 möglich)</p>

                <ul>
                    <li>Mit Klick auf das "+" Symbol können neue Bedingungen hinzugefügt werden</li>
                    <li>Mit Klick auf das "-" Symbol wird immer die unterste Bedingung gelöscht</li></ul>

            </div>
            <div class="actions">
                <button type="button" onClick="$('#modal_abfragen_info').modal('hide');" class="ui button styleGrau"  style="width: 20%;">OK</button>
            </div>
        </div>

        <div class="ui small modal" id="modal_erstelle_vorlage">
            <div class="header">Neue Vorlage</div>
            <div class="content">
                <p>Wählen Sie bitte einen Namen für die neue Vorlage aus:</p>
                <div class="ui input" style="width: 100%">
                    <%                        if (request.getParameter("hidden_vorlage_name") != null && request.getAttribute("dynamisch_vorlage_vorhanden") != null) {
                    %>
                    <input placeholder="Name" type="text" name="input_neueVorlage" id="input_neueVorlage" value="<%=request.getParameter("hidden_vorlage_name")%>">
                    <%
                    } else {
                    %>
                    <input placeholder="Name" type="text" name="input_neueVorlage" id="input_neueVorlage">
                    <%
                        }
                    %>
                </div>
            </div>
            <div class="actions">
                <button type="button" onClick="onActionSubmit(<%=intZaehler%>, 'erstelle_vorlage')" name="button_bestätigen_erstelleVorlage" class="ui button styleGruen"  style="width: 20%;">Bestätigen</button>
                <button type="button" onClick="$('#modal_erstelle_vorlage').modal('hide');" name="button_abbrechen_erstelleVorlage" class="ui button styleRot"  style="width: 20%;">Abbrechen</button>
            </div>
        </div>


        <%
            if (request.getAttribute("dynamisch_vorlage_vorhanden") != null) {
        %>
        <div class="ui small modal" id="modal_vorhanden">
            <div class="header">Name bereits vorhanden</div>
            <div class="content">
                <p>Der von Ihnen gewählte Name wird bereits bei einer Vorlage verwendet, wollen Sie diese ersetzen?</p>
            </div>
            <div class="actions">
                <button type="button" onClick="onActionSubmit(<%=intZaehler%>, 'erstelle_vorlage_2')"  class="ui button styleGruen"  style="width: 10%;">Ja</button>
                <button type="button" onClick="$('#modal_vorhanden').modal('hide');" class="ui button styleRot"  style="width: 10%;">Nein</button>
            </div>
        </div>
        <%
            }
        %>

        <div class="ui small modal" id="modal_fehler">
            <div class="header">Fehler</div>
            <div class="content">
                <p></p>
            </div>
            <div class="actions">
                <button type="button" onClick="$('#modal_fehler').modal('hide');" class="ui button styleGrau">OK</button>
            </div>
        </div>

        <%
            HashMap<String, LinkedList<String>> liVorlagen = (HashMap<String, LinkedList<String>>) application.getAttribute("userid_" + session.getAttribute("intUserID") + "_vorlagen");
            if (liVorlagen != null && liVorlagen.size() > 0) {
        %>
        <div class="ui small modal" id="modal_lade_vorlage">
            <div class="header">Lade Vorlage</div>
            <div class="content">
                <p>Wählen Sie bitte eine Vorlage aus:</p>
                <form action="MainServlet" method="POST" name="form_ladeVorlage">
                    <select name="select_lade_vorlage" class="ui fluid dropdown" id="select_vorlage">
                        <%=leseVorhandeneVorlagen(application, session)%>
                    </select>
                </form>
            </div>
            <div class="actions">
                <button type="button" onClick="document.form_ladeVorlage.submit();" name="button_bestätigen_ladeVorlage" class="ui button styleGruen"  style="width: 20%;">Bestätigen</button>
                <button type="button" onClick="$('#modal_lade_vorlage').modal('hide');" name="button_abbrechen_ladeVorlage" class="ui button styleRot"  style="width: 20%;">Abbrechen</button>
            </div>
        </div>

        <div class="ui small modal" id="modal_loesche_vorlage">
            <div class="header">Lösche Vorlage</div>
            <div class="content">
                <p>Welche Vorlage möchten Sie löschen?</p>
                <form action="MainServlet" method="POST" name="form_loescheVorlage">
                    <select name="select_loesche_vorlage" class="ui fluid dropdown" id="select_vorlage">
                        <%=leseVorhandeneVorlagen(application, session)%>
                    </select>
                </form>
            </div>
            <div class="actions">
                <button type="button" onClick="document.form_loescheVorlage.submit();"  class="ui button styleGruen"  style="width: 20%;">Bestätigen</button>
                <button type="button" onClick="$('#modal_loesche_vorlage').modal('hide');" class="ui button styleRot"  style="width: 20%;">Abbrechen</button>
            </div>
        </div>
        <%
            }
        %>

        <script src="js/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script src="js/jquery-ui.js"></script> 
        <!--  <script src="js/tablesort.js"></script>-->


        <script src="js/dynamisch_mitglieder.js"></script>
        <script src="js/datepicker-de.js"></script>
        <script src="tablesorter/jquery.tablesorter.js"></script> 
        <script>
                    $(function () {
            <%                for (int i = 1; i <= intZaehler; i++) {
            %>

                        $("#input_filter_datepicker_<%=i%>").datepicker({
                            onSelect: function (selected)
                            {
                                $("#input_filter_datepicker_<%=i%>").datepicker("option", "showAnim", "slideDown");
                                $("#input_filter_datepicker_<%=i%>").datepicker("option", "dateFormat", "dd.mm.yy");
                                $("#input_filter_datepicker_<%=i%>").datepicker("option", "changeMonth", "true");
                                $("#input_filter_datepicker_<%=i%>").datepicker("option", "changeYear", "true");
                                $("#input_filter_datepicker_<%=i%>").datepicker("option", $.datepicker.regional['de']);
                            }
                        }).datepicker("option", {
                            changeMonth: 'true',
                            changeYear: 'true',
                            yearRange: '1960:2017',
                            showAnim: 'slideDown'});


            <%
                }
            %>
                        var liTypen = [
            <%
                LinkedList<String> liTypen = (LinkedList<String>) application.getAttribute("TypenAusgabe");
                for (String strTyp : liTypen) {
                    out.print('"' + strTyp.split(";")[0] + '"');
                    if (!strTyp.equals(liTypen.getLast())) {
                        out.println(",");
                    }
                }
            %>
                        ];
                        setTypen(liTypen);
            <%
                if (request.getAttribute("hidden_typen_daten") != null || request.getParameter("hidden_typen_daten") != null) {
                    String[] strTypen;
                    if (request.getAttribute("hidden_typen_daten") != null) {
                        strTypen = ((String) request.getAttribute("hidden_typen_daten")).split(";");
                    } else {
                        strTypen = request.getParameter("hidden_typen_daten").split(";");
                    }

                    for (int i = 0; i < strTypen.length; i++) {
                        if (!strTypen[i].isEmpty()) {
                            out.println("addTypenAuswahl('" + strTypen[i] + "');");
                        }
                    }
                }
                HashMap<String, LinkedList<String>> hsFilter = (HashMap<String, LinkedList<String>>) session.getAttribute("hashMap_typ");
                Set<String> set = hsFilter.keySet();
                Iterator it = set.iterator();
            %>

                        var mapAlt = {
            <%
                while (it.hasNext()) {
                    String strKey = (String) it.next();
                    LinkedList<String> liValues = hsFilter.get(strKey);
                    out.print("'" + strKey + "' : '");
                    for (String value : liValues) {
                        value = value.replace("''", "\"");
                        value = value.replace("&quot", "\"");
                        value = value.replace(";", "");
                        if (liValues.getLast().equals(value)) {
                            out.print(value);
                        } else {
                            out.print(value + ";");
                        }

                    }
                    if (it.hasNext()) {
                        out.println("',");
                    } else {
                        out.print("'");
                    }
                }

            %>
                        };
                        setMap(mapAlt);
            <%                LinkedList<String> liOperatoren = (LinkedList<String>) application.getAttribute("liOperatoren");
            %>

                        var mapOperatoren = {
            <%
                for (String strLine : liOperatoren) {
                    String[] daten = strLine.split(";");
                    out.print("'" + daten[0].toUpperCase() + "' : '");
                    for (int i = 1; i < daten.length; i++) {
                        if (i + 1 == daten.length) {
                            out.print(daten[i]);
                        } else {
                            out.print(daten[i] + ";");
                        }

                    }
                    if (strLine.equals(liOperatoren.getLast())) {
                        out.print("'");
                    } else {
                        out.println("',");
                    }
                }

            %>
                        };
                        setMapOperatoren(mapOperatoren);
            <%                for (int i = 1; i <= intZaehler; i++) {
            %>
                        var strLastFilter = null;
                        var strLastOperator = null;
            <%
                if (request.getAttribute("hidden_element_data_" + i) != null) {
                    out.println("strLastFilter= '" + ((String) request.getAttribute("hidden_element_data_" + i)).split(";")[4] + "';");
                    out.println("strLastOperator= '" + ((String) request.getAttribute("hidden_element_data_" + i)).split(";")[3] + "';");
                } else if (request.getParameter("hidden_element_data_" + i) != null) {
                    out.println("strLastFilter= '" + request.getParameter("hidden_element_data_" + i).split(";")[4] + "';");
                    out.println("strLastOperator= '" + request.getParameter("hidden_element_data_" + i).split(";")[3] + "';");
                }

            %>
                        onTypChanged(document.getElementById("select_typ_<%=i%>"), strLastFilter, strLastOperator);
            <%   }
                if (request.getAttribute("dyn_table") != null && request.getAttribute("dyn_table").toString().split("<tr>").length > 2) {
                    String strDynHTML = request.getAttribute("dyn_table").toString();
            %>
            <%=initialisiereTableSorter(strDynHTML)%>
                        // $('.sortable.table').tablesort();
                        $('th').popup();
                        document.getElementById("div_csv_pdf").style.display = "block";
            <%
                }
                if (request.getAttribute("dynamisch_vorlage_vorhanden") != null) {
            %>
                        $('#modal_vorhanden').modal('show');
            <%
                }
                if (request.getParameter("hidden_berechtigungs_info") != null) {
            %>
                        bezirkChanged(document.getElementById("select_bezirk"),<%=request.getParameter("hidden_berechtigungs_info").split(";")[1]%>);
                        abschittChanged(document.getElementById("select_abschnitt"),<%=request.getParameter("hidden_berechtigungs_info").split(";")[2]%>);
            <%
                }
            %>
                        fixDropdowns("select_bezirk");
                        fixDropdowns("select_abschnitt");
                        fixDropdowns("select_feuerwehr");
                    });
        </script>
    </body>
</html>


<%!
    /**
     * Setzt den Tablesorter abhängig von den ausgewählten Daten
     */
    private String initialisiereTableSorter(String strTable) {
        String output = "$('.sortable').tablesorter({headers: {1: {sorter: 'levels'}";
        int index1 = strTable.indexOf("<th ");
        int index2 = strTable.lastIndexOf("</th>");
        strTable = strTable.substring(index1, index2);
        strTable = strTable.replaceAll("\\<th[^>]*>", "");
        String[] strSplit = strTable.split("</th>");

        for (int i = 4; i < strSplit.length; i++) {
            if (strSplit[i].toUpperCase().contains("DATUM")) {
                output += "," + i + ": {sorter: 'berichtdate'}";
            }
        }
        output += "}});";
        return output;
    }

    /**
     * Erzeugt jeweils eine Option für jede vorhandene Vorlage
     */
    private String leseVorhandeneVorlagen(ServletContext application, HttpSession session) {
        String strHTML = "";
        HashMap<String, LinkedList<String>> hsVorlagen = (HashMap<String, LinkedList<String>>) application.getAttribute("userid_" + session.getAttribute("intUserID") + "_vorlagen");
        Set<String> setName = hsVorlagen.keySet();
        for (String strName : setName) {
            strHTML += "<option value='" + strName + "'>" + strName + "</option>";
        }
        return strHTML;
    }

    /**
     * Generiert den Inhalt eines Dropdowns und setzt, falls vorhanden, zuletzt
     * gewähltes Item auf "selected"
     */
    private String generiereSelect(int intIndex, String[] strFeld, HttpServletRequest request, int intAktI) {
        try {
            String strAusgabe = "";
            String strLetzteAuswahl = "";
            if (request.getAttribute("hidden_element_data_" + intAktI) != null) {
                strLetzteAuswahl = ((String) request.getAttribute("hidden_element_data_" + intAktI)).split(";")[intIndex];
            } else if (request.getParameter("hidden_element_data_" + intAktI) != null) {
                strLetzteAuswahl = request.getParameter("hidden_element_data_" + intAktI).split(";")[intIndex];
            }
            for (String strElement : strFeld) {
                if (strLetzteAuswahl.equals(strElement)) {
                    strAusgabe += "<option value='" + strElement + "' selected>" + strElement + "</option>";
                } else {
                    strAusgabe += "<option value='" + strElement + "'>" + strElement + "</option>";
                }
            }
            return strAusgabe;
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Erstellt einen HTML String der aus Divs besteht. Diese Divs enthalten
     * relevante Daten über die Feuerwehren
     */
    private String generiereHiddenAbschnittDiv(HttpSession session) {
        String strAusgabe = "";
        if (intIDGruppe == 15) {
            Abschnitt abschnitt = (Abschnitt) session.getAttribute("abschnitt");
            strAusgabe = abschnitt.generiereHiddenDiv();
        } else if (intIDGruppe == 5) {
            String strAbschnittDivs = "";
            Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
            LinkedList<Abschnitt> liAbschnitte = bezirk.getLiAbschnitte();
            for (Abschnitt abschnitt : liAbschnitte) {
                strAbschnittDivs += abschnitt.generiereHiddenDiv();
            }
            strAusgabe = strAbschnittDivs;
        } else if (intIDGruppe == 1) {
            String strAbschnittDivs = "";
            LinkedList<Bezirk> liBezirke = (LinkedList<Bezirk>) session.getAttribute("alleBezirke");
            for (Bezirk bezirk : liBezirke) {
                LinkedList<Abschnitt> liAbschnitte = bezirk.getLiAbschnitte();
                for (Abschnitt abschnitt : liAbschnitte) {
                    strAbschnittDivs += abschnitt.generiereHiddenDiv();
                }
            }
            strAusgabe = strAbschnittDivs;
        }

        return strAusgabe;
    }

    /**
     * Erstellt einen HTML String der aus Divs besteht. Diese Divs enthalten
     * relevante Daten über die Abschnitte
     */
    private String generiereHiddenBezirkDiv(HttpSession session) {
        String strAusgabe = "";
        if (intIDGruppe == 1) {
            LinkedList<Bezirk> liBezirke = (LinkedList<Bezirk>) session.getAttribute("alleBezirke");
            for (Bezirk bezirk : liBezirke) {
                strAusgabe += bezirk.generiereHiddenDiv();
                strAusgabe += bezirk.generiereHiddenDivFw();
            }
        } else if (intIDGruppe == 5) {
            Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
            strAusgabe += bezirk.generiereHiddenDiv();
            strAusgabe += bezirk.generiereHiddenDivFw();
        }

        return strAusgabe;
    }

    /**
     * Erzeugung der Optionen für das Bezirk Dropdown
     */
    private String generiereBezirk(HttpSession session, HttpServletRequest request) {
        int intLetzerBezirk = -1000;
        if (request.getParameter("hidden_berechtigungs_info") != null) {
            intLetzerBezirk = Integer.parseInt(request.getParameter("hidden_berechtigungs_info").split(";")[0]);
        }
        String strAusgabe = "";
        if (session.getAttribute("alleBezirke") != null) {
            intIDGruppe = 1;
            LinkedList<Bezirk> liBezirke = (LinkedList<Bezirk>) session.getAttribute("alleBezirke");
            strAusgabe += "<option value='-2'>Alle Bereiche</option>";
            for (Bezirk bezirk : liBezirke) {
                if (bezirk.getIntBezirksNummer() == intLetzerBezirk) {
                    strAusgabe += bezirk.toSelectedString();
                } else {
                    strAusgabe += bezirk.toString();
                }

            }
        } else if (session.getAttribute("bezirk") != null) {
            intIDGruppe = 5;
            Bezirk bezirk = (Bezirk) session.getAttribute("bezirk");
            strAusgabe = bezirk.toString();
        } else {
            String strName = (String) session.getAttribute("bezirkName");
            strAusgabe = "<option value='-1'>" + strName + "</option>";
        }
        return strAusgabe;
    }

    /**
     * Erzeugung der Optionen für das Abschnitt Dropdown
     */
    private String generiereAbschnitt(HttpSession session) {
        if (session.getAttribute("abschnitt") != null) {
            intIDGruppe = 15;
            Abschnitt abschnitt = (Abschnitt) session.getAttribute("abschnitt");
            return abschnitt.toString();
        } else if (intIDGruppe != -1) {
            return "";
        } else {
            String strName = (String) session.getAttribute("abschnittName");
            return "<option value='-1'>" + strName + "</option>";
        }
    }

    /**
     * Erzeugung der Optionen für das Feuerwehr Dropdown
     */
    private String generiereFeuerwehr(HttpSession session) {
        if (session.getAttribute("feuerwehr") != null) {
            intIDGruppe = 9;
            Feuerwehr feuerwehr = (Feuerwehr) session.getAttribute("feuerwehr");
            return feuerwehr.toString();
        } else {
            return "";
        }
    }
%>
