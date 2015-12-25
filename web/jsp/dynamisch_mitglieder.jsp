
<%-- 
    Document   : dynamisch_mitarbeiter
    Created on : 18.08.2015, 17:15:59
    Author     : user
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
        <link rel="stylesheet" href="css/jquery-ui_1.css">
        <link href="css/jquery-ui_2.css" rel="Stylesheet"type="text/css"/>
        <title>Dynamisch - Mitglieder</title>
    </head>
    <body>
        <%!
            private String strAktBoxArt = "";
        %>
        <%
            session.setAttribute("lastPage", "dynamisch_mitglieder");

            String[] strFeldKlammerAuf =
            {
                " ", "(", "[", "{"
            };
            String[] strFeldKlammerZu =
            {
                " ", ")", "]", "}"
            };

            String[] strFeldOperator =
            {
                "=", ">=", "<=", ">", "<", "<>"
            };

            String[] strFeldFilter =
            {
                "Technischer Lehrgang 1", "Technischer Lehrgang 2"
            };

            String[] strFeldVerknuepfung =
            {
                " ", "UND", "UND NICHT", "ODER", "ODER NICHT"
            };
        %>


        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="res/logo_oben.png">
                </br>
            </div>
            <div class="ui menu" style="background-color: #C00518; width: 100%">
                <form action="MainServlet" method="POST" name="form_vordefiniert">
                    <input type="hidden" name="vordefiniert">
                    <a href="#" onclick="document.form_vordefiniert.submit();" class="item linkMenu">
                        Vordefiniert
                    </a>
                </form>
                <a class="item active linkMenu">
                    Dynamisch
                </a>
                <div class="right menu">
                    <form action="MainServlet" method="POST" name="form_logout">
                        <input type="hidden" name="logout">
                        <a href="#" onclick="document.form_logout.submit();" class="ui item linkMenu">
                            Logout
                        </a>
                    </form>
                </div>
                <!--<div class="ui simple dropdown item">
                    Dynamisch
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <div class="item" onclick="location.href='jsp/dynamisch_mitarbeiter.jsp'">Mitglieder</div>
                        <div class="item" onclick="location.href='jsp/dynamisch_fahrzeuge_geraete.jsp'">Fahrzeuge und Geräte</div>
                    </div>
                </div>-->
            </div>
        </div>
        <%
            int intZaehler = 1;
            if (request.getParameter("button_plus") != null)
            {
                intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler")) + 1;
            } else if (request.getParameter("button_minus") != null)
            {
                intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler")) - 1;
                if (intZaehler < 1)
                {
                    intZaehler = 1;
                }
            }
        %>
        <h1>Dynamisch - Mitglieder</h1>
        <!--<div class="ui grid" id="div_mitte">-->

        <br/>
        <form action="MainServlet" method="POST">
            <input type="hidden" name="hidden_zaehler" value="<%=intZaehler%>">

            <%
                for (int i = 1; i <= intZaehler; i++)
                {
            %>

            <div class="ui grid" id="div_mitte">

                <div class="one wide column" id="div_klammerAuf" style="width: 100%;">
                    <select name="select_klammer_auf_<%=i%>" class="ui fluid dropdown" id="select_klammer">
                        <!--<option value=""></option>-->
                        <%=generiereSelect("select_klammer_auf_" + i, strFeldKlammerAuf, request)%>
                    </select>
                </div>

                <div class="four wide column" id="div_typ_<%=i%>" style="width: 100%;">
                    <select name="select_typ_<%=i%>" class="ui fluid dropdown" onchange="onTypChanged(this)" id="select_typ_<%=i%>">
                        <%
                            String strAktTypMitBoxArt = "";
                            if (request.getParameter("select_typ_" + i) != null)
                            {
                                strAktTypMitBoxArt = request.getParameter("select_typ_" + i);
                            }
                        %>

                        <!--<option value="">Typ</option>-->
                        <%
                            LinkedList<String> liTypen = (LinkedList<String>) application.getAttribute("Typen");
                            for (String strTypMitBoxArt : liTypen)
                            {
                                String[] strSplitTyp = strTypMitBoxArt.split(";");
                                String strTyp = strSplitTyp[0];
                                String strBoxArt = strSplitTyp[1];
                                if (strAktTypMitBoxArt.equals(strTypMitBoxArt))
                                {
                                    strAktBoxArt = strBoxArt;
                        %>
                        <option value="<%=strTypMitBoxArt%>" selected><%=strTyp%></option>
                        <%
                        } else
                        {
                        %>
                        <option value="<%=strTypMitBoxArt%>"><%=strTyp%></option>
                        <%
                                }
                            }
                            if (strAktBoxArt.isEmpty())
                            {
                                strAktBoxArt = liTypen.get(0).split(";")[1];
                            }
                        %>
                    </select>

                </div>
                <div class="two wide column" id="div_operator" style="width: 100%;">
                    <select name="select_operator_<%=i%>" class="ui fluid dropdown" id="select_operator">
                        <!--<option value="">Operator</option>-->
                        <%=generiereSelect("select_operator_" + i, strFeldOperator, request)%>
                    </select>
                </div>




                <div class="four wide column" id="div_filter_cb_<%=i%>" style="width: 100%; display: none;">
                    <select name="select_filter_<%=i%>" class="ui fluid dropdown" id="select_filter">
                        <!--<option value="">Filter</option>-->
                        <%=generiereSelect("select_filter_" + i, strFeldFilter, request)%>
                    </select>
                </div>
                <div class="four wide column" id="div_filter_txt_<%=i%>" style="width: 100%; display: none;">
                    <div class="ui input" style="width: 100%">
                        <input name="input_filter" id="input_filter" placeholder="Filter" autocomplete="off" type="text">
                    </div>
                </div>
                <div class="four wide column" id="div_filter_datepicker_<%=i%>" style="width: 100%; display: none;">
                    <div class="ui input" style="width: 100%">
                        <input name="input_filter_date" id="input_filter_date_<%=i%>" placeholder="Filter" autocomplete="off" type="text">
                    </div>
                </div>





                <div class="one wide column" id="div_klammerZu" style="width: 100%;">
                    <select name="select_klammer_zu_<%=i%>" class="ui fluid dropdown" id="select_klammer_zu_<%=i%>">
                        <!--<option value=""></option>-->
                        <%=generiereSelect("select_klammer_zu_" + i, strFeldKlammerZu, request)%>
                    </select>
                </div>
                <div class="four wide column" id="div_verknuepfung_<%=i%>" style="width: 100%;">
                    <select name="select_verknuepfung_<%=i%>" class="ui fluid dropdown" onchange="onChangeVerknuepfung(<%=i%>)" id="select_verknuepfung_<%=i%>">
                        <!--<option value="">Verknüpfung</option>-->
                        <%=generiereSelect("select_verknuepfung_" + i, strFeldVerknuepfung, request)%>
                    </select>
                </div>
            </div>
            
            <%
                    
                }
            %>

            
            
            </br>
            <div id="div_plusminus" class="ui segment" style="width: 10%; margin: auto;">
                <div class="ui equal width grid" >

                    <div class="column">
                        <button name="button_minus" type="submit" class="ui button styleRot" style="text-align: center; padding: 10%; background-color: #C00518; float: left; width: 100%; color: white;">-</button>
                    </div>

                    <div class="column">
                        <button name="button_plus" type="submit" class="ui button styleGruen" style="text-align: center; padding: 10%; background-color: #007336; float: right; width: 100%; color: white; ">+</button>
                    </div> 
                </div>
            </div>
            </br>
            <div id="div_erstellen" class="ui segment" style="width: 8%; margin: auto;">
                <div class="column" >
                    <button name="button_erstellen" type="button" onclick="onErstellen(<%=intZaehler%>)" class="ui button styleGrau" style="text-align: center; padding: 10%; background-color: #707173; width: 100%; color: white;">Erstellen</button>
                </div>
            </div> 
        </form>
        <br/>
        <!--</div>-->
        <br/>
        <<script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script src="js/jquery-ui.js"></script> 
        <script src="js/dynamisch_mitglieder.js"></script>

        <script>
                            $(function () {
            <%
                for (int i = 1; i <= intZaehler; i++)
                {
            %>
                                $("#input_filter_date_<%=i%>").datepicker({
                                    onSelect: function (selected)
                                    {
                                        var dt = new Date(selected);
                                        dt.setDate(dt.getDate() + 1);
                                        $("#input_bis_datum").datepicker("option", "minDate", dt);
                                        $("#input_von_datum").datepicker("option", "showAnim", "slideDown");
                                        $("#input_von_datum").datepicker("option", "dateFormat", "dd.mm.yy");
                                        $("#input_von_datum").datepicker("option", $.datepicker.regional['de']);
                                    }
                                });

            <%
                }
            %>
                            });
        </script>

    </body>
</html>



<%!
    /**
     * Generiert den Inhalt eines Dropdowns und setzt, falls vorhanden, zuletzt
     * gewähltes Item auf "selected"
     */
    public String generiereSelect(String strSelectName, String[] strFeld, HttpServletRequest request)
    {
        try
        {
            String strAusgabe = "";
            String strLetzteAuswahl = "";
            if (request.getParameter(strSelectName) != null)
            {
                strLetzteAuswahl = request.getParameter(strSelectName);
            }
            for (String strElement : strFeld)
            {
                if (strLetzteAuswahl.equals(strElement))
                {
                    strAusgabe += "<option value='" + strElement + "' selected>" + strElement + "</option>";
                } else
                {
                    strAusgabe += "<option value='" + strElement + "'>" + strElement + "</option>";
                }
            }
            return strAusgabe;
        } catch (Exception ex)
        {
            return "";
        }
    }
%>
