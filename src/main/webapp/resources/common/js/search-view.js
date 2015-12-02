$(document).ready(function () {
    $(".menu-icon-five").parent().addClass("active");

    $(".general-search input").focus();
});

$(document).on("mousemove", ".user-chat.disabled, .user-friend-status", function (e) {
    if (!$(e.target).parent().hasClass("friend-situation") || ($(e.target).parent().hasClass("friend-situation") && $(e.target).parent().is("span"))) {
        $('#visitorPermissionWarning').css({position: 'absolute', top: e.pageY + 10 + 'px', left: e.pageX + 'px'}).show();
    }
});

$(document).on("mouseleave", ".user-chat.disabled, .user-friend-status", function (e) {
    $('#visitorPermissionWarning').hide();
});

$(".general-search").on('keypress', function (e) {
    if (e.keyCode == 13) {
        e.preventDefault();
    }
});

$(document).on("click", ".user-chat", function () {
    if (!$(this).hasClass("disabled")) {
        var id = $(this).closest(".result-box").find(".social-profile-id").text();
        var name = $(this).closest(".result-box").find(".social-profile-name").text();
        openMessengerBox(id, name);
    }
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        $.each($(".result-name a, .user-job"), function () {
            var limit = $(this).hasClass("user-job") ? 25 : 22;
            $(this).text(changeNameLength($(this).text(), limit));
        });
    }
});