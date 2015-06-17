$(document).ready(function(){
    $("#phone").mask("(99) 9999-9999");
    $('.contacts_box_block').on('click',sendMessage);
    $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 600,
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
});


function sendMessage(){
    var socialProfileId = $(this).attr('socialProfileId');
    $("#dialog-form").dialog('open');
    $('#send_message').on('click',function(){
        if($('#business_name').val() != "" && $('#e_mail').val() != "" && $('#phone').val() != "" && $('#message').val() != ""){
            var businessName = $('#business_name').val();
            var email = $('#e_mail').val();
            var phone = $('#phone').val();
            var message = $('#message').val();
            $.ajax({
                type:"GET",
                url:"/webresources/sendMessageCurriculum",
                data: {
                    "socialProfileId":socialProfileId,
                    "businessName":businessName,
                    "email":email,
                    "phone":phone,
                    "message":message
                },
                dataType: 'json',
                success:function(success){
                    $("#dialog-form").dialog("close");
                    $("#message_alert").append("<strong>Mensagem enviada com sucesso! Aguarde contato do usuário.</strong>");
                }
        
            });
        }
    });
    return false;
}




