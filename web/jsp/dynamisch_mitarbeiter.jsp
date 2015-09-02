<%-- 
    Document   : dynamisch_mitarbeiter
    Created on : 18.08.2015, 17:15:59
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../semantic/dist/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="../css/standardDesign.css">
        <link rel="stylesheet" type="text/css" href="../css/dynamisch.css">
        <title>Dynamisch - Mitarbeiter</title>
    </head>
    <body>
        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="../res/logo_oben.png">
                </br>
            </div>
            <div class="ui menu" style="background-color: #C00518; width: 100%">
                <a class="item">
                    Vordefiniert
                </a>
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

        <h1>Dynamisch - Mitarbeiter</h1>
        <div class="ui grid" id="div_mitte">

            <br/>
            <table class="ui celled table" >
                <tbody>
                    <%
                        for (int i = 0; i < 4; i++)
                        {
                    %>
                    <tr>
                        <td style="width: 5%">
                            <button class="ui button" style="background-color: #C00518; color: white;">-</button>
                        </td>
                        <td style="width: 10%">
                            <select name="select_klammer_auf" class="ui fluid dropdown" id="select_klammer">
                                <option value=""></option>
                                <option value="klammer1">(</option>
                                <option value="klammer2">[</option>
                                <option value="klammer3">{</option>
                            </select>
                        </td>
                        <td style="width: 15%">
                            <select name="select_typ" class="ui fluid dropdown" id="select_typ">
                                <option value="">Typ</option>
                                <option value="kurs">Kurs</option>
                                <option value="la">LA</option>
                            </select>
                        </td>
                        <td style="width: 25%">
                            <select name="select_filter" class="ui fluid dropdown" id="select_filter">
                                <option value="">Filter</option>
                                <option value="Technischer Lehrgang 1">Technischer Lehrgang 1</option>
                                <option value="Technischer Lehrgang 2">Technischer Lehrgang 2</option>
                            </select>
                        </td>
                        <td style="width: 10%">
                            <select name="select_klammer_zu" class="ui fluid dropdown" id="select_klammer">
                                <option value=""></option>
                                <option value="klammer1">)</option>
                                <option value="klammer2">]</option>
                                <option value="klammer3">}</option>
                            </select>
                        </td>
                        <td>
                            <select name="select_verknüpfung" class="ui fluid dropdown" id="select_verknüpfung">
                                <option value="">Verknüpfung</option>
                                <option value="und">UND</option>
                                <option value="undNicht">UND NICHT</option>
                                <option value="oder">ODER</option>
                                <option value="oderNicht">ODER NICHT</option>
                            </select>
                        </td>
                        <td style="width: 5%">
                            <button class="ui button" style="background-color: #007336; color: white;">+</button>
                        </td>
                    </tr>
                    <%
                        }
                    %>

                </tbody>
            </table>
          <br/>
        </div>
        <br/>
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="../semantic/dist/semantic.min.js"></script>
        <script>$('.ui.dropdown').dropdown();</script>
    </body>
</html>
