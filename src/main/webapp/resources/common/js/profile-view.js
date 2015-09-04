var edit;

$(document).ready(function () {
    $("#profile-icon-mail span").text(changeNameLength($("#profile-icon-mail span").text(), 20));
});

$('.open-chat').click(function(){
    var id = $("#social-profile-id").val();
    var name = $("#social-profile-name").val();
    openMessengerBox(id, name);
});

$('.edit-photo').click(function () {
    edit = ($(this).hasClass("cover-photo")) ? "cover" : "photo";
    $('#browse-photo').click();
});

$('#browse-photo').change(function (e) {
    var widthMin = 0;
    var heightMin = 0;
    if (edit === "photo") {
        widthMin = 125;
        heightMin = 125;
    } else if (edit === "cover") {
        widthMin = 960 / 2;
        heightMin = 200 / 2;
    }
    for (var i = 0; i < e.originalEvent.target.files.length; i++) {
        var file = e.originalEvent.target.files[i];
        var reader = new FileReader();
        reader.onloadend = function () {
            if (file.type.split("/")[0] === 'image') {
                $('.original-uploaded-image').show();
                $('.original-uploaded-image').attr("src", reader.result).load(function() {
                    var imageWidth = $(this).width();
                    var imageHeight = $(this).height();
                    if (imageWidth > widthMin && imageHeight > heightMin) {
                        $('.image-error').text("");
                        if (imageWidth > imageHeight) {
                            $(this).css("max-width", "750px");
                        } else {
                            $(this).css("max-height", "500px");
                        }
                        document.getElementById("open-image-cropping-modal").click();
                        $(this).Jcrop({
                            setSelect: [0, 0, widthMin, heightMin],
                            minSize: [widthMin, heightMin],
                            aspectRatio: widthMin / heightMin,
                            onSelect: function (c) {
                                getTrackerCoords(c, 'original-uploaded-image');
                            }
                        });
                    } else {
                        $('.image-error').text("Escolha uma imagem de tamanho mínimo de " + widthMin + "x" + heightMin + ".");
                        $('#browse-photo').val("");
                    }
                });
            } else {
                $('.image-error').text("Apenas imagens são aceitas.");
            }
        };
        reader.readAsDataURL(file);
    }
});

$('.cut-photo').click(function () {
    if (edit === "photo") {
        $('.upload-photo').click();
    } else if (edit === "cover") {
        $('.upload-cover-photo').click();
    }
})

$('.close-image-cropping-modal, .image-cropping-button.cancel').click(function () {
    setTimeout(function () {
        $('.original-uploaded-image').data('Jcrop').destroy();
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