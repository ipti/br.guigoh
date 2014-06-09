$(document).ready(function() {
    var width = $(".progress").width();
    var page = 1;
    $(".educational_object_author_phone").mask("(99) 9999-9999");
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
        if(page == 3){
            if ($(".author_added").length == 0){
                validate = false;
                $(".educational_object_author_warning2").css("display","block");
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
            $(".educational_object_author_warning1").hide();
            $(".educational_object_author_warning2").hide();
        }
       
    });
    $(document).on('click', '.button_add', function(){
        if (!String($(".educational_object_author_name").val()).match("[a-zA-Z ]{3,40}")){
            $(".educational_object_author_warning1").text("Digite um campo 'Nome' válido");
            $(".educational_object_author_warning1").css("display","block");
        }else if (!String($(".educational_object_author_email").val()).match("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")){
            $(".educational_object_author_warning1").text("Digite um campo 'E-mail' válido");
            $(".educational_object_author_warning1").css("display","block");
        }else if (!String($(".educational_object_author_phone").val()).match("\\(\\d{2}\\) \\d{4}-\\d{4}")){
            $(".educational_object_author_warning1").text("Digite um campo 'Phone' válido");
            $(".educational_object_author_warning1").css("display","block");
        }else if (!String($(".educational_object_author_site").val()).match("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?")){
            $(".educational_object_author_warning1").text("Digite um campo 'Site' válido");
            $(".educational_object_author_warning1").css("display","block");
        }else{
            $(".educational_object_author_warning1").hide();
        }
    });
});


