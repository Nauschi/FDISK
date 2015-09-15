
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

        <%!
            public String generiereSelect(String strSelectName, String[] strFeld, HttpServletRequest request)
            {
                String strAusgabe = "";
                String strAktAuswahl = "";
                if (request.getParameter("select_klammer_auf_") != null)
                {
                    strAktAuswahl = request.getParameter("select_klammer_auf_");
                }
                for (String strKlammerAuf : strFeld)
                {
                    if (strAktAuswahl.equals(strKlammerAuf))
                    {
                        
                    } else
                    {
                    }
                }
        %>


        return strAusgabe;
        }
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
        <div class="ui grid" id="div_mitte">

            <br/>
            <form action="MainServlet" method="POST">
                <input type="hidden" name="hidden_zaehler" value="<%=intZaehler%>">
                <table class="ui celled table" >
                    <tbody>
                        <%
                            for (int i = 1; i <= intZaehler; i++)
                            {
                        %>
                        <tr>
                            <td style="width: 5%">
                                <%
                                    if (i == intZaehler)
                                    {
                                %>
                                <button name="button_minus" type="submit" class="ui button styleRot" style="background-color: #C00518; color: white;">-</button>
                                <%
                                    }
                                %>
                            </td>
                            <td style="width: 5%">
                                <select name="select_klammer_auf_<%=i%>" class="ui fluid dropdown" id="select_klammer">
                                    <option value=""></option>
                                    <%
                                        String strAktKlammerAuf = "";
                                        if (request.getParameter("select_klammer_auf_" + i) != null)
                                        {
                                            strAktKlammerAuf = request.getParameter("select_klammer_auf_" + i);
                                        }
                                        for (String strKlammerAuf : strFeldKlammerAuf)
                                        {
                                            if (strAktKlammerAuf.equals(strKlammerAuf))
                                            {
                                    %>
                                    <option value="<%=strKlammerAuf%>" selected><%=strKlammerAuf%></option>
                                    <%
                                    } else
                                    {
                                    %>
                                    <option value="<%=strKlammerAuf%>"><%=strKlammerAuf%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </td>
                            <td style="width: 25%">
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
                            </td>
                            <td style="width: 5%">
                                <select name="select_operator_<%=i%>" class="ui fluid dropdown" id="select_operator">
                                    <option value="">Operator</option>
                                    <%
                                        String strAktOperator = "";
                                        if (request.getParameter("select_operator_" + i) != null)
                                        {
                                            strAktOperator = request.getParameter("select_operator_" + i);
                                        }
                                        for (String strOperator : strFeldOperator)
                                        {
                                            if (strAktOperator.equals(strOperator))
                                            {
                                    %>
                                    <option value="<%=strOperator%>" selected><%=strOperator%></option>
                                    <%
                                    } else
                                    {
                                    %>
                                    <option value="<%=strOperator%>"><%=strOperator%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </td>
                            <td style="width: 25%">
                                <select name="select_filter_<%=i%>" class="ui fluid dropdown" id="select_filter">
                                    <option value="">Filter</option>
                                    <%
                                        String strAktFilter = "";
                                        if (request.getParameter("select_filter_" + i) != null)
                                        {
                                            strAktFilter = request.getParameter("select_filter_" + i);
                                        }
                                        for (String strFilter : strFeldFilter)
                                        {
                                            if (strAktFilter.equals(strFilter))
                                            {
                                    %>
                                    <option value="<%=strFilter%>" selected><%=strFilter%></option>
                                    <%
                                    } else
                                    {
                                    %>
                                    <option value="<%=strFilter%>"><%=strFilter%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </td>
                            <td style="width: 5%">
                                <select name="select_klammer_zu_<%=i%>" class="ui fluid dropdown" id="select_klammer">
                                    <option value=""></option>
                                    <%
                                        String strAktKlammerZu = "";
                                        if (request.getParameter("select_klammer_zu_" + i) != null)
                                        {
                                            strAktKlammerZu = request.getParameter("select_klammer_zu_" + i);
                                        }
                                        for (String strKlammerZu : strFeldKlammerZu)
                                        {
                                            if (strAktKlammerZu.equals(strKlammerZu))
                                            {
                                    %>
                                    <option value="<%=strKlammerZu%>" selected><%=strKlammerZu%></option>
                                    <%
                                    } else
                                    {
                                    %>
                                    <option value="<%=strKlammerZu%>"><%=strKlammerZu%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </td>
                            <td>
                                <select name="select_verknuepfung_<%=i%>" class="ui fluid dropdown" id="select_verknüpfung">
                                    <option value="">Verknüpfung</option>
                                    <%
                                        String strAktVerknuepfung = "";
                                        if (request.getParameter("select_verknuepfung_" + i) != null)
                                        {
                                            strAktVerknuepfung = request.getParameter("select_verknuepfung_" + i);
                                        }
                                        for (String strVerknuepfung : strFeldVerknuepfung)
                                        {
                                            if (strAktVerknuepfung.equals(strVerknuepfung))
                                            {
                                    %>
                                    <option value="<%=strVerknuepfung%>" selected><%=strVerknuepfung%></option>
                                    <%
                                    } else
                                    {
                                    %>
                                    <option value="<%=strVerknuepfung%>"><%=strVerknuepfung%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </td>
                            <td style="width: 5%">
                                <%
                                    if (i == intZaehler)
                                    {
                                %>
                                <button name="button_plus" type="submit" class="ui button styleGruen" style="background-color: #007336; color: white;">+</button>
                                <%
                                    }
                                %>
                            </td>
                        </tr>
                        <%                        }
                        %>

                    </tbody>
                </table>
            </form>
            <br/>
        </div>
        <br/>
        <script type="text/javascript" src="js/dynamisch_mitglieder.js"></script>
        <script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script>$('.ui.dropdown').dropdown();</script>
    </body>
</html>
