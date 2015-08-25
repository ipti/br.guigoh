$(document).ready(function(){
    $(".menu-icon-five").parent().addClass("active");
});

$(".general-search").on('keypress', function (e) {
        if (e.keyCode == 13) {
            e.preventDefault();
        }
    });