$(document).ready(function(){
    $(document).on('click','.reply_attach_button', function() {
        $(".file").click();
    });
    
    $(document).on('click', '.reply_message_container', function() {
        if ($('.new_reply').val() === "") {
            $('.new_reply_warning').css("display", "block");
        }else{
            $('.new_body_warning').hide();
            $('.submit').click();
        }
    });
});