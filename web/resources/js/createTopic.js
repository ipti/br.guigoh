/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    $(document).on('click','.attach_button',function() {
        $(".file").click();
    });

    $(document).on('click', '.post_topic_button_container', function() {
        var validate = true;
        if ($('.new_title').val() === "") {
            $('.new_title_warning').css("display", "block");
            validate = false;
        }else{
            $('.new_title_warning').hide();
        }
        if ($('.new_body').val() === "") {
            $('.new_body_warning').css("display", "block");
            validate = false;
        }else{
            $('.new_body_warning').hide();
        }
        if (validate) {
            $('.submit').click();
        }
    });
});

