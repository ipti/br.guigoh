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

$(document).on("click", ".user-chat", function(){
    var id = $(this).closest(".user").find(".social-profile-id").text();
    var name = $(this).closest(".user").find(".social-profile-name").text();    
    openMessengerBox(id, name);
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        $.each($(".user-name a, .user-job"), function () {
            var limit = $(this).hasClass("user-job") ? 25 : 22;
            $(this).text(changeNameLength($(this).text(), limit));
        });
        if ($(data.source).hasClass("accept-user") || $(data.source).hasClass("reject-user")) {
            $('.refresh-pending-friends').click();
        }
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
});

$(".user-menu-box a").click(function () {
    $(".user-menu-box[friendindex = " + friendIndex + "]").toggle();
});
