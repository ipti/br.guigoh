$(document).ready(function(){
    var left = $(".left_column").height();
    var middle = $(".middle_column").height();
    var right = $(".right_column").height();
    if(right > middle){
        $(".middle_column").css("height", right);
    }else if (left > middle){
        $(".middle_column").css("height", left);
    }
});