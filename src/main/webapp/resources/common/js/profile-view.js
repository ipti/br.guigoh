var image;

$(document).ready(function () {

    $("#profile-icon-mail span").text(changeNameLength($("#profile-icon-mail span").text(), 20));

});

$('.edit-photo').click(function () {
    $('#browse-photo').click();
});

$('#browse-photo').change(function (e) {
    for (var i = 0; i < e.originalEvent.target.files.length; i++) {
        var file = e.originalEvent.target.files[i];
        var reader = new FileReader();
        reader.onloadend = function () {
            $('#browse-photo').val("");
            document.getElementById("open-image-cropping-modal").click();
            $('.original-uploaded-image').attr("src", reader.result);
            var imageWidth = $('.original-uploaded-image').width();
            var imageHeight = $('.original-uploaded-image').height();
            var x = imageWidth / 2 - 125 / 2;
            var y = imageHeight / 2 - 125 / 2;
            $('.original-uploaded-image').Jcrop({
                setSelect: [x, y, x + 125, y + 125],
                minSize: [125, 125],
                aspectRatio: 1,
            });
        }
        reader.readAsDataURL(file);
    }
});

$('#image-cropping-modal a').click(function(){
    $('.original-uploaded-image').data('Jcrop').destroy();
    $('.original-uploaded-image').removeAttr('style');
})

$('#profile-tab-about').click(function () {
    $('#profile-container-about').show();
    $('#profile-container-objects').hide();
    $('#profile-container-resume').hide();
    $('#profile-tab-about').addClass('active');
    $('#profile-tab-objects').removeClass('active');
    $('#profile-tab-resume').removeClass('active');
});

$('#profile-tab-objects').click(function () {
    $('#profile-container-about').hide();
    $('#profile-container-objects').show();
    $('#profile-container-resume').hide();
    $('#profile-tab-about').removeClass('active');
    $('#profile-tab-objects').addClass('active');
    $('#profile-tab-resume').removeClass('active');
});

$('#profile-tab-resume').click(function () {
    $('#profile-container-about').hide();
    $('#profile-container-objects').hide();
    $('#profile-container-resume').show();
    $('#profile-tab-about').removeClass('active');
    $('#profile-tab-objects').removeClass('active');
    $('#profile-tab-resume').addClass('active');
});