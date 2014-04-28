var locale = $("localeAcronym").val();

$(document).ready(function(){
    loadAll();
    jsf.ajax.addOnEvent(function(data){
        if (data.status === 'success') {
            loadAll();
        }
    });
});

function loadAll(){
    var closeButton = "";
    if(locale == "ptBR"){closeButton = "Fechar"}
    else if(locale == "enUS"){closeButton = "Close"}
    else if(locale == "frFR"){closeButton = "Proche"}
    $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 400,
        width: 600,
        modal: true,
        overlay: {
            backgroundColor: "#000", 
            opacity: 0.5
        },
        buttons:[{text: closeButton,click:function() {$(this).dialog("close");}
            }],
        close: function(ev, ui) {
            $(this).hide();
        }
    });
 
    $('.reply_attach_button').click(function() {
        $("#dialog-form").dialog('open');
        return false;
    });
}



