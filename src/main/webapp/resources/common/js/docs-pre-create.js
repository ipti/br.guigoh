$(document).ready(function () {
    $(".menu-icon-four").parent().addClass("active");
});

$('#create-new-doc > span').mousemove(function (e) {
    $('#visitorPermissionWarning').css({position: 'absolute', top: e.pageY + 10 + 'px', left: e.pageX + 'px'}).show();
});

$('#create-new-doc > span').mouseleave(function (e) {
    $('#visitorPermissionWarning').hide();
});

$(document).on("click", ".doc-status", function () {
    if ($(this).find("i").hasClass("hidden")) {
        $(this).find("i").removeClass("hidden");
    } else {
        $(this).find("i").addClass("hidden");
    }
});