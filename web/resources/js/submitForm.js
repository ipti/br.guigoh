$(document).ready(function() {
    var width = $(".page_progress").width();
    var page = 1;
    $(".educational_object_author_phone").mask("(99) 9999-9999");
    $(document).on('click', '.button_back', function() {
        $(".form" + page + "_body").hide();
        $(".page_number").text(--page);
        $(".form" + page + "_body").show();
        $(".page_progress").width(width * $(".page_number").text())
        $(".button_next").show();
        $(".button_submit").hide();
        if ($(".page_number").text() == 1) {
            $(".button_back").hide();
        }
    });
    $(document).on('click', '.button_next', function() {
        var validate = true;
        //Form specifics
        if (page == 2) {
            if ($(".educational_object_name").val() == "") {
                validate = false;
                $(".educational_object_name_warning").css("display", "block");
            }
            if ($('input[type=radio]:checked').length == 0) {
                validate = false;
                $(".educational_object_theme_warning").css("display", "block");
            }
            if ($(".educational_object_tags").val() == "") {
                validate = false;
                $(".educational_object_tags_warning").css("display", "block");
            }
        }
        if (page == 3) {
            if ($(".author_added").length == 0) {
                validate = false;
                $(".educational_object_author_warning").css("display", "block");
            }
        }
        //End
        if (validate) {
            $(".form" + page + "_body").hide();
            $(".page_number").text(++page);
            $(".form" + page + "_body").show();
            $(".page_progress").width(width * $(".page_number").text());
            if ($(".page_number").text() != 1) {
                $(".button_back").show();
            }
            if ($(".page_number").text() == 4) {
                $(".button_next").hide();
                $(".button_submit").show();
            }
            $(".educational_object_name_warning").hide();
            $(".educational_object_theme_warning").hide();
            $(".educational_object_tags_warning").hide();
            $(".educational_object_author_warning_name").hide();
            $(".educational_object_author_warning_email").hide();
            $(".educational_object_author_warning_phone").hide();
            $(".educational_object_author_warning_site").hide();
            $(".educational_object_author_warning").hide();
            $(".educational_object_image_warning").hide();
            $(".educational_object_media_warning").hide();
        }

    });
    $(document).on('click', '.button_author_add', function() {
        var nameValue = $(".educational_object_author_name").val();
        var emailValue = $(".educational_object_author_email").val();
        var phoneValue = $(".educational_object_author_phone").val();
        var siteValue = $(".educational_object_author_site").val();
        var validate = true;
        if (!String(nameValue).match("[a-z\u00C0-\u00ff A-Z]{3,40}")) {
            $(".educational_object_author_warning_name").css("display", "block");
            validate = false;
        } else if (emailValue != "" && !String(emailValue).match("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
            $(".educational_object_author_warning_email").css("display", "block");
            validate = false;
        } else if (phoneValue != "" && !String(phoneValue).match("\\(\\d{2}\\) \\d{4}-\\d{4}")) {
            $(".educational_object_author_warning_phone").css("display", "block");
            validate = false;
        } else if (siteValue != "" && !String(siteValue).match("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?")) {
            $(".educational_object_author_warning_site").css("display", "block");
            validate = false;
        }
        if (validate) {
            $(".educational_object_author_warning_name").hide();
            $(".educational_object_author_warning_email").hide();
            $(".educational_object_author_warning_phone").hide();
            $(".educational_object_author_warning_site").hide();
            $(".add").click();
        }
    });
    $(document).on('change', '#imageFile', function(e) {
        for (var i = 0; i < e.originalEvent.target.files.length; i++) {
            var file = e.originalEvent.target.files[i];
            var reader = new FileReader();
            reader.onloadend = function() {
                $("#image").attr("src", reader.result);
                $("#image").css("display", "block");
                $(".educational_object_image_warning").hide();
            }
            reader.readAsDataURL(file);
        }
    });
//    $(document).on('change', '#mediaFile', function(e) {
//        $(".file_loading_image").css("display", "block");
//        $(".file_attach_image").css("display", "none");
//        for (var i = 0; i < e.originalEvent.target.files.length; i++) {
//            var file = e.originalEvent.target.files[i];
//            var reader = new FileReader();
//            reader.onloadend = function() {
//                $(".file_loading_image").css("display", "none");
//                $(".file_attach_image").css("display", "block");
//                $(".educational_object_media_warning").hide();
//            }
//            reader.readAsDataURL(file);
//        }
//    });

    $(document).on('click', '.button_submit', function() {
        var validate = true;
        if ($("#image").attr("src") === undefined) {
            $(".educational_object_image_warning").css("display", "block");
            validate = false;
        }
        if ((($("#mediaFile1").val() == '') && ($("#mediaFile2").val() == '') && ($("#mediaFile3").val() == '')) == true) {
            validate = false;
            $(".educational_object_media_warning").css("display", "block");
        }
        if (validate) {
            $(".submit").click();
            //$(".loading_gif").css("display", "block");
            $(".form4_body").hide();
            $(".button_options").hide();
            $(".form5_body").show();
        }
    });

    var media1;
    var media2;
    var media3;

    $(document).on("change", "#mediaFile1", function() {
        media1 = this.files[0];
        $("#uploaded_file1").text(media1.name);
        $("#uploaded1").show();
        $(".educational_object_media_warning").css("display", "none");
    });

    $(document).on("change", "#mediaFile2", function() {
        media2 = this.files[0];
        $("#uploaded_file2").text(media2.name);
        $("#uploaded2").show();
        $(".educational_object_media_warning").css("display", "none");
    });

    $(document).on("change", "#mediaFile3", function() {
        media3 = this.files[0];
        $("#uploaded_file3").text(media3.name);
        $("#uploaded3").show();
        $(".educational_object_media_warning").css("display", "none");
    });

    var submitted1 = true;
    var submitted2 = true;
    var submitted3 = true;

    function finishUpload() {
        if ((submitted1 == true) & (submitted2 == true) & (submitted3 == true)) {
            $(".form5_body").hide();
            $(".form6_body").show();
        }
    }

    $(document).on("click", "#submit", function() {
        var xhr1 = new XMLHttpRequest();
        var xhr2 = new XMLHttpRequest();
        var xhr3 = new XMLHttpRequest();

        if (xhr1.upload) {
            var progress1;
            xhr1.upload.onloadstart = function() {
                submitted1 = false;
            }
            xhr1.upload.onprogress = function(e) {
                progress1 = Math.floor(e.loaded / e.total * 100);
                $("#upload_bg1").css("width", progress1 + '%');
                $("#upload_percent1").text(progress1 + '%');
            };
            xhr1.upload.onloadend = function() {
                $("#upload_bg1").css("width", "100%");
                $("#upload_percent1").text("100%");
                $("#loading1").hide();
                $("#attached1").show();
                submitted1 = true;
                finishUpload();
            }
        }
        xhr1.open('post', "../mandril/submitForm.xhtml", true);
        xhr1.send(media1);

        if (xhr2.upload) {
            var progress2;
            xhr2.upload.onloadstart = function() {
                submitted2 = false;
            }
            xhr2.upload.onprogress = function(e) {
                progress2 = Math.floor(e.loaded / e.total * 100);
                $("#upload_bg2").css("width", progress2 + '%');
                $("#upload_percent2").text(progress2 + '%');
            };
            xhr2.upload.onloadend = function() {
                $("#upload_bg2").css("width", "100%");
                $("#upload_percent2").text("100%");
                $("#loading2").hide();
                $("#attached2").show();
                submitted2 = true;
                finishUpload();
            }
        }
        xhr2.open('post', "../mandril/submitForm.xhtml", true);
        xhr2.send(media2);

        if (xhr3.upload) {
            var progress3;
            xhr3.upload.onloadstart = function() {
                submitted3 = false;
            }
            xhr3.upload.onprogress = function(e) {
                progress3 = Math.floor(e.loaded / e.total * 100);
                $("#upload_bg3").css("width", progress3 + '%');
                $("#upload_percent3").text(progress3 + '%');
            };
            xhr3.upload.onloadend = function() {
                $("#upload_bg3").css("width", "100%");
                $("#upload_percent3").text("100%");
                $("#loading3").hide();
                $("#attached3").show();
                submitted3 = true;
                finishUpload();
            }
        }
        xhr3.open('post', "../mandril/submitForm.xhtml", true);
        xhr3.send(media3);
    });

});