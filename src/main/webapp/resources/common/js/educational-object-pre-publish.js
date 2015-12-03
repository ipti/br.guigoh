$(document).ready(function () {
    $(".menu-icon-two").parent().addClass("active");

    $(".educational_object").last().css("border-bottom", "0px solid #d3d3d3");
});

$('#submit-educational-object > span').mousemove(function (e) {
    $('#visitorPermissionWarning').css({position: 'absolute', top: e.pageY + 10 + 'px', left: e.pageX + 'px'}).show();
});

$('#submit-educational-object > span').mouseleave(function (e) {
    $('#visitorPermissionWarning').hide();
});