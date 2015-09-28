var stepOne = stepTwo = stepThree = stepFour = false;

$(document).ready(function () {
    $(".menu-icon-two").parent().addClass("active");

    $(".active-bar").css("width", "20%");

    $(".number-container span").click(function () {
        var validated = stepValidation($(".wizard-container.active"));
        if (validated === true) {
            $(".number").removeClass("active").removeClass("previous");
            $(".wizard-container").removeClass("active");
            $(this).parent().children(".number").addClass("active");
            $(this).parent().prevAll(".number-container").children(".number").addClass("previous");
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
        if ($(this).children().is(":visible")) {
            $(this).parent().css("border", "0");
            $(".agree-error").addClass("hidden");
        } else {
            $(this).parent().css("border", "1px solid red");
            $(".agree-error").removeClass("hidden");
        }
    });
});

function stepValidation(container) {
    if (container.hasClass("one")) {
        if ($(".agree-container div i").is(":visible")) {
            $(".agree-container").css('border', "0");
            $(".agree-error").addClass("hidden");
            return stepOne = true;
        } else {
            $(".agree-container").css('border', "1px solid red");
            $(".agree-error").removeClass("hidden");
            return stepOne = false;
        }
    } else if (container.hasClass("two")) {
        return stepTwo = true;
    } else if (container.hasClass("three")) {
        return stepThree = true;
    } else {
        return stepFour = true;
    }
}

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