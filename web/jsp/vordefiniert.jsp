<%-- 
    Document   : vordefiniert
    Created on : 02.08.2015, 13:14:24
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="semantic/dist/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="css/standardDesign.css">
        <link rel="stylesheet" type="text/css" href="css/vordefiniert.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="Stylesheet"type="text/css"/>
        <title>Vordefiniert</title>
    </head>
    <body>
        <div class="ui segment" id="div_oben">
            <div style="float: right">
                <img class="ui small image" src="res/logo_oben.png">
                </br>
            </div>

            <div class="ui menu" style="background-color: #C00518; width: 100%">
                <a class="item active">
                    Vordefiniert
                </a>
                <div class="ui simple dropdown item">
                    Dynamisch
                    <i class="dropdown icon"></i>
                    <div class="menu">
                        <div class="item" onclick="location.href = 'jsp/dynamisch_mitarbeiter.jsp'">Mitglieder</div>
                        <div class="item" onclick="location.href = 'jsp/dynamisch_fahrzeuge_geraete.jsp'">Fahrzeuge und Geräte</div>
                    </div>
                </div>
            </div>
        </div>
        <h1>Vordefiniert</h1>
        <div class="ui grid" id="div_mitte">
            <div class="four wide column">
                <div class="ui vertical fluid tabular menu" id="div_liste">

                    <%
                        for (int i = 0; i < 10; i++) {
                            if (i == 0) {
                                out.println("<a class='item active' onclick='onListItemClicked(this)'>Liste" + i + "</a>");
                            } else {
                                out.println("<a class='item' onclick='onListItemClicked(this)'>Liste" + i + "</a>");
                            }
                        }
                    %>

                </div>
            </div>
            <div class="twelve wide stretched column">
                <div class="ui segment" id="div_daten">
                    <h2>Liste0</h2>

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
                                <option value="Feuerwehr4">Feuerwehr4</option>
                                <option value="Feuerwehr5">Feuerwehr5</option>
                                <option value="Feuerwehr6">Feuerwehr6</option>
                                <option value="Feuerwehr7">Feuerwehr7</option>
                                <option value="Feuerwehr8">Feuerwehr8</option>
                            </select>
                        </div>
                    </div>
                    <div class="ui equal width grid">
                        <div class="column">
                            <div class="ui input" style="width: 100%">
                                <input id="input_von_datum" placeholder="von..." type="text">
                            </div>
                        </div>
                        <div class="column" >
                            <div class="ui input" style="width: 100%">
                                <input id="input_bis_datum" placeholder="bis..." type="text">
                            </div>
                        </div>
                        <div class="column">
                            <button class="ui button" style="background-color: #707173; width: 100%; color: white;">Vorschau</button>
                        </div>
                    </div>

                    <table class="ui celled table">
                        <thead>
                            <tr><th>Header</th>
                                <th>Header</th>
                                <th>Header</th>
                            </tr></thead>
                        <tbody>
                            <tr>
                                <td>Cell</td>
                                <td>Cell</td>
                                <td>Cell</td>
                            </tr>
                            <tr>
                                <td>Cell</td>
                                <td>Cell</td>
                                <td>Cell</td>
                            </tr>
                            <tr>
                                <td>Cell</td>
                                <td>Cell</td>
                                <td>Cell</td>
                            </tr>
                        </tbody>
                    </table>

                    <div id="div_abbrechen_bestaetigen">
                        <div class="ui equal width grid">
                            <div class="column">
                                <button class="ui button" style="background-color: #C00518; width: 100%; color: white;">Zurücksetzen</button>
                            </div>
                            <div class="column">
                                <button class="ui button" style="background-color: #007336; width: 100%; color: white;">Bestätigen</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <br/>

        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script type="text/javascript" src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/minified/i18n/jquery-ui-i18n.min.js"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js" type="text/javascript"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js" type="text/javascript"></script>

        <script src="js/vordefiniert.js"></script>

        <script>$('.ui.dropdown').dropdown();</script>

        <script>
            $(function () {
                $("#input_von_datum").datepicker({
                    onSelect: function (selected) {
                        var dt = new Date(selected);
                        dt.setDate(dt.getDate() + 1);                        
                        $("#input_bis_datum").datepicker("option", "minDate", dt);
                        $("#input_von_datum").datepicker("option", "showAnim", "slideDown");
                        $("#input_von_datum").datepicker("option", "dateFormat", "dd.mm.yy");
                        $("#input_von_datum").datepicker("option", $.datepicker.regional['de']);
                    }
                });
                $("#input_bis_datum").datepicker({
                    onSelect: function (selected) {
                        var dt = new Date(selected);
                        dt.setDate(dt.getDate() - 1);
                        $("#input_von_datum").datepicker("option", "maxDate", dt);
                        $("#input_bis_datum").datepicker("option", "showAnim", "slideDown");
                        $("#input_bis_datum").datepicker("option", "dateFormat", "dd.mm.yy");
                        $("#input_bis_datum").datepicker("option", $.datepicker.regional['de']);
                    }
                });
            });
        </script>

    </body>
</html>
