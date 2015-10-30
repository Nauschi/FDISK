
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
        <title>Dynamisch - Mitglieder</title>
    </head>
    <body>
        <%
            session.setAttribute("lastPage", "dynamisch_mitglieder");
            
            String[] strFeldKlammerAuf =
            {
                "(", "[", "{"
            };

            String[] strFeldKlammerZu =
            {
                ")", "]", "}"
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
                "UND", "UND NICHT", "ODER", "ODER NICHT"
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
                    <a href="#" onclick="document.form_vordefiniert.submit();" class="item">
                        Vordefiniert
                    </a>
                </form>
                <a class="item active">
                    Dynamisch
                </a>
                <div class="right menu">
                    <form action="MainServlet" method="POST" name="form_logout">
                        <input type="hidden" name="logout">
                        <a href="#" onclick="document.form_logout.submit();" class="ui item">
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

                <div class="one wide column" id="div_klammerAuf">
                    <select name="select_klammer_auf_<%=i%>"  class="ui fluid dropdown" id="select_klammer">
                        <option value=""></option>
                        <%=generiereSelect("select_klammer_auf_" + i, strFeldKlammerAuf, request)%>
                    </select>
                </div>

                <div class="four wide column" id="div_typ">
                    <select name="select_typ_<%=i%>" class="ui fluid dropdown" id="select_typ">
                        <%
                            String strAktTyp = "";
                            if (request.getParameter("select_typ_" + i) != null)
                            {
                                strAktTyp = request.getParameter("select_typ_" + i);
                            }
                        %>

                        <option value="">Typ</option>
                        <%
                            LinkedList<String> liTypen = (LinkedList<String>) application.getAttribute("Typen");
                            for (String strTyp : liTypen)
                            {
                                if (strAktTyp.equals(strTyp))
                                {
                        %>
                        <option value="<%=strTyp%>" selected><%=strTyp%></option>
                        <%
                        } else
                        {
                        %>
                        <option value="<%=strTyp%>"><%=strTyp%></option>
                        <%
                                }
                            }
                        %>
                    </select>

                </div>
                <div class="two wide column" id="div_operator">
                    <select name="select_operator_<%=i%>" class="ui fluid dropdown" id="select_operator">
                        <option value="">Operator</option>
                        <%=generiereSelect("select_operator_" + i, strFeldOperator, request)%>
                    </select>
                </div>
                <div class="four wide column" id="div_filter">
                    <select name="select_filter_<%=i%>" class="ui fluid dropdown" id="select_filter">
                        <option value="">Filter</option>
                        <%=generiereSelect("select_filter_" + i, strFeldFilter, request)%>
                    </select>
                </div>
                <div class="one wide column" id="div_klammerZu">
                    <select name="select_klammer_zu_<%=i%>" class="ui fluid dropdown" id="select_klammer">
                        <option value=""></option>
                        <%=generiereSelect("select_klammer_zu_" + i, strFeldKlammerZu, request)%>
                    </select>
                </div>
                <div class="four wide column" id="div_verknuepfung">
                    <select name="select_verknuepfung_<%=i%>" class="ui fluid dropdown" id="select_verknüpfung">
                        <option value="">Verknüpfung</option>
                        <%=generiereSelect("select_verknuepfung_" + i, strFeldVerknuepfung, request)%>
                    </select>
                </div>
            </div>


            <%
                if (i == intZaehler)
                {
            %>
            </br>
            <div id="div_plusminus" class="ui segment" style="width: 10%; margin: auto;">
                <div class="ui equal width grid" >
                    <div class="column">
                        <button name="button_plus" type="submit" class="ui button styleGruen" style="background-color: #007336; float: right; color: white;">+</button>
                    </div>
                    <div class="column">
                        <button name="button_minus" type="submit" class="ui button styleRot" style="background-color: #C00518; color: white;">-</button>
                    </div>
                </div>
                <!--<button name="button_plus" type="submit" class="ui button styleGruen" style="background-color: #007336; color: white;">+</button>
                <button name="button_minus" type="submit" class="ui button styleRot" style="background-color: #C00518; color: white;">-</button>-->
            </div>

            <div id="div_abbrechen_bestaetigen" style="display:none" class="ui segment">
                <div class="ui equal width grid">
                    <div class="column">
                        <button class="ui button styleRot" style="background-color: #C00518; width: 100%; color: white;">Zurücksetzen</button>
                    </div>
                    <div class="column">
                        <button onclick="onBestaetigen();" name="button_bestaetigen" class="ui button styleGruen"  style="background-color: #007336; width: 100%; color: white;">Bestätigen</button>
                    </div>
                </div>
            </div>
            <%
                    }
                }
            %>

        </form>
        <br/>
        <!--</div>-->
        <br/>
        <script type="text/javascript" src="js/dynamisch_mitglieder.js"></script>
        <script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script>$('.ui.dropdown').dropdown();</script>
    </body>
</html>



<%!
    /**
     * Generiert den Inhalt eines Dropdowns und setzt, falls vorhanden, zuletzt
     * gewähltes Item auf "selected"
     */
    public String generiereSelect(String strSelectName, String[] strFeld, HttpServletRequest request)
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
    }
%>
