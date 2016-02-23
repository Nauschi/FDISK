/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var map = {'Test': 'anushgjk'};


//Initialisiert die Dropdowns
$('.ui.dropdown').dropdown();




function setMap(otherMap)
{
    map = otherMap;
}


//Wird aufgerufen wenn in einem Dropdown wo der Typ bestimmt wird sich etwas ändert
/**
 * Stellt je nach Typ in der Zeile, den Filter auf ein Dropdown, Textfield oder Datepicker
 * @param {type} select_typ
 * @returns {undefined}
 */
function onTypChanged(select_typ, strLastFilter, strLastOperator)
{
    var strTypPlusBox = select_typ.value;
    var strID = select_typ.getAttribute('id');
    strID = strID.split("_")[2];
    var strBoxArt = strTypPlusBox.split(";")[1];
    var strTyp = strTypPlusBox.split(";")[0];
    if (strBoxArt == "cb")
    {

        document.getElementById("div_filter_cb_" + strID).style.display = "block";
        document.getElementById("div_filter_txt_" + strID).style.display = "none";
        document.getElementById("div_filter_datepicker_" + strID).style.display = "none";
        initialisiereCBFilter(strTyp, strID, strLastFilter);


    } else if (strBoxArt == "txt")
    {
        var operatorFeld = ["=", "<>"];
        aktualisiereOperator(strID, operatorFeld, strLastOperator);
        document.getElementById("div_filter_cb_" + strID).style.display = "none";
        document.getElementById("div_filter_txt_" + strID).style.display = "block";
        if (strLastFilter != null)
        {
            document.getElementById("input_filter_" + strID).value = strLastFilter;
        }
        document.getElementById("div_filter_datepicker_" + strID).style.display = "none";
    } else if (strBoxArt == "datepicker")
    {
        document.getElementById("div_filter_cb_" + strID).style.display = "none";
        document.getElementById("div_filter_txt_" + strID).style.display = "none";
        document.getElementById("div_filter_datepicker_" + strID).style.display = "block";
        if (strLastFilter != null)
        {
            document.getElementById("input_filter_datepicker_" + strID).value = strLastFilter;
        }
    }

    if (strBoxArt == "datepicker" || strBoxArt == "cb")
    {
        var operatorFeld = ["=", "<>", "<=", ">=", "<", ">"];
        aktualisiereOperator(strID, operatorFeld, strLastOperator);
    }

}
/**
 * 
 * @param {type} strID
 * @param {type} operatorFeld
 * @returns {undefined}
 */
function aktualisiereOperator(strID, operatorFeld, strLastOperator)
{
    //alert("aktualisiereOperator: " + strLastOperator);
    var div_operator = document.getElementById("div_operator_" + strID);
    div_operator.innerHTML = '<select name="select_operator_' + strID + '" class="ui fluid dropdown" id="select_operator_' + strID + '">';
    var select_operator = document.getElementById("select_operator_" + strID);

    for (var i = 0; i < operatorFeld.length; i++)
    {
        var opt = document.createElement('option');
        if (strLastOperator == operatorFeld[i])
        {
            //alert("Beide gleich");
            opt.setAttribute('selected', 'selected');
        }
        opt.value = operatorFeld[i];
        opt.innerHTML = operatorFeld[i];
        select_operator.appendChild(opt);
    }
    $('#select_operator_' + strID).dropdown();
}


function initialisiereCBFilter(strTyp, strID, strLastFilter)
{
    //alert("initialisiereCBFilter");
    if (map[strTyp.toUpperCase()] != undefined)
    {
        var div_filter = document.getElementById("div_filter_cb_" + strID);

        div_filter.innerHTML = '<select name="select_filter_cb_' + strID + '>" class="ui fluid dropdown" id="select_filter_cb_' + strID + '"></select>';
        var select_filter = document.getElementById("select_filter_cb_" + strID);

        var strHTMLFilter = map[strTyp.toUpperCase()];
        var strSplitFilter = strHTMLFilter.split(';');
        var opt1 = document.createElement('option');

        for (var i = 0; i < strSplitFilter.length; i++)
        {

            var opt = document.createElement('option');
            if (strLastFilter == strSplitFilter[i])
            {
                opt.setAttribute('selected', 'selected');
            }
            opt.value = strSplitFilter[i];
            opt.innerHTML = strSplitFilter[i];
            select_filter.appendChild(opt);
        }
        $('#select_filter_cb_' + strID).dropdown();
    }
}


function onChangeVerknuepfung(intIndexVonZeile)
{
    var div_verknuefung = document.getElementById("div_verknuepfung_" + intIndexVonZeile);
    var style = getStyle(div_verknuefung, 'backgroundColor');
    if (style == 'rgb(192, 5, 24)' || style == '#c00518')
    {
        div_verknuefung.style.backgroundColor = "white";
    }
}

function getStyle(el, styleProp)
{
    if (el.currentStyle)
        return el.currentStyle[styleProp];

    return document.defaultView.getComputedStyle(el, null)[styleProp];
}

//Wir aufgerufen wenn der Button vorschau gecklickt wird.
/**
 * Überprüft ob die letzte Verknüpfung leer ist, 
 * falls sie leer ist wird das div rot eingefärbt,
 * falls nicht....
 * @param {type} intZahler
 * @returns {undefined}
 */
function onVorschau(intZahler)
{
    var boolCorrect = true;
    for (var i = 1; i < intZahler; i++)
    {
        var strWertVonVerknupfungSelect = document.getElementById("select_verknuepfung_" + i).value;
        if (strWertVonVerknupfungSelect == "N/A")
        {
            boolCorrect = false;
        }
    }
    if (boolCorrect==true)
    {
        var strWertVonLetztemSelect = document.getElementById("select_verknuepfung_" + intZahler).value;
        if (strWertVonLetztemSelect != "N/A")
        {
            document.getElementById("modal_fehler").getElementsByTagName("p")[0].innerHTML = "Letzte Verknüpfung muss leer sein";
            $('#modal_fehler').modal('show');
//        $('.ui.dropdown').dropdown();
            return false;
        } else
        {
            return true;
        }
    } else
    {
        document.getElementById("modal_fehler").getElementsByTagName("p")[0].innerHTML = "Alle Verknüfungen, außer der Letzten, müssen gesetzt sein";
        $('#modal_fehler').modal('show');
        return false;
    }


}

function onActionSubmit(intZaehler, strButton)
{
    if (strButton == "minus")
    {
        intZaehler--;
    } else if (strButton == "vorschau")
    {
        var boolIsCorrect = onVorschau(intZaehler);
        if (boolIsCorrect == false)
        {
            return;
        }
    } else if (strButton == "erstelle_vorlage" || strButton == "erstelle_vorlage_2")
    {
        var boolIsCorrect = onErstelleNeueVorlage();
        if (boolIsCorrect == false)
        {
            return;
        }
    }
    for (var i = 1; i <= intZaehler; i++)
    {
        var div_element = document.getElementById("div_element_" + i);
        var strKlammerAuf_value = document.getElementById("select_klammer_" + i).value;
        var strTyp_value = document.getElementById("select_typ_" + i).value;
        var strOperator_value = document.getElementById("select_operator_" + i).value;
        var strBoxArt = document.getElementById("select_typ_" + i).value.split(";")[1];
        var strFilter_value = "";
        if (strBoxArt == "cb")
        {
            strFilter_value = document.getElementById("select_filter_cb_" + i).value;
        } else if (strBoxArt == "txt")
        {
            strFilter_value = document.getElementById("input_filter_" + i).value;
        } else if (strBoxArt == "datepicker")
        {
            strFilter_value = document.getElementById("input_filter_datepicker_" + i).value;
        }
        var strKlammerZu_value = document.getElementById("select_klammer_zu_" + i).value;
        var strVerknuefung_value = document.getElementById("select_verknuepfung_" + i).value;
        var strHTML = '<input type="hidden" name="hidden_element_data_' + i + '" value="' + strKlammerAuf_value + ";" + strTyp_value + ";" + strOperator_value + ";" + strFilter_value + ";" + strKlammerZu_value + ";" + strVerknuefung_value + '">';
        div_element.innerHTML = div_element.innerHTML + strHTML;
    }
    var strBezirk = document.getElementById("select_bezirk").value;
    var strAbschnitt = document.getElementById("select_abschnitt").value;
    var strFeuerwehr = document.getElementById("select_feuerwehr").value;
    var strBerechtigung = strBezirk + ";" + strAbschnitt + ";" + strFeuerwehr;
    document.getElementById("hidden_berechtigungs_info").value = strBerechtigung;

    strHTML = '<input type="hidden" name="hidden_action" value="' + strButton + '">';
    div_element.innerHTML = div_element.innerHTML + strHTML;
    document.form_plus_minus_vorschau.submit();
}

function onErstelleNeueVorlage()
{
    var strVorlageName = document.getElementById("input_neueVorlage").value;
    if (strVorlageName != "")
    {
        document.getElementById("hidden_vorlage_name").value = strVorlageName;
        return true;
    } else
    {
        alert("Eingabefeld leer");
        return false;

    }
}


/*
 * Leitet zum PDFServlet weiter um eine PDF zu erstellen, anzuzeigen und herunterzuladen
 */
function saveDataForPDF()
{
    var strTable = document.getElementById("div_table").innerHTML;
    document.getElementById("hidden_pdfData").value = strTable;
    document.formPDF.submit();
}

/*
 * Leitet zum CSVServlet weiter um eine CSV zu erstellen und herunterzuladen
 */
function saveDataForCSV()
{
    var strTable = document.getElementById("div_table").innerHTML;
    document.getElementById("hidden_CSVData").value = strTable;
    document.formCSV.submit();
}

function showErstellenModal()
{
    $('#modal_erstelle_vorlage').modal('show');
}

function showLadenModal()
{
    if (document.getElementById("modal_lade_vorlage") != null)
    {
        $('#modal_lade_vorlage').modal('show');
    } else
    {
        document.getElementById("modal_fehler").getElementsByTagName("p")[0].innerHTML = "Keine Vorlagen gefunden";
        $('#modal_fehler').modal('show');
    }
}

function showLoeschenModal()
{
    if (document.getElementById("modal_loesche_vorlage") != null)
    {
        $('#modal_loesche_vorlage').modal('show');
    } else
    {
        document.getElementById("modal_fehler").getElementsByTagName("p")[0].innerHTML = "Keine Vorlagen gefunden";
        $('#modal_fehler').modal('show');
    }
}



function abschittChanged(select_abschnitt, strLetzteFW)
{
    //alert("Abschnitt_value: "+select_abschnitt.value);
    if (select_abschnitt.value != -1 && select_abschnitt.value != -2)
    {
        var strFeuerwehrOptions = document.getElementById("div_" + select_abschnitt.value).innerHTML;
        if (strLetzteFW != null)
        {
            strFeuerwehrOptions = strFeuerwehrOptions.replace('value="' + strLetzteFW + '"', 'value="' + strLetzteFW + '" selected');
        }
        document.getElementById("fieldset_feuerwehr").innerHTML = '<legend><b>Feuerwehr</b></legend><select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr"></select>';
        if (strFeuerwehrOptions.split('<option').length > 1)
        {
            document.getElementById("select_feuerwehr").innerHTML = "<option value='-2'>Alle Feuerwehren</option>" + strFeuerwehrOptions;
        } else
        {
            document.getElementById("select_feuerwehr").innerHTML = strFeuerwehrOptions;
        }

        $('#select_feuerwehr').dropdown();
        //alert(strFeuerwehrOptions);
    } else if (select_abschnitt.value == -2)
    {
        document.getElementById("fieldset_feuerwehr").innerHTML = '<legend><b>Feuerwehr</b></legend><select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr"></select>';
        document.getElementById("select_feuerwehr").innerHTML = "<option value='-2'>Alle Feuerwehren</option>"
        $('#select_feuerwehr').dropdown();
        fixDropdowns("select_feuerwehr");

    }
}

function bezirkChanged(select_bezirk, strLetzteAbschnitt)
{
    //alert("Bezirk_value: "+select_bezirk.value);
    if (select_bezirk.value != -1 && select_bezirk.value != -2)
    {
        //alert("IN bezirk changed");
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
        //alert(strAbschnittOptions);
    } else if (select_bezirk.value == -2)
    {
        document.getElementById("fieldset_abschnitt").innerHTML = '<legend><b>Abschnitt</b></legend><select name="select_abschnitt" class="ui fluid dropdown" id="select_abschnitt" onchange="abschittChanged(this)"></select>';
        document.getElementById("select_abschnitt").innerHTML = "<option value='-2'>Alle Abschnitte</option>"
        $('#select_abschnitt').dropdown();
        fixDropdowns("select_feuerwehr");
    }
}

function fixDropdowns(id)
{
    //alert("Fix: "+id);
    var lenght = document.getElementById(id).getElementsByTagName("option").length;
    if (lenght == 1)
    {
        //alert("Add disabled");
        $("#" + id).parent("div").addClass("disabled");
    } else
    {
        //alert("remove disabled");
        $("#" + id).parent("div").removeClass("disabled");
    }
}

function zuVordefiniertWeiterleiten()
{
    var strBezirk = document.getElementById("select_bezirk").value;
    var strAbschnitt = document.getElementById("select_abschnitt").value;
    var strFeuerwehr = document.getElementById("select_feuerwehr").value;
    var strBerechtigung = strBezirk + ";" + strAbschnitt + ";" + strFeuerwehr;
    document.getElementById("hidden_berechtigungs_info_2").value = strBerechtigung;
    document.form_vordefiniert.submit();
}



