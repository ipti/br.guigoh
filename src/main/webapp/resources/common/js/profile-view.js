var edit;

$(document).ready(function () {
    $("#profile-icon-mail span").text(changeNameLength($("#profile-icon-mail span").text(), 20));
    $('.item-box-title-bold p').each(function () {
        $(this).text(changeNameLength($(this).text(), 23));
    });

    $('.item-box-title').each(function () {
        var role = $(this).text().trim();
        role = role.substring(0, role.length - 1);
        $(this).attr("title", role);
        $(this).text(changeNameLength(role, 25));
        if ($(this).text().trim() === "") {
            $(this).next().text("");
        }
    });
});

$(document).on("keypress", '.editable-field', function (e) {
    if (e.keyCode === 13) {
        $(this).closest('.editable-container').find('.edit-button').click();
    }
});

$(document).on("click", ".add-new, .edit-pencil", function () {
    $(this).closest(".editable-container").find('.add-new-action').click();
});

$('.open-chat').click(function () {
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
                $('.original-uploaded-image').attr("src", reader.result).load(function () {
                    var imageWidth = $(this).width();
                    var imageHeight = $(this).height();
                    if (imageWidth > widthMin && imageHeight > heightMin) {
                        $('.image-error').text("");
                        if (imageWidth > imageHeight) {
                            $(this).css("max-width", "650px");
                        } else {
                            $(this).css("max-height", "400px");
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

$('.tab').click(function () {
    if (!$(this).parent().hasClass('active')) {
        $('.tab').not(this).parent().removeClass('active');
        $(this).parent().addClass('active');
        $('.profile-container').hide();
        if ($(this).hasClass("tab-about")) {
            $('#profile-container-about').show();
        } else if ($(this).hasClass("tab-objects")) {
            $('#profile-container-objects').show();
        } else if ($(this).hasClass("tab-resume")) {
            $('#profile-container-resume').show();
        }
    }
});

$(document).on("keyup", '#birth-date-input, .editable-education-initial-date, .editable-education-final-date', function () {
    var reg = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/g;
    var birthDate = $(this).val();
    if (birthDate.indexOf("_") === -1) {
        if (!birthDate.match(reg) || (birthDate.split("/")[2] > new Date().getFullYear())) {
            $(this).val("");
            $(this).mask("99/99/9999");
        }
    }
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        if ($(data.source).hasClass("upload-photo")) {
            document.getElementById("close-image-cropping-modal").click();
        } else if ($(data.source).hasClass("add-new-action")) {
            if ($(data.source).hasClass("occupation")) {
                $('#occupation-input').focusTextToEnd();
            } else if ($(data.source).hasClass("description")) {
                $('#description-input').focusTextToEnd();
            } else if ($(data.source).hasClass("matters")) {
                $('#matters-input').focusTextToEnd();
            } else if ($(data.source).hasClass("musics")) {
                $('#musics-input').focusTextToEnd();
            } else if ($(data.source).hasClass("books")) {
                $('#books-input').focusTextToEnd();
            } else if ($(data.source).hasClass("movies")) {
                $('#movies-input').focusTextToEnd();
            } else if ($(data.source).hasClass("sports")) {
                $('#sports-input').focusTextToEnd();
            } else if ($(data.source).hasClass("hobbies")) {
                $('#hobbies-input').focusTextToEnd();
            } else if ($(data.source).hasClass("birth-date")) {
                $('#birth-date-input').mask("99/99/9999");
                $('#birth-date-input').focusTextToEnd();
            } else if ($(data.source).hasClass("occupation-2")) {
                $('#occupation-2-input').focusTextToEnd();
            } else if ($(data.source).hasClass("phone")) {
                $('#phone-input').focusTextToEnd();
                $('#phone-input').focusout(function () {
                    var phone, element;
                    element = $(this);
                    element.unmask();
                    phone = element.val().replace(/\D/g, '');
                    if (phone.length > 10) {
                        element.mask("(99) 99999-999?9");
                    } else {
                        element.mask("(99) 9999-9999?9");
                    }
                }).trigger('focusout');
            } else if ($(data.source).hasClass("address")) {
                $('.editable-address').focusTextToEnd();
                $('.editable-cep').mask("99999-999");
            } else if ($(data.source).hasClass("education")) {
                $('.editable-education-initial-date, .editable-education-final-date').mask("99/99/9999");
                $('.editable-education-initial-date').focusTextToEnd();
            } else if ($(data.source).hasClass("experience")) {
                $('.editable-experience-initial-date, .editable-experience-final-date').mask("99/99/9999");
                $('.editable-experience-initial-date').focusTextToEnd();
            }
        }
        $('select').each(changeSelectColors);
        $('select').on("change", changeSelectColors);
    }
});

(function ($) {
    $.fn.focusTextToEnd = function () {
        this.focus();
        var $thisVal = this.val();
        this.val('').val($thisVal);
        return this;
    }
}(jQuery));