$(document).ready(function() {
    var width = $(".progress").width();
    var page = 1;
    $(document).on('click', '.button_back', function(){
        $(".page-number").text(--page);
        $(".progress").width(width * $(".page-number").text())
        $(".button_next").show();
        $(".button_submit").hide();
        if($(".page-number").text() == 1){
            $(".button_back").hide();
        }
    });
    $(document).on('click', '.button_next', function(){
        $(".page-number").text(++page);
        $(".progress").width(width * $(".page-number").text());
        if($(".page-number").text() != 1){
            $(".button_back").show();
        }
        if($(".page-number").text() == 7){
            $(".button_next").hide();
            $(".button_submit").show();
        }
    });
});


