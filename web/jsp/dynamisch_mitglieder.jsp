
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
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
        <link rel="stylesheet" type="text/css" href="css/jquery-ui_1.css">
        <link rel="stylesheet" type="text/css" href="css/jquery-ui_2.css">
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
                "N/A", "(", "[", "{"
            };
            String[] strFeldKlammerZu =
            {
                "N/A", ")", "]", "}"
            };

            String[] strFeldOperator =
            {
                "=", "<>", "<=", ">=", "<", ">"
            };

            String[] strFeldFilter =
            {
                "Technischer Lehrgang 1", "Technischer Lehrgang 2"
            };

            String[] strFeldVerknuepfung =
            {
                "N/A", "UND", "UND NICHT", "ODER", "ODER NICHT"
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

            if (request.getParameter("hidden_action") != null)
            {

                String strAction = request.getParameter("hidden_action");
                if (strAction.equals("plus"))
                {
                    intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler")) + 1;
                } else if (strAction.equals("minus"))
                {
                    intZaehler = Integer.parseInt(request.getParameter("hidden_zaehler")) - 1;
                    if (intZaehler < 1)
                    {
                        intZaehler = 1;
                    }
                }

            }
        %>
       
        <h1>Dynamisch - Mitglieder</h1>
        <!--<div class="ui grid" id="div_mitte">-->

        <br/>

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
        <form action="MainServlet" method="POST" name="form_plus_minus_erstellen">
            <input type="hidden" name="hidden_zaehler" value="<%=intZaehler%>">
            <%
                for (int i = 1; i <= intZaehler; i++)
                {
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
                            if (request.getParameter("hidden_element_data_" + i) != null)
                            {
                                String strTyp = request.getParameter("hidden_element_data_" + i).split(";")[1];
                                String strBoxArt = request.getParameter("hidden_element_data_" + i).split(";")[2];
                                strAktTypMitBoxArt = strTyp + ";" + strBoxArt;
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
                <div class="two wide column" id="div_operator_<%=i%>" style="width: 100%;">
                    <select class="ui fluid dropdown" id="select_operator_<%=i%>">
                        <!--<option value="">Operator</option>-->
                        <%--<%=generiereSelect("select_operator_" + i, strFeldOperator, request)%>--%>
                    </select>
                </div>




                <div class="four wide column" id="div_filter_cb_<%=i%>" style="width: 100%; display: none;">
                    <select class="ui fluid dropdown" id="select_filter_cb_<%=i%>">
                        <!--<option value="">Filter</option>-->
                        <%--<%=generiereSelect("select_filter_" + i, strFeldFilter, request)%>--%>
                    </select>
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



            </br>
            <div id="div_plusminus" class="ui segment" style="width: 10%; margin: auto;">
                <div class="ui equal width grid" >

                    <div class="column">
                        <button name="button_minus" type="button" onclick="onPlusMinusZeile_Erstellen(<%=intZaehler%>, 'minus')" class="ui button styleRot" style="text-align: center; padding: 10%; background-color: #C00518; float: left; width: 100%; color: white;">-</button>
                    </div>

                    <div class="column">
                        <button name="button_plus" type="button" onclick="onPlusMinusZeile_Erstellen(<%=intZaehler%>, 'plus')" class="ui button styleGruen" style="text-align: center; padding: 10%; background-color: #007336; float: right; width: 100%; color: white; ">+</button>
                    </div>
                </div>
            </div>
            </br>
            <div id="div_erstellen" class="ui segment" style="width: 8%; margin: auto;">
                <div class="column" >
                    <button name="button_erstellen" type="button" onclick="onPlusMinusZeile_Erstellen(<%=intZaehler%>, 'erstellen')" class="ui button styleGrau" style="text-align: center; padding: 10%; background-color: #707173; width: 100%; color: white;">Erstellen</button>
                </div>
            </div> 
        </form>
        <br/>
        
        
        <%
            if (request.getAttribute("dyn_table") != null)
            {
                StringBuilder sbDynHTML = (StringBuilder) request.getAttribute("dyn_table");
                out.println("<div class='ui segment' style='width:90%; margin: 0 auto !important;'>");
                out.println(sbDynHTML);
                out.println("</div>");
            }

        %>
        <!--</div>-->
        <br/>
        <script src="js/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script src="js/jquery-ui.js"></script> 
        <script src="js/tablesort.js"></script>
        <script src="js/dynamisch_mitglieder.js"></script>
        <script src="js/datepicker-de.js"></script>
        

        <script>
                        $(function () {
            <%                for (int i = 1; i <= intZaehler; i++)
                {
            %>

                            $("#input_filter_datepicker_<%=i%>").datepicker({
                                onSelect: function (selected)
                                {
                                    $("#input_filter_datepicker_<%=i%>").datepicker("option", "showAnim", "slideDown");
                                    $("#input_filter_datepicker_<%=i%>").datepicker("option", "dateFormat", "dd.mm.yy");
                                    $("#input_filter_datepicker_<%=i%>").datepicker("option", $.datepicker.regional['de']);
                                }
                            });
            <%
                }
                HashMap<String, LinkedList<String>> hsFilter = (HashMap<String, LinkedList<String>>) session.getAttribute("hashMap_typ");
                Set<String> set = hsFilter.keySet();
                Iterator it = set.iterator();
            %>
                            var mapAlt = {
            <%
                while (it.hasNext())
                {
                    String strKey = (String) it.next();
                    LinkedList<String> liValues = hsFilter.get(strKey);
                    out.print("'" + strKey + "' : '");
                    for (String value : liValues)
                    {
                        value = value.replace("''", "\"");
                        value = value.replace(";", "");
                        if (liValues.getLast().equals(value))
                        {
                            out.print(value);
                        } else
                        {
                            out.print(value + ";");
                        }

                    }
                    if (it.hasNext())
                    {
                        out.println("',");
                    } else
                    {
                        out.print("'");
                    }
                }

            %>
                            };
                            setMap(mapAlt);

            <%                for (int i = 1; i <= intZaehler; i++)
                {
            %>
                            var strLastFilter = null;
                            var strLastOperator = null;
            <%
                if (request.getParameter("hidden_element_data_" + i) != null)
                {
                    out.println("strLastFilter= '" + request.getParameter("hidden_element_data_" + i).split(";")[4] + "';");
                    out.println("strLastOperator= '" + request.getParameter("hidden_element_data_" + i).split(";")[3] + "';");
                }

            %>
                            onTypChanged(document.getElementById("select_typ_<%=i%>"), strLastFilter, strLastOperator);
            <%   }
                if (request.getAttribute("dyn_table") != null)
                {
            %>
                            $('.sortable.table').tablesort();
                            $('th').popup();
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
    public String generiereSelect(int intIndex, String[] strFeld, HttpServletRequest request, int intAktI)
    {
        try
        {
            String strAusgabe = "";
            String strLetzteAuswahl = "";
            if (request.getParameter("hidden_element_data_" + intAktI) != null)
            {
                strLetzteAuswahl = request.getParameter("hidden_element_data_" + intAktI).split(";")[intIndex];
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
