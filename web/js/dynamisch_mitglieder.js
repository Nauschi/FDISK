/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$('.ui.dropdown').dropdown();


$(function () {
    $("#input_filter_date").datepicker({
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
});


function onTypChanged(select_typ)
{
    var strTyp = select_typ.value;
    var strID = select_typ.getAttribute('id');
    alert(strID);
    var strBoxArt = strTyp.split(";")[1];
    if(strBoxArt == "cb")
    {
        document.getElementById("div_filter_cb_1").style.display = "block";
        document.getElementById("div_filter_txt_1").style.display = "none";
        document.getElementById("div_filter_datepicker_1").style.display = "none";
    }else if(strBoxArt == "txt")
    {
        document.getElementById("div_filter_cb_1").style.display = "none";
        document.getElementById("div_filter_txt_1").style.display = "block";
        document.getElementById("div_filter_datepicker_1").style.display = "none";
    }else if(strBoxArt == "datepicker")
    {
        document.getElementById("div_filter_cb_1").style.display = "none";
        document.getElementById("div_filter_txt_1").style.display = "none";
        document.getElementById("div_filter_datepicker_1").style.display = "block";
    }
    
}

