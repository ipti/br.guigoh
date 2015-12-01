$(document).ready(function () {
    $(".menu-icon-two").parent().addClass("active");

    $(".educational_object").last().css("border-bottom", "0px solid #d3d3d3");
});

$('#visitorPermissionWarning, #submit-educational-object > span').hover(function (e) {
    var top = e.pageY + 'px';
    var left = e.pageX + 'px'
    $('#visitorPermissionWarning').css({position: 'absolute', top: top, left: left}).show();
},
        function () {
            $('#visitorPermissionWarning').hide();
        });
