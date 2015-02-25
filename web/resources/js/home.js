$(document).ready(function () {
    var left = $(".left_column").height();
    var middle = $(".middle_column").height();
    var right = $(".right_column").height();
    var educationalObjectName = $(".educational_object_name");
    if (right > middle) {
        $(".middle_column").css("height", right);
    } else if (left > middle) {
        $(".middle_column").css("height", left);
    }

    //shortenName();

    $(".interest_theme").last().css("border-bottom", "1px solid #ABABAB");
    $(".text_feed").last().css("border-bottom", "0");
    //$(".educational_object").last().css("border-bottom", "0");

    function shortenName() {
        $.each(educationalObjectName, function () {
            var nameTemp = $(this).text();
            if (nameTemp.length > 28) {
                $(this).text(nameTemp.substring(0, 28) + "...");
            }
        });
    }       
    
//    $(document).on("click", "#show_more", function () {
//        $.ajax({
//            url: "../primata/home.xhtml",            
//            success: function () {
//                if ($("#show_more").length == 0){
//                    $(".educational_object").last().css("border-bottom", "0");
//                }                
//            }
//        });
//    })
});