$(document).ready(function () {
    $(".menu-icon-five").parent().addClass("active");
});

$(".general-search").on('keypress', function (e) {
    if (e.keyCode == 13) {
        e.preventDefault();
    }
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        $.each($(".result-name a, .user-job"), function(){
            var limit = $(this).hasClass("user-job") ? 25 : 22;
            $(this).text(changeNameLength($(this).text(), limit));
        });
    }
});