/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$('.ui.dropdown').dropdown();

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
    document.getElementById("div_daten").getElementsByTagName("h2")[0].innerHTML = strBerichtname;
    document.getElementById("input_hidden").value = strBerichtname;
    document.getElementById("div_table").innerHTML = strTable;

}





function saveDataForPDF()
{
    var strTable = document.getElementById("div_table").innerHTML;
    var strName = document.getElementById("h2_bericht").innerHTML;
    document.getElementById("hidden_pdfData").value = strName+"###"+strTable;
    document.formPDF.submit();
}

function saveDataForCSV()
{
    var strTable = document.getElementById("div_table").innerHTML;
    var strName = document.getElementById("h2_bericht").innerHTML;
    document.getElementById("hidden_CSVData").value = strName+"###"+strTable;
    document.formCSV.submit();
}


