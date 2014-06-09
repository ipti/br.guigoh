$(document).ready(function() {
    var width = $(".progress").width();
    var page = 1;
    $(document).on('click', '.button_back', function(){
        $(".form"+page+"_body").hide();
        $(".page-number").text(--page);
        $(".form"+page+"_body").show();
        $(".progress").width(width * $(".page-number").text())
        $(".button_next").show();
        $(".button_submit").hide();
        if($(".page-number").text() == 1){
            $(".button_back").hide();
        }
    });
    $(document).on('click', '.button_next', function(){
        var validate = true;
        //Form specifics
        if(page == 2){
            if ($(".educational_object_name").val() == ""){
                validate = false;
                $(".educational_object_name_warning").css("display","block");
            }
            if ($('input[type=radio]:checked').length == 0) {
                validate = false;
                $(".educational_object_theme_warning").css("display","block");
            }
            if ($(".educational_object_tags").val() == ""){
                validate = false;
                $(".educational_object_tags_warning").css("display","block");
            }
        }
        //End
        if (validate == true){
            $(".form"+page+"_body").hide();
            $(".page-number").text(++page);
            $(".form"+page+"_body").show();
            $(".progress").width(width * $(".page-number").text());
            if($(".page-number").text() != 1){
                $(".button_back").show();
            }
            if($(".page-number").text() == 4){
                $(".button_next").hide();
                $(".button_submit").show();
            }
            $(".educational_object_name_warning").hide();
            $(".educational_object_theme_warning").hide();
            $(".educational_object_tags_warning").hide();
        }
        
        
    });
});


