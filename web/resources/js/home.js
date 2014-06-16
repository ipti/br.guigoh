$(document).ready(function(){
    var left = $(".left_column").height();
    var middle = $(".middle_column").height();
    var right = $(".right_column").height();
    var educationalObjectName = $(".educational_object_name");
    if(right > middle){
        $(".middle_column").css("height", right);
    }else if (left > middle){
        $(".middle_column").css("height", left);
    }
    $.each(educationalObjectName, function(){
        var nameTemp = $(this).text();
        if (nameTemp.length > 28){
            $(this).text(nameTemp.substring(0, 28)+"...");
        }
    });
});