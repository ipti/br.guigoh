/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
locale = $("localeAcronym").val();
$(document).ready(function() {/*
    if(locale == "ptBR"){
        inputLabelValue = "Um título que descreva o assunto tratado no seu tópico...";
        closeButton = "Fechar";
    }
    else if(locale == "enUS"){
        inputLabelValue = "A title that describes the subject matter in your topic..."
        closeButton = "Close";
    }
    else if(locale == "frFR"){
        inputLabelValue = "Un titre qui décrit la matière dans votre sujet..."
        closeButton = "Proche";
    }
    */
    $(document).on('click', '.tag_submit', function(){  
        $('.tag_input').css("color","#ccc");
        $('.tag_input').val("#");        
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
        buttons:[{text: closeButton,click:function() {$(this).dialog("close");}
            }],
        close: function(ev, ui) {
            $(this).hide();
        }
    });
 
    $('.attach_button').click(function() {
        $("#dialog-form").dialog('open');
        return false;
    });
    
    $( ".tag_input" ).autocomplete({
        source: function( request, response ) {
            $.ajax({
                url: "/webresources/primata/tags",
                dataType: "json",
                data: {
                    "text": request.term,
                    "theme_id":theme_id
                },
                success: function( data ) {
                    response( $.map( data, function( item ) {
                        return {
                            label: item.name,
                            value: item.name
                        }
                    }));
                }
            });
        },
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".tag_input" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".tag_input" ).val(ui.item.label);
            return false;
        }
    });
    
    
}

