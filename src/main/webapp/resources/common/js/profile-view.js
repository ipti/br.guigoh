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
            if (file.type.split("/")[0] === 'image') {
                $('.original-uploaded-image').show();
                $('.original-uploaded-image').attr("src", reader.result);
                var imageWidth = $('.original-uploaded-image').width();
                var imageHeight = $('.original-uploaded-image').height();
                if (imageWidth > 125 && imageHeight > 125) {
                    $('.image-error').text("");
                    if (imageWidth > imageHeight){
                        $('.original-uploaded-image').css("max-width", "750px");
                    } else {
                        $('.original-uploaded-image').css("max-height", "500px");
                    }
                    document.getElementById("open-image-cropping-modal").click();
                    var x = 0;
                    var y = 0;
                    $('.original-uploaded-image').Jcrop({
                        setSelect: [x, y, x + 125, y + 125],
                        minSize: [125, 125],
                        aspectRatio: 1,
                        onChange: getCoords,
                        onSelect: getCoords
                    });
                } else {
                    $('.image-error').text("Escolha uma imagem de tamanho mínimo de 125x125.");
                    $('#browse-photo').val("");
                }
            } else {
                $('.image-error').text("Apenas imagens são aceitas.");
            }
        }
        reader.readAsDataURL(file);
    }
});

function getCoords(c)
{
    // fix crop size: find ratio dividing current per real size
    var ratioW = $('.original-uploaded-image')[0].naturalWidth / $('.original-uploaded-image').width();
    var ratioH = $('.original-uploaded-image')[0].naturalHeight / $('.original-uploaded-image').height();
    var currentRatio = Math.min(ratioW, ratioH);
    $('#coord-x').val(Math.round(c.x * currentRatio));
    $('#coord-y').val(Math.round(c.y * currentRatio));
    $('#coord-w').val(Math.round(c.w * currentRatio));
    $('#coord-h').val(Math.round(c.h * currentRatio));

}

$('.cut-photo').click(function () {
    $('.upload-photo').click();
})

$('.close-image-cropping-modal, .image-cropping-button.cancel').click(function () {
    $('.original-uploaded-image').data('Jcrop').destroy();
    setTimeout(function(){
        $('.original-uploaded-image').removeAttr('style');
        $('#browse-photo').val("");
    }, 400);
    
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

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        if ($(data.source).hasClass("upload-photo")) {
            document.getElementById("close-image-cropping-modal").click();
        }
    }
});