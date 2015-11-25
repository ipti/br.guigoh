var stepOne = stepTwo = stepThree = false;
var formData = new FormData();

$(document).ready(function () {
    $.post("../ping.html");
    window.setInterval(function () {
        $.post("../ping.html");
    }, 1500000);
    
    $(".menu-icon-two").parent().addClass("active");

    $(".active-bar").css("width", "20%");

    $(".number-container span").click(function () {
        checkValidation($(".wizard-container.active"));
        var validated = $(this).parent().hasClass("one")
                || ($(this).parent().hasClass("two") && stepOne)
                || ($(this).parent().hasClass("three") && stepOne && stepTwo)
                || ($(this).parent().hasClass("four") && stepOne && stepTwo && stepThree);
        if (validated) {
            $(".number").removeClass("active").removeClass("completed");
            $(".wizard-container").removeClass("active");
            $(this).parent().children(".number").addClass("active");
            $(this).parent().prevAll(".number-container").children(".number").addClass("completed");
            $(".active-bar").css("width", 20 * $(this).parent().children(".number").text() + "%");
            $(this).parent().hasClass("one") ? $(".wizard-container.one").addClass("active")
                    : $(this).parent().hasClass("two") ? $(".wizard-container.two").addClass("active")
                    : $(this).parent().hasClass("three") ? $(".wizard-container.three").addClass("active")
                    : $(".wizard-container.four").addClass("active");
        }
    });

    $(".forward").click(function () {
        $(".number.active").parent().next().find(".number").click();
    });

    $(".backward").click(function () {
        $(".number.active").parent().prev().find(".number").click();
    });

    $(".agree-container div").click(function () {
        $(this).find("i").toggle();
        checkErrorBorder(".agree-container div i", "checkbox");
    });

    $(".object-name, .object-description, .author-name, .author-email").keyup(function (e) {
        if (e.keyCode !== 9) {
            checkErrorBorder(this, "input");
        }
    });

    $(document).on("keypress", ".tag-input", function (e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            if ($(this).val().trim() !== "") {
                var value = $(this).val();
                value = value.replace("#", "");
                $(this).val(value);
                $('.add-tag').click();
            }
        }
    });

    $(document).on("keypress", ".author-name, .author-email, .author-role", function (e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            $('.add-author').click();
        }
    });

    $('select').each(changeSelectColors);
    $(document).on("change", "select", changeSelectColors);
    $(document).on("change", "select", function () {
        checkErrorBorder(this, "select");
    });

    var maxLength = 200;
    $('.max-length').text("(" + maxLength + ")");
    $(document).on('keyup', '.object-description', function (e) {
        var length = $(this).val().length;
        length = maxLength - length;
        $('.max-length').text("(" + length + ")");
    });

    $(document).on("click", ".add-image, .edit-image", function () {
        $('#browse-image').click();
    });

    $(document).on("click", ".add-media .fa-plus-circle, .add-media span", function () {
        $(this).parent().hasClass("add-media-one") ? $('#browse-media-1').click() : ($(this).parent().hasClass("add-media-two") ? $('#browse-media-2').click() : $('#browse-media-3').click());
    });

    $('#browse-image').change(function (e) {
        var widthMin = 100;
        var heightMin = 100;
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
                            $('.error').text("");
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
                            $('.error').text("Escolha uma imagem de tamanho mínimo de " + widthMin + "x" + heightMin + ".");
                            $('#browse-image').val("");
                        }
                    });
                } else {
                    $('.error').text("Apenas imagens são aceitas.");
                }
            };
            reader.readAsDataURL(file);
        }
    });

    $('.browse-media').change(function (e) {
        for (var i = 0, file; file = this.files[i]; i++) {
            formData.append(file.name, file);
        }
        var number = $(this).attr("id") === "browse-media-1" ? "one" : ($(this).attr("id") === "browse-media-2" ? "two" : "three");
        for (var i = 0; i < e.originalEvent.target.files.length; i++) {
            var file = e.originalEvent.target.files[i];
            var media = $(this);
            var exists = false;
            $('.browse-media').not(this).each(function () {
                if ($(this).val() === media.val()) {
                    exists = true;
                }
            });
            if (!exists) {
                if (file.type.split("/")[1] === 'mp4' || file.type.split("/")[1] === 'wmv' || file.type.split("/")[1] === 'mpeg'
                        || file.type.split("/")[1] === 'avi' || file.type.split("/")[1] === 'wav' || file.type.split("/")[1] === 'mp3'
                        || file.type.split("/")[1] === 'wma' || file.type.split("/")[1] === 'ogg' || file.type.split("/")[1] === 'pdf') {
                    $(".add-media").each(function () {
                        if (!$(this).children().first().hasClass("fa-paperclip")) {
                            $(this).css("color", "#9c9c9c");
                        }
                    });
                    $(".add-media-" + number).css("color", "#333");
                    $(".add-media-" + number).children("span").text(changeNameLength(file.name, 53));
                    if (!$(".add-media-" + number).children(".remove-media").length) {
                        $(".add-media-" + number).append("<i class='remove-media fa fa-times'/>");
                    }
                    if (!$(".add-media-" + number).children(".fa-paperclip").length) {
                        $(".add-media-" + number).children(".fa-plus-circle").removeClass("fa-plus-circle").addClass("fa-paperclip");
                    }
                } else {
                    $('.error').text("Adicione uma mídia com formato suportado pelo Guigoh.");
                }
            }
        }
    });

    $(document).on("click", ".remove-media", function () {
        var number = $(this).parent().hasClass("add-media-one") ? "1" : ($(this).parent().hasClass("add-media-two") ? "2" : "3");
        $("#browse-media-" + number).val("");
        $(this).parent().children("span").text($(".add-media-text").text());
        $(this).parent().children(".fa-paperclip").removeClass("fa-paperclip").addClass("fa-plus-circle");
        $(this).parent().css("color", "#9c9c9c");
        $(this).remove();
    })

    $('.close-image-cropping-modal, .image-cropping-button.cancel').click(function () {
        setTimeout(function () {
            $('.original-uploaded-image').data('Jcrop').destroy();
            $('.original-uploaded-image').removeAttr('style');
            $('#browse-photo').val("");
        }, 400);
    });

    $('.cut-photo').click(function () {
        $('.upload-image').click();
        document.getElementById("close-image-cropping-modal").click();
    });

    $('.add-media span').text($(".add-media-text").text());
});

function checkValidation(container) {
    if (container.hasClass("one")) {
        checkErrorBorder(".agree-container div i", "checkbox");
        if ($(".agree-container div i").is(":visible")) {
            stepOne = true;
        } else {
            stepOne = false;
        }
    } else if (container.hasClass("two")) {
        checkErrorBorder(".object-name", "input");
        checkErrorBorder(".object-description", "input");
        checkErrorBorder(".object-theme", "select");
        checkErrorBorder(".tag", "tag");
        if ($(".object-name").val() !== "" && $(".object-description").val() !== ""
                && !$(".object-theme").children('option:first-child').is(':selected') && $('.tag').length !== 0) {
            stepTwo = true;
        } else {
            stepTwo = false;
        }
    } else if (container.hasClass("three")) {
        if ($(".author").length !== 0) {
            stepThree = true;
            $(".author-name").css('border', "1px solid #CACACA");
            $(".author-email").css('border', "1px solid #CACACA");
            $(".author-role").css('border', "1px solid #CACACA");
        } else {
            $(".author-name").css('border', "1px solid red");
            $(".author-email").css('border', "1px solid red");
            $(".author-role").css('border', "1px solid red");
            stepThree = false;
        }
    }
}

function checkErrorBorder(element, type) {
    switch (type) {
        case "input":
            if ($(element).val() !== "") {
                $(element).css('border', "1px solid #CACACA");
            } else {
                $(element).css('border', "1px solid red");
            }
            break;
        case "select":
            if (!$(element).children('option:first-child').is(':selected')) {
                $(element).css('border', "1px solid #CACACA");
            } else {
                $(element).css('border', "1px solid red");
            }
            break;
        case "tag":
            if ($(element).length !== 0) {
                $('.tag-input').css('border', "1px solid #CACACA");
            } else {
                $('.tag-input').css('border', "1px solid red");
            }
            break;
        case "checkbox":
            if ($(element).is(":visible")) {
                $(element).parent().parent().css('border', "0");
            } else {
                $(element).parent().parent().css('border', "1px solid red");
            }
            break;
        case "email":
            var email = $(element).val();
            if (email.match("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
                $(element).css('border', "0");
            } else {
                $(element).css('border', "1px solid red");
            }
            break;
        case "image":
            if ($(element).val() !== "") {
                $(".add-image").css('border', "0");
            } else {
                $(".add-image").css('border', "1px solid red");
            }
            break;
        case "media":
            var empty = true;
            $(".add-media").each(function () {
                if ($(this).children().first().hasClass("fa-paperclip")) {
                    empty = false;
                    $(this).css("color", "#333");
                } else {
                    if (!empty) {
                        $(this).css("color", "#9c9c9c");
                    }
                }
            });
            if (empty) {
                $(".add-media").css("color", "red");
            }
            break;
    }
}

function checkTagEmpty(tag) {
    if ($(tag).val() !== "") {
        return true;
    } else {
        return false;
    }
}

function checkAuthorEmpty(authorName, authorEmail, authorRole) {
    $(".author-email").keyup(function (e) {
        if (e.keyCode !== 9) {
            checkErrorBorder(this, "email");
        }
    });
    checkErrorBorder(authorName, "input");
    checkErrorBorder(authorEmail, "email");
    checkErrorBorder(authorRole, "select");
    var email = $(authorEmail).val();
    if ($(authorName).val() !== "" && email.match("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b") && !$(authorRole).children('option:first-child').is(':selected')) {
        return true;
    } else {
        return false;
    }
}

function checkMediaEmpty() {
    if ($('#browse-image').val() !== "" && ($('#browse-media-1').val() !== "" || $('#browse-media-2').val() !== "" || $('#browse-media-3').val() !== "")) {
        $(".submit-object").hide();
        $("#upload-progress-box").show();
        uploadFiles();
        return true;
    } else {
        checkErrorBorder(".add-image", "image");
        checkErrorBorder(".add-media", "media");
        return false;
    }
}

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        if ($(data.source).hasClass("add-tag")) {
            if ($('.tag').length < 7) {
                $('.tag-input').focus().select();
            } else {
                $('.tag-input').prop("disabled", "true");
            }
        }
        if ($(data.source).hasClass("add-author")) {
            $('select').each(changeSelectColors);
            $('.author-name').focus().select();
            $('.new-author-name').each(function () {
                if ($(this).children("span").length) {
                    $(this).children("span").text(changeNameLength($(this).children("span").text(), 18));
                } else {
                    $(this).text(changeNameLength($(this).text(), 18));
                }
            });
            $(".author-email").keyup(function (e) {
                if (e.keyCode !== 9) {
                    checkErrorBorder(this, "input");
                }
            });
        }
    }
});

$("#upload-progress-bar").progressbar({
    value: false,
    change: function () {
        $(".upload-progress-label").text($("#upload-progress-bar").progressbar("value") + "%");
    },
    complete: function () {
        $(".upload-progress-label").text("100%");
    }
});

function uploadFiles() {
    var percent;
    var xhr = new XMLHttpRequest();

    if (xhr.upload) {

        xhr.upload.onloadstart = function () {
            percent = 0;
        }
        xhr.upload.onprogress = function (e) {
            if (e.lengthComputable) {
                percent = Math.floor(e.loaded / e.total * 100);
                $("#upload-progress-bar").progressbar("value", percent);
            }
        }
    }

    xhr.open("post", "../educational-object/publish.xhtml", true);
    xhr.send(formData);
}
