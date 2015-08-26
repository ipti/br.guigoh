$(document).ready(function(){
    $(".menu-icon-four").parent().addClass("active");
});

$(".friend-search").on('keypress', function (e) {
    if (e.keyCode == 13) {
        e.preventDefault();
    }
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        $.each($(".user-name a, .user-job"), function(){
            var limit = $(this).hasClass("user-job") ? 25 : 22;
            $(this).text(changeNameLength($(this).text(), limit));
        });
    }
});

$(document).on('click', function (e) {
    if ($(e.target).closest($(".user-arrow")).length > 0){
        $(".user-menu-box").toggle();
    } else if ($(e.target).closest(".user-menu-box").length === 0) {
        $(".user-menu-box").hide();
    }
});

$(".user-menu-box a").click(function(){
    $(".user-menu-box").toggle();
});
