/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Initialisiert die Dropdowns
$('.ui.dropdown').dropdown();

//Initialisiert die Datepicker
$(function () {
    $("#input_von_datum").datepicker({
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


//Wird von onListItemClicked aufgerufen
/*
 * Andert je nach Bericht die Informationen die angegeben werden müssen
 */
function onChangeTypeOfDateUI(intTypeOfDateUI)
{
    if (intTypeOfDateUI == 0)
    {
        document.getElementById("div_input_von_datum").style.display = "none";
        document.getElementById("div_input_bis_datum").style.display = "none";
        document.getElementById("div_kein_datum_1").style.display = "block";
        document.getElementById("div_kein_datum_2").style.display = "block";
        document.getElementById("div_select_jahr").style.display = "none";
    } else if (intTypeOfDateUI == 1)
    {
        document.getElementById("div_input_von_datum").style.display = "none";
        document.getElementById("div_input_bis_datum").style.display = "none";
        document.getElementById("div_kein_datum_1").style.display = "block";
        document.getElementById("div_kein_datum_2").style.display = "none";
        document.getElementById("div_select_jahr").style.display = "block";
    } else if (intTypeOfDateUI == 2)
    {
        document.getElementById("div_input_von_datum").style.display = "block";
        document.getElementById("div_input_bis_datum").style.display = "block";
        document.getElementById("div_kein_datum_1").style.display = "none";
        document.getElementById("div_kein_datum_2").style.display = "none";
        document.getElementById("div_select_jahr").style.display = "none";
    }
}

//Wird aufgerufen wenn bei der Liste ein anderer Bericht ausgewählt wird
/*
 * ändert die angezeigten Informationen je nach Bericht
 */
function onListItemClicked(item)
{
    var liItems = document.getElementById("div_liste").getElementsByTagName("a");
    var index;
    for (index = 0; index < liItems.length; ++index) {
        liItems[index].className = "item";
    }
    item.className = item.className + " active";
    document.getElementById("div_abbrechen_bestaetigen").style.display = "none";
    var strTable = item.getElementsByTagName("div")[0].innerHTML;
    var strBerichtname = item.getElementsByTagName("span")[0].innerHTML;
    var intTypeOfDateUI = item.getElementsByTagName("div")[1].innerHTML;
    onChangeTypeOfDateUI(intTypeOfDateUI);
    document.getElementById("div_daten").getElementsByTagName("h2")[0].innerHTML = strBerichtname;
    document.getElementById("input_hidden").value = strBerichtname;
    document.getElementById("div_table").innerHTML = strTable;
}




/*
 * Leitet zum PDFServlet weiter um eine PDF zu erstellen, anzuzeigen und herunterzuladen
 */
function saveDataForPDF()
{
    var strTable = document.getElementById("div_table").innerHTML;
    var strName = document.getElementById("h2_bericht").innerHTML;
    document.getElementById("hidden_pdfData").value = strName + "###" + strTable;
    document.formPDF.submit();
}

/*
 * Leitet zum CSVServlet weiter um eine CSV zu erstellen und herunterzuladen
 */
function saveDataForCSV()
{
    var strTable = document.getElementById("div_table").innerHTML;
    var strName = document.getElementById("h2_bericht").innerHTML;
    document.getElementById("hidden_CSVData").value = strName + "###" + strTable;
    document.formCSV.submit();
}


function abschittChanged(select_abschnitt)
{
    //alert("Abschnitt_value: "+select_abschnitt.value);
    if (select_abschnitt.value != -1)
    {
        //alert("IN abschnitt changed");
        var strFeuerwehrOptions = document.getElementById("div_" + select_abschnitt.value).innerHTML;
        document.getElementById("div_feuerwehr").innerHTML = '<select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr"></select>';
        document.getElementById("select_feuerwehr").innerHTML = strFeuerwehrOptions;
        $('#select_feuerwehr').dropdown();
        //alert(strFeuerwehrOptions);
    }
}

function bezirkChanged(select_bezirk)
{
    //alert("Bezirk_value: "+select_bezirk.value);
    if (select_bezirk.value != -1)
    {
        //alert("IN bezirk changed");
        var strAbschnittOptions = document.getElementById("div_" + select_bezirk.value).innerHTML;
        document.getElementById("div_abschnitt").innerHTML = '<select name="select_abschnitt" class="ui fluid dropdown" id="select_abschnitt" onchange="abschittChanged(this)"></select>';
        document.getElementById("select_abschnitt").innerHTML = strAbschnittOptions;
        $('#select_abschnitt').dropdown();
        //alert(strAbschnittOptions);
    }
}