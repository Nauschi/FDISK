/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var boolDeleteOnChange = false;

//Initialisiert die Dropdowns
$('.ui.dropdown').dropdown();


//Initialisiert die Datepicker
$(function () {
    $("#input_von_datum").datepicker({
        onSelect: function (selected)
        {
            var dt = new Date(dateUmwandeln(selected));
            if (selected != null)
            {
                document.getElementById("div_remove_von_datum").style.display = "block";
            }
            dt.setDate(dt.getDate() + 1);
            $("#input_bis_datum").datepicker("option", "minDate", dt);
            $("#input_von_datum").datepicker("option", "showAnim", "slideDown");
            $("#input_von_datum").datepicker("option", "dateFormat", "dd.mm.yy");
            $("#input_von_datum").datepicker("option", $.datepicker.regional['de']);
        }
    });
    $("#input_bis_datum").datepicker({
        onSelect: function (selected) {

            var dt = new Date(dateUmwandeln(selected));
            if (selected != null)
            {
                document.getElementById("div_remove_bis_datum").style.display = "block";
            }
            dt.setDate(dt.getDate() - 1);
            $("#input_von_datum").datepicker("option", "maxDate", dt);
            $("#input_bis_datum").datepicker("option", "showAnim", "slideDown");
            $("#input_bis_datum").datepicker("option", "dateFormat", "dd.mm.yy");
            $("#input_bis_datum").datepicker("option", $.datepicker.regional['de']);
        }
    });
});

/**
 * Wandelt den Date String in das Format "yyyy-MM-dd" um
 * @param {type} selected
 * @returns {String}
 */
function dateUmwandeln(selected)
{
    var strSplitDate = selected.split(".");
    return strSplitDate[2] + "-" + strSplitDate[1] + "-" + strSplitDate[0];
}


/**
 * Wird von onListItemClicked aufgerufen
 * Ändert je nach Bericht die Objekte die der User sieht
 * @param {type} intTypeOfDateUI
 * @returns {undefined}
 */
function onChangeTypeOfDateUI(intTypeOfDateUI)
{
    if (intTypeOfDateUI == 0)
    {
        document.getElementById("div_input_von_datum").style.display = "none";
        document.getElementById("div_input_bis_datum").style.display = "none";
        document.getElementById("div_select_jahr").style.display = "none";
        document.getElementById("div_mitglied").style.display = "none";
        document.getElementById("div_kennzeichen").style.display = "none";
        document.getElementById("div_hidden_hilfe").style.display = "block";
    } else if (intTypeOfDateUI == 1)
    {
        document.getElementById("div_input_von_datum").style.display = "none";
        document.getElementById("div_input_bis_datum").style.display = "none";
        document.getElementById("div_select_jahr").style.display = "block";
        document.getElementById("div_mitglied").style.display = "none";
        document.getElementById("div_kennzeichen").style.display = "none";
        document.getElementById("div_hidden_hilfe").style.display = "none";
    } else if (intTypeOfDateUI == 2)
    {
        document.getElementById("div_input_von_datum").style.display = "block";
        document.getElementById("div_input_bis_datum").style.display = "block";
        document.getElementById("div_mitglied").style.display = "none";
        document.getElementById("div_select_jahr").style.display = "none";
        document.getElementById("div_kennzeichen").style.display = "none";
        document.getElementById("div_hidden_hilfe").style.display = "block";

    } else if (intTypeOfDateUI == 3)
    {
        document.getElementById("div_input_von_datum").style.display = "block";
        document.getElementById("div_input_bis_datum").style.display = "block";
        document.getElementById("div_select_jahr").style.display = "none";
        document.getElementById("div_kennzeichen").style.display = "block";
        document.getElementById("div_mitglied").style.display = "none";
        document.getElementById("div_hidden_hilfe").style.display = "none";
    } else if (intTypeOfDateUI == 4)
    {
        document.getElementById("div_input_von_datum").style.display = "block";
        document.getElementById("div_input_bis_datum").style.display = "block";
        document.getElementById("div_select_jahr").style.display = "none";
        document.getElementById("div_kennzeichen").style.display = "none";
        document.getElementById("div_mitglied").style.display = "block";
        document.getElementById("div_hidden_hilfe").style.display = "none";
    }


    var strBerichtname = document.getElementById("input_hidden").value;
    if (strBerichtname == "Dienstzeitliste")
    {
        document.getElementById("legend_jahr").innerHTML = "<b>Dienstalter im Jahr</b>";
    } else if (strBerichtname == "Geburtstagsliste")
    {
        document.getElementById("legend_jahr").innerHTML = "<b>Alter im Jahr</b>";
    }

}

/**
 * Wird aufgerufen wenn bei der Liste auf der Linken Seite ein anderer Bericht ausgewählt wird.
 * Ändert die angezeigten Informationen je nach Bericht
 * @param {type} item
 * @returns {undefined}
 */
function onListItemClicked(item)
{



    var liItems = document.getElementById("div_liste").getElementsByTagName("a");
    var index;

    if (document.getElementById("div_zusatzDaten") != null)
    {
        document.getElementById("div_zusatzDaten").remove();
    }
    for (index = 0; index < liItems.length; ++index) {
        liItems[index].className = "item";
    }
    item.className = item.className + " active";
    console.log("ListItemClicked");
    document.getElementById("button_Search").style.display = "none";
    document.getElementById("div_csv_pdf").style.display = "none";
    var strTable = item.getElementsByTagName("div")[0].innerHTML;
    var strBerichtname = item.getElementsByTagName("span")[0].innerHTML;
    var intTypeOfDateUI = item.getElementsByTagName("div")[1].innerHTML;

    document.getElementById("div_daten").getElementsByTagName("h2")[0].innerHTML = strBerichtname;
    document.getElementById("input_hidden").value = strBerichtname;
    document.getElementById("div_table").innerHTML = strTable;
    onChangeTypeOfDateUI(intTypeOfDateUI);
    if (document.getElementById("div_zusatzDaten") == null && strBerichtname == 'Kursstatistik')
    {
        document.getElementById("div_kursstatistik").style.display = "block";
    } else
    {
        document.getElementById("div_kursstatistik").style.display = "none";
    }

    if (document.getElementById("div_zusatzDaten") == null && strBerichtname == 'Digitales Fahrtenbuch')
    {
        document.getElementById("div_fahrtenbuch").style.display = "block";
    } else
    {
        document.getElementById("div_fahrtenbuch").style.display = "none";
    }
}

/**
 * Filtert die Tabelle der Vorschau nach dem eingegebenen String
 * @returns {undefined}
 */
function onSucheInTabelle(input) {

    var $rows = $('#table tbody tr');
    var val = $.trim(input).replace(/ +/g, '').toLowerCase();
    $rows.show().filter(function () {
        var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
        return !~text.indexOf(val);
    }).hide();
    $('#modal_search').modal('hide');

}


/**
 * Leitet zum PDFServlet weiter um eine PDF zu erstellen, anzuzeigen und herunterzuladen
 * @returns {undefined}
 */
function saveDataForPDF()
{
    var strTable = document.getElementById("div_table").innerHTML;
    var strName = document.getElementById("h2_bericht").innerHTML;
    if (document.getElementById("div_zusatzDaten") != null)
    {
        var strZusatzDaten = document.getElementById("div_zusatzDaten").innerHTML;
        document.getElementById("hidden_pdfData").value = strName + "###" + strTable + "###" + strZusatzDaten;
    } else
    {
        document.getElementById("hidden_pdfData").value = strName + "###" + strTable;
    }
    document.formPDF.submit();
}

/**
 * Leitet zum CSVServlet weiter um eine CSV zu erstellen und herunterzuladen
 * @returns {undefined}
 */
function saveDataForCSV()
{
    var strName = document.getElementById("h2_bericht").innerHTML;
    if (strName == "Stundenauswertung je Mitglied je Instanz")
    {
        document.getElementById("modal_fehler").getElementsByTagName("p")[0].innerHTML = "Diese  Funktion ist für diesen Bericht nicht verfügbar";
        $('#modal_fehler').modal('show');
        return;
    }
    var strTable = document.getElementById("div_table").innerHTML;
    if (document.getElementById("div_zusatzDaten") != null)
    {
        var strZusatzDaten = document.getElementById("div_zusatzDaten").innerHTML;
        document.getElementById("hidden_CSVData").value = strName + "###" + strTable + "###" + strZusatzDaten;
    } else
    {
        document.getElementById("hidden_CSVData").value = strName + "###" + strTable;
    }
    document.formCSV.submit();
}

/**
 * Setzt das Mitglieder Dropdown zurück zum Default.
 * @returns {undefined}
 */
function resetSelectMitglieder()
{
    var lenght = document.getElementById("select_mitglied").getElementsByTagName("option").length;
    if (lenght > 1)
    {
        document.getElementById("div_select_mitglieder").innerHTML = '<select name="select_mitglied" class="ui fluid dropdown" id="select_mitglied"></select>';
        var selectMitglied = document.getElementById("select_mitglied");
        var opt = document.createElement('option');
        opt.value = "-2";
        opt.innerHTML = "Alle Mitglieder";
        selectMitglied.appendChild(opt);
        $('#select_mitglied').dropdown();
        fixDropdowns("select_mitglied");
        document.getElementById("div_mitglieder_submit").style.display = "block";
    }
}

/**
 * Wird aufgerufen wenn sich im Abschnitt Dropdown die Value ändert und aktualisiert
 * das Feuerwehr Dropdown
 * @param {type} select_abschnitt
 * @param {type} strLetzteFW
 * @returns {undefined}
 */
function abschittChanged(select_abschnitt, strLetzteFW)
{
    if (boolDeleteOnChange == true)
    {
        $('.ui.search').search();
        resetSelectMitglieder();
    }
    if (select_abschnitt.value != -1 && select_abschnitt.value != -2)
    {
        var strFeuerwehrOptions = document.getElementById("div_" + select_abschnitt.value).innerHTML;
        if (strLetzteFW != null)
        {
            strFeuerwehrOptions = strFeuerwehrOptions.replace('value="' + strLetzteFW + '"', 'value="' + strLetzteFW + '" selected');
        }
        document.getElementById("fieldset_feuerwehr").innerHTML = '<legend><b>Feuerwehr</b></legend><select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr" onchange="feuerwehrChanged(this)"></select>';
        if (strFeuerwehrOptions.split('<option').length > 1)
        {
            document.getElementById("select_feuerwehr").innerHTML = "<option value='-2'>Alle Feuerwehren</option>" + strFeuerwehrOptions;
        } else
        {
            document.getElementById("select_feuerwehr").innerHTML = strFeuerwehrOptions;
        }

        $('#select_feuerwehr').dropdown();
    } else if (select_abschnitt.value == -2)
    {
        document.getElementById("fieldset_feuerwehr").innerHTML = '<legend><b>Feuerwehr</b></legend><select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr" onchange="feuerwehrChanged(this)"></select>';
        document.getElementById("select_feuerwehr").innerHTML = "<option value='-2'>Alle Feuerwehren</option>"
        $('#select_feuerwehr').dropdown();
        fixDropdowns("select_feuerwehr");

    }
}

/**
 * Wird aufgerufen wenn sich im Bezirk Dropdown die Value ändert und aktualisiert
 * das Abschnitt Dropdown
 * @param {type} select_bezirk
 * @param {type} strLetzteAbschnitt
 * @returns {undefined}
 */
function bezirkChanged(select_bezirk, strLetzteAbschnitt)
{
    if (boolDeleteOnChange == true)
    {
        $('.ui.search').search();
        resetSelectMitglieder();
    }
    if (select_bezirk.value != -1 && select_bezirk.value != -2)
    {
        var strAbschnittOptions = document.getElementById("div_" + select_bezirk.value).innerHTML;
        if (strLetzteAbschnitt != null)
        {
            strAbschnittOptions = strAbschnittOptions.replace('value="' + strLetzteAbschnitt + '"', 'value="' + strLetzteAbschnitt + '" selected');
        }
        document.getElementById("fieldset_abschnitt").innerHTML = '<legend><b>Abschnitt</b></legend><select name="select_abschnitt" class="ui fluid dropdown" id="select_abschnitt" onchange="abschittChanged(this)"></select>';
        if (strAbschnittOptions.split('<option').length > 1)
        {
            document.getElementById("select_abschnitt").innerHTML = "<option value='-2'>Alle Abschnitte</option>" + strAbschnittOptions;
        } else
        {
            document.getElementById("select_abschnitt").innerHTML = strAbschnittOptions;
        }
        $('#select_abschnitt').dropdown();
    } else if (select_bezirk.value == -2)
    {
        document.getElementById("fieldset_abschnitt").innerHTML = '<legend><b>Abschnitt</b></legend><select name="select_abschnitt" class="ui fluid dropdown" id="select_abschnitt" onchange="abschittChanged(this)"></select>';
        document.getElementById("select_abschnitt").innerHTML = "<option value='-2'>Alle Abschnitte</option>";
        $('#select_abschnitt').dropdown();
        fixDropdowns("select_abschnitt");

    }
}

/**
 * Wird aufgerufen wenn sich die Value des Feuerwehr Dropdowns verändert
 * und löscht daten die nur für die Vorherige Sicht waren. 
 * @param {type} select_feuerwehr
 * @returns {undefined}
 */
function feuerwehrChanged(select_feuerwehr)
{
    if (boolDeleteOnChange == true)
    {
        $('.ui.search').search();
        resetSelectMitglieder();
    }
}

/**
 * Überprüft ob in einem Dropdown nur ein Wert
 * vorhanden ist. Wenn ja wird es auf disabled gesetzt.
 * @param {type} id
 * @returns {undefined}
 */
function fixDropdowns(id)
{
    var lenght = document.getElementById(id).getElementsByTagName("option").length;
    if (lenght == 1)
    {
        $("#" + id).parent("div").addClass("disabled");
    } else
    {
        $("#" + id).parent("div").removeClass("disabled");
    }
}

/**
 * Löscht die Value des übergebenen Datepicker Objekt und aktualisiert min
 * und max Date.
 * @param {type} idDiv
 * @param {type} idDatepicker
 * @returns {undefined}
 */
function removeDateAndSetDivHidden(idDiv, idDatepicker)
{
    document.getElementById(idDiv).style.display = "none";
    document.getElementById(idDatepicker).value = "";
    if (idDatepicker.contains("von"))
    {
        $("#input_bis_datum").datepicker("option", "minDate", null);
    } else
    {
        $("#input_von_datum").datepicker("option", "maxDate", null);
    }
}

/**
 * Verändert eine Instanzvariable die erst auf true gesetz wird wenn die ganze 
 * Seite fertig geladen ist.
 * @returns {undefined}
 */
function setDeleteOnChange()
{
    boolDeleteOnChange = true;
}
/**
 * Leitet zum dynamisch_mitglieder.jsp weiter
 * @returns {undefined}
 */
function zuDynamischWeiterleiten()
{
    var strBezirk = document.getElementById("select_bezirk").value;
    var strAbschnitt = document.getElementById("select_abschnitt").value;
    var strFeuerwehr = document.getElementById("select_feuerwehr").value;
    var strBerechtigung = strBezirk + ";" + strAbschnitt + ";" + strFeuerwehr;
    document.getElementById("hidden_berechtigungs_info").value = strBerechtigung;
    document.form_dynamisch.submit();
}



