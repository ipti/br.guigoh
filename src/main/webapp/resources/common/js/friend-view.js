$(document).ready(function () {
    $(".menu-icon-four").parent().addClass("active");
    
    $.each($(".user-name a, .user-job"), function () {
        var limit = $(this).hasClass("user-job") ? 25 : 22;
        $(this).text(changeNameLength($(this).text(), limit));
    });
});

$(".friend-search").on('keypress', function (e) {
    if (e.keyCode == 13) {
        e.preventDefault();
    }
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        $.each($(".user-name a, .user-job"), function () {
            var limit = $(this).hasClass("user-job") ? 25 : 22;
            $(this).text(changeNameLength($(this).text(), limit));
        });
    }
});

var friendIndex;

$(document).on('click', function (e) {
    friendIndex = $(e.target).closest("div").siblings(".user-menu-box").attr("friendindex");

    if ($(e.target).closest($(".user-arrow")).length > 0) {
        //$(".user-menu-box").hide();
        $(".user-menu-box[friendindex = " + friendIndex + "]").toggle();
    } else if ($(e.target).closest(".user-menu-box").length === 0) {
        $(".user-menu-box").hide();
    }
    console.log($(e.target).closest($(".user-arrow")).length);
});

$(".user-menu-box a").click(function () {
    $(".user-menu-box[friendindex = " + friendIndex + "]").toggle();
});
