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
            var dt = new Date(dateUmwandeln(selected));
            if(selected!=null)
            {
                document.getElementById("div_remove_von_datum").style.display="block";
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
            if(selected!=null)
            {
                document.getElementById("div_remove_bis_datum").style.display="block";
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
        document.getElementById("div_kennzeichen").style.display = "none";
    } else if (intTypeOfDateUI == 1)
    {
        document.getElementById("div_input_von_datum").style.display = "none";
        document.getElementById("div_input_bis_datum").style.display = "none";
        document.getElementById("div_kein_datum_1").style.display = "block";
        document.getElementById("div_kein_datum_2").style.display = "none";
        document.getElementById("div_select_jahr").style.display = "block";
        document.getElementById("div_kennzeichen").style.display = "none";
    } else if (intTypeOfDateUI == 2)
    {
        document.getElementById("div_input_von_datum").style.display = "block";
        document.getElementById("div_input_bis_datum").style.display = "block";
        document.getElementById("div_kein_datum_1").style.display = "none";
        document.getElementById("div_kein_datum_2").style.display = "none";
        document.getElementById("div_select_jahr").style.display = "none";
        document.getElementById("div_kennzeichen").style.display = "none";
    } else if (intTypeOfDateUI == 3)
    {
        document.getElementById("div_input_von_datum").style.display = "block";
        document.getElementById("div_input_bis_datum").style.display = "block";
        document.getElementById("div_kein_datum_1").style.display = "none";
        document.getElementById("div_kein_datum_2").style.display = "none";
        document.getElementById("div_select_jahr").style.display = "none";
        document.getElementById("div_kennzeichen").style.display = "block";
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
    
    if(document.getElementById("div_zusatzDaten")!=null)
    {
        document.getElementById("div_zusatzDaten").remove();
    }
    for (index = 0; index < liItems.length; ++index) {
        liItems[index].className = "item";
    }
    item.className = item.className + " active";
    document.getElementById("div_csv_pdf").style.display = "none";
    var strTable = item.getElementsByTagName("div")[0].innerHTML;
    var strBerichtname = item.getElementsByTagName("span")[0].innerHTML;
    var intTypeOfDateUI = item.getElementsByTagName("div")[1].innerHTML;
    onChangeTypeOfDateUI(intTypeOfDateUI);
    document.getElementById("div_daten").getElementsByTagName("h2")[0].innerHTML = strBerichtname;
    document.getElementById("input_hidden").value = strBerichtname;
    document.getElementById("div_table").innerHTML = strTable;
    if(document.getElementById("div_zusatzDaten")==null&&strBerichtname=='Kursstatistik')
    {
        document.getElementById("div_kursstatistik").style.display="block";
    }else
    {
        document.getElementById("div_kursstatistik").style.display="none";
    }
    
    if(document.getElementById("div_zusatzDaten")==null&&strBerichtname=='Digitales Fahrtenbuch')
    {
        document.getElementById("div_fahrtenbuch").style.display="block";
    }else
    {
        document.getElementById("div_fahrtenbuch").style.display="none";
    }
}




/*
 * Leitet zum PDFServlet weiter um eine PDF zu erstellen, anzuzeigen und herunterzuladen
 */
function saveDataForPDF()
{
    var strTable = document.getElementById("div_table").innerHTML;
    var strName = document.getElementById("h2_bericht").innerHTML;
    if(document.getElementById("div_zusatzDaten")!=null)
    {
        var strZusatzDaten = document.getElementById("div_zusatzDaten").innerHTML;
        document.getElementById("hidden_pdfData").value = strName + "###" + strTable + "###" + strZusatzDaten;
    }else
    {
        document.getElementById("hidden_pdfData").value = strName + "###" + strTable;
    }
    
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
    if (select_abschnitt.value != -1&& select_abschnitt.value != -2)
    {
        var strFeuerwehrOptions = document.getElementById("div_" + select_abschnitt.value).innerHTML;
        document.getElementById("fieldset_feuerwehr").innerHTML = '<legend><b>Feuerwehr</b></legend><select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr"></select>';
        if(strFeuerwehrOptions.split('<option').length>1)
        {
            document.getElementById("select_feuerwehr").innerHTML = "<option value='-2'>Alle Feuerwehren</option>"+ strFeuerwehrOptions;
        }else
        {
            document.getElementById("select_feuerwehr").innerHTML = strFeuerwehrOptions;
        }
        
        $('#select_feuerwehr').dropdown();
        //alert(strFeuerwehrOptions);
    }else if(select_abschnitt.value == -2)
    {
        document.getElementById("fieldset_feuerwehr").innerHTML = '<legend><b>Feuerwehr</b></legend><select name="select_feuerwehr" class="ui fluid dropdown" id="select_feuerwehr"></select>';
        document.getElementById("select_feuerwehr").innerHTML = "<option value='-2'>Alle Feuerwehren</option>"
        $('#select_feuerwehr').dropdown();
        fixDropdowns("select_feuerwehr");
        
    }
}

function bezirkChanged(select_bezirk)
{
    //alert("Bezirk_value: "+select_bezirk.value);
    if (select_bezirk.value != -1&& select_bezirk.value!=-2)
    {
        //alert("IN bezirk changed");
        var strAbschnittOptions = document.getElementById("div_" + select_bezirk.value).innerHTML;
        document.getElementById("fieldset_abschnitt").innerHTML = '<legend><b>Abschnitt</b></legend><select name="select_abschnitt" class="ui fluid dropdown" id="select_abschnitt" onchange="abschittChanged(this)"></select>';
        if(strAbschnittOptions.split('<option').length>1)
        {
            document.getElementById("select_abschnitt").innerHTML = "<option value='-2'>Alle Abschnitte</option>"+ strAbschnittOptions;
        }else
        {
            document.getElementById("select_abschnitt").innerHTML = strAbschnittOptions;
        }
        $('#select_abschnitt').dropdown();
        //alert(strAbschnittOptions);
    }else if(select_bezirk.value == -2)
    {
        document.getElementById("fieldset_abschnitt").innerHTML = '<legend><b>Abschnitt</b></legend><select name="select_abschnitt" class="ui fluid dropdown" id="select_abschnitt" onchange="abschittChanged(this)"></select>';
        document.getElementById("select_abschnitt").innerHTML = "<option value='-2'>Alle Abschnitte</option>"
        $('#select_abschnitt').dropdown();
        fixDropdowns("select_feuerwehr");
    }
}
//select_bezirk
function fixDropdowns(id)
{
    //alert("Fix: "+id);
    var lenght = document.getElementById(id).getElementsByTagName("option").length;
    if(lenght==1)
    {
        //alert("Add disabled");
        $("#"+id).parent("div").addClass("disabled");
    }else
    {
        //alert("remove disabled");
        $("#"+id).parent("div").removeClass("disabled");
    }
}


function removeDateAndSetDivHidden(idDiv,idDatepicker)
{
    document.getElementById(idDiv).style.display="none";
    document.getElementById(idDatepicker).value="";
    if(idDatepicker.contains("von"))
    {
        $("#input_bis_datum").datepicker("option", "minDate", null);
    }else
    {
        $("#input_von_datum").datepicker("option", "maxDate", null);
    }
}