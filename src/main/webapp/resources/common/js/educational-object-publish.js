var stepOne = stepTwo = stepThree = false;

$(document).ready(function () {
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

    $(document).on("click", ".add-media", function () {
        $('#browse-media').click();
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

    $('#browse-media').change(function (e) {
        for (var i = 0; i < e.originalEvent.target.files.length; i++) {
            var file = e.originalEvent.target.files[i];
            var reader = new FileReader();
            reader.onloadend = function () {
                if (file.type.split("/")[1] === 'mp4' || file.type.split("/")[1] === 'wmv' || file.type.split("/")[1] === 'mpeg'
                || file.type.split("/")[1] === 'avi' || file.type.split("/")[1] === 'wav' || file.type.split("/")[1] === 'mp3'
                || file.type.split("/")[1] === 'wma' || file.type.split("/")[1] === 'ogg' || file.type.split("/")[1] === 'pdf') {
                    $(".add-new-media").click();
                } else {
                    $('.error').text("Adicione uma mídia com formato suportado pelo Guigoh.");
                }
            };
            reader.readAsDataURL(file);
        }
    });

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
            if ($(element).length) {
                $(".add-media").css("color", "#9c9c9c");
            } else {
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
    if ($('.image-preview').length && $('.media').length) {
        return true;
    } else {
        checkErrorBorder(".add-image", "image");
        checkErrorBorder(".media", "media");
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
                $(this).text(changeNameLength($(this).text(), 20));
            });
            $(".author-email").keyup(function (e) {
                if (e.keyCode !== 9) {
                    checkErrorBorder(this, "input");
                }
            });
        }
        if ($(data.source).hasClass("add-new-media") || $(data.source).hasClass("remove-media")) {
            $(".media > span").each(function () {
                $(this).text(changeNameLength($(this).text(), 50));
            });
        }
    }
});

//    var width = $(".page_progress").width();
//    var page = 1;
//    $('.educational_object_author_phone').focusout(function () {
//        var phone, element;
//        element = $(this);
//        element.unmask();
//        phone = element.val().replace(/\D/g, '');
//        if (phone.length > 10) {
//            element.mask("(99) 99999-999?9");
//        } else {
//            element.mask("(99) 9999-9999?9");
//        }
//    }).trigger('focusout');
//    $(document).on('click', '.button_back', function () {
//        $(".form" + page + "_body").hide();
//        $(".page_number").text(--page);
//        $(".form" + page + "_body").show();
//        $(".page_progress").width(width * $(".page_number").text())
//        $(".button_next").show();
//        $(".button_submit").hide();
//        if ($(".page_number").text() == 1) {
//            $(".button_back").hide();
//        }
//    });
//    $(document).on('click', '.button_next', function () {
//        var validate = true;
//        //Form specifics
//        if (page == 2) {
//            if ($(".educational_object_name").val() == "") {
//                validate = false;
//                $(".educational_object_name_warning").css("display", "block");
//            }
//            if ($('input[type=radio]:checked').length == 0) {
//                validate = false;
//                $(".educational_object_theme_warning").css("display", "block");
//            }
//            if ($(".educational_object_tags").val() == "") {
//                validate = false;
//                $(".educational_object_tags_warning").css("display", "block");
//            }
//        }
//        if (page == 3) {
//            if ($(".author_added").length == 0) {
//                validate = false;
//                $(".educational_object_author_warning").css("display", "block");
//            }
//        }
//        //End
//        if (validate) {
//            $(".form" + page + "_body").hide();
//            $(".page_number").text(++page);
//            $(".form" + page + "_body").show();
//            $(".page_progress").width(width * $(".page_number").text());
//            if ($(".page_number").text() != 1) {
//                $(".button_back").show();
//            }
//            if ($(".page_number").text() == 4) {
//                $(".button_next").hide();
//                $(".button_submit").show();
//            }
//            $(".educational_object_name_warning").hide();
//            $(".educational_object_theme_warning").hide();
//            $(".educational_object_tags_warning").hide();
//            $(".educational_object_author_warning_name").hide();
//            $(".educational_object_author_warning_email").hide();
//            $(".educational_object_author_warning_phone").hide();
//            $(".educational_object_author_warning_site").hide();
//            $(".educational_object_author_warning").hide();
//            $(".educational_object_image_warning").hide();
//            $(".educational_object_media_warning").hide();
//        }
//
//    });
//    $(document).on('click', '.button_author_add', function () {
//        var nameValue = $(".educational_object_author_name").val();
//        var emailValue = $(".educational_object_author_email").val();
//        var validate = true;
//        if (!String(nameValue).match("[a-z\u00C0-\u00ff A-Z]{3,40}")) {
//            $(".educational_object_author_warning_name").css("display", "block");
//            validate = false;
//        } else if (emailValue != "" && !String(emailValue).match("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
//            $(".educational_object_author_warning_email").css("display", "block");
//            validate = false;
//        }
//        if (validate) {
//            $(".educational_object_author_warning_name").hide();
//            $(".educational_object_author_warning_email").hide();
//            $(".add").click();
//        }
//    });
//
//    function validateImage(image) {
//        var type = image.text().split(".").pop();
//        if ((!type.match(/^png$/i)) && (!type.match(/^jpg$/i)) && (!type.match(/^jpeg$/i)) && (!type.match(/^gif$/i)) && (!type.match(/^bmp$/i))) {
//            return false;
//        }
//        return true;
//    }
//
//    function validateMedia(media) {
//        var type = media.text().split(".").pop();
//        if ((!type.match(/^jpg$/i)) && (!type.match(/^jpeg$/i)) && (!type.match(/^png$/i)) && (!type.match(/^gif$/i)) &&
//                (!type.match(/^bmp$/i)) && (!type.match(/^mp3$/i)) && (!type.match(/^wav$/i)) && (!type.match(/^wma$/i)) &&
//                (!type.match(/^mp4$/i)) && (!type.match(/^avi$/i)) && (!type.match(/^mpeg$/i)) && (!type.match(/^wmv$/i)) &&
//                (!type.match(/^ogg$/i)) && (!type.match(/^webM$/i)) && (!type.match(/^swf$/i)) && (!type.match(/^doc$/i)) &&
//                (!type.match(/^docx$/i)) && (!type.match(/^txt$/i)) && (!type.match(/^pdf$/i))) {
//
//            return false;
//        }
//        return true;
//    }
//
//    $(document).on("click", "#selectImage", function () {
//        $("#imageFile").click();
//    });
//
//    $(document).on('change', '#imageFile', function (e) {
//        for (var i = 0; i < e.originalEvent.target.files.length; i++) {
//            var file = e.originalEvent.target.files[i];
//            var reader = new FileReader();
//            reader.onloadend = function () {
//                $("#noImage").hide();
//                $("#image").attr("src", reader.result);
//                $("#image").css("display", "block");
//                $(".educational_object_image_warning").hide();
//                $(".educational_object_image_type_warning").hide();
//                $("#selectedImageName").css("color", "black");
//            }
//            reader.readAsDataURL(file);
//            if (file.name.length <= 32) {
//                $("#selectedImageName").text(file.name);
//            } else {
//                $("#selectedImageName").text(file.name.substring(0, 15) + "..." + file.name.substring(file.name.length - 15, file.name.length));
//            }
//        }
//    });
//
//    $(document).on('click', '.button_submit', function () {
//        var validate = true;
//        if ($("#image").attr("src") == "") {
//            validate = false;
//            $(".educational_object_image_warning").css("display", "block");
//        } else if (!validateImage($("#selectedImageName"))) {
//            validate = false;
//            $("#selectedImageName").css("color", "red");
//            $(".educational_object_image_type_warning").css("display", "block");
//        }
//        if ((($("#mediaFile1").val() == '') && ($("#mediaFile2").val() == '') && ($("#mediaFile3").val() == '')) == true) {
//            validate = false;
//            $(".educational_object_media_warning").css("display", "block");
//        }
//        if (!validateMedia($("#selectedMediaName1")) && $("#mediaFile1").val() != "") {
//            validate = false;
//            $("#selectedMediaName1").css("color", "red");
//            $(".educational_object_media_type_warning").css("display", "block");
//        }
//        if (!validateMedia($("#selectedMediaName2")) && $("#mediaFile2").val() != "") {
//            validate = false;
//            $("#selectedMediaName2").css("color", "red");
//            $(".educational_object_media_type_warning").css("display", "block");
//        }
//        if (!validateMedia($("#selectedMediaName3")) && $("#mediaFile3").val() != "") {
//            validate = false;
//            $("#selectedMediaName3").css("color", "red");
//            $(".educational_object_media_type_warning").css("display", "block");
//        }
//
//        if (validate) {
//            $("#submit").click();
//            $(".form4_body").hide();
//            $(".button_options").hide();
//            $(".form5_body").show();
//        }
//    });
//
//    $(document).on("click", "#selectMedia1", function () {
//        $("#mediaFile1").click();
//    });
//    $(document).on("click", "#selectMedia2", function () {
//        $("#mediaFile2").click();
//    });
//    $(document).on("click", "#selectMedia3", function () {
//        $("#mediaFile3").click();
//    });
//
//    var media1;
//    var media2;
//    var media3;
//
//    $(document).on("change", "#mediaFile1", function () {
//        media1 = this.files[0];
//        if (media1.name.length <= 32) {
//            $("#selectedMediaName1").text(media1.name);
//            $("#uploaded_file1").text(media1.name);
//        } else {
//            $("#selectedMediaName1").text(media1.name.substring(0, 15) + "..." + media1.name.substring(media1.name.length - 15, media1.name.length));
//            $("#uploaded_file1").text(media1.name.substring(0, 15) + "..." + media1.name.substring(media1.name.length - 15, media1.name.length));
//        }
//        $("#uploaded1").show();
//        $(".educational_object_media_warning").css("display", "none");
//        $(".educational_object_media_type_warning").css("display", "none");
//        $("#selectedMediaName1").css("color", "black");
//    });
//
//    $(document).on("change", "#mediaFile2", function () {
//        media2 = this.files[0];
//        if (media2.name.length <= 32) {
//            $("#selectedMediaName2").text(media2.name);
//            $("#uploaded_file2").text(media2.name);
//        } else {
//            $("#selectedMediaName2").text(media2.name.substring(0, 15) + "..." + media2.name.substring(media2.name.length - 15, media2.name.length));
//            $("#uploaded_file2").text(media2.name.substring(0, 15) + "..." + media2.name.substring(media2.name.length - 15, media2.name.length));
//        }
//        $("#uploaded2").show();
//        $(".educational_object_media_warning").css("display", "none");
//        $(".educational_object_media_type_warning").css("display", "none");
//        $("#selectedMediaName2").css("color", "black");
//    });
//
//    $(document).on("change", "#mediaFile3", function () {
//        media3 = this.files[0];
//        if (media3.name.length <= 32) {
//            $("#selectedMediaName3").text(media3.name);
//            $("#uploaded_file3").text(media3.name);
//        } else {
//            $("#selectedMediaName3").text(media3.name.substring(0, 15) + "..." + media3.name.substring(media3.name.length - 15, media3.name.length));
//            $("#uploaded_file3").text(media3.name.substring(0, 15) + "..." + media3.name.substring(media3.name.length - 15, media3.name.length));
//        }
//        $("#uploaded3").show();
//        $(".educational_object_media_warning").css("display", "none");
//        $(".educational_object_media_type_warning").css("display", "none");
//        $("#selectedMediaName3").css("color", "black");
//    });
//
//    var progressbar = $("#progressbar");
//    var progressLabel = $(".progress-label");
//
//    $("#progressbar").progressbar({
//        value: false,
//        change: function () {
//            progressLabel.text(progressbar.progressbar("value") + "%");
//        },
//        complete: function () {
//            progressLabel.text("100%");
//
//        }
//    });
//
//    var formData = new FormData();
//    var percent;
//
//    $(document).on("change", "input[type='file']", function () {
//        addFiles(this.files);
//    });
//
//    $(document).on("click", "#submit", uploadFiles);
//
//    function addFiles(files) {
//        for (var i = 0, file; file = files[i]; i++) {
//            formData.append(file.name, file);
//        }
//    }
//
//    function uploadFiles() {
//
////        for (var i = 0, file; file = files[i]; i++) {
////            formData.append(file.name, file);
////        }
//
//        var xhr = new XMLHttpRequest();
//
//        if (xhr.upload) {
//
//            xhr.upload.onloadstart = function () {
//                percent = 0;
//            }
//            xhr.upload.onprogress = function (e) {
//                if (e.lengthComputable) {
////                    console.log("e.loaded: " + e.loaded + " / total: " + e.total);
//                    percent = Math.floor(e.loaded / e.total * 100);
////                    console.log("Progresso: " + percent + "%");
//                    progressbar.progressbar("value", percent);
//                }
//            }
//            xhr.onreadystatechange = function () {
//                if (this.readyState === 4) {
////                    console.log(this.readyState + " / " + this.status + " / " + this.statusText);
//                    $("#loading1").hide();
//                    $("#attached1").show();
//                    $(".form5_body").hide();
//                    $(".form6_body").show();
//                }
//            }
//        }
//
//        xhr.open("post", "../educational-object/submit.xhtml", true);
//        xhr.send(formData);
//    }