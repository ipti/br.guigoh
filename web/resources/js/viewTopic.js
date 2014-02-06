navigator.userLanguage = 'pt';

$(document).ready(function(){
    $(document).on('focus', '.general_search_input', function(){
        if($('.general_search_input').val() == "pesquise tópicos ou objetos educacionais..."){
            $('.general_search_input').val("");
        }
    });
    $(document).on('focusout', '.general_search_input', function(){
        if($('.general_search_input').val() == ""){
            $('.general_search_input').val("pesquise tópicos ou objetos educacionais...");
        }
    });
    loadAll();
    jsf.ajax.addOnEvent(function(data){
        if (data.status === 'success') {
            loadAll();
        }
    });
});

function loadAll(){
   
    $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 400,
        width: 600,
        modal: true,
        overlay: {
            backgroundColor: "#000", 
            opacity: 0.5
        },
        buttons:{
            "Close": function() {
                $(this).dialog("close");
            }
        },
        close: function(ev, ui) {
            $(this).hide();
        }
    });
 
    $('.reply_attach_button').click(function() {
        $("#dialog-form").dialog('open');
        return false;
    });
}



