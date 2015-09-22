$(document).ready(function () {
    $(".menu-icon-three").parent().addClass("active");

    $(document).on('click', '.attach-link', function () {
        $('.file-input').click();
    });

    $('.message-textarea').on('DOMMouseScroll mousewheel', preventScrolling);

    $(document).on('keypress', '.message-textarea, .topic-title', function (e) {
        if (e.keyCode === 13 && !e.shiftKey)
        {
            e.preventDefault();
            $('.publish-topic').click();
        }
    });

    $(document).on("click", ".publish-topic", function () {
        if ($('.message-textarea').val().trim() !== "" && $('.topic-title').val().trim() !== "") {
            $('.publish-link').click();
        }
    });

    $(document).on("keypress", ".tag-input", function (e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            if ($(this).val().trim() !== "") {
                var value = $(this).val();
                value = value.replace("#", "");
                $(this).val(value);
                $('.tag-link').click();
            }
        }
    });
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        if ($(data.source).hasClass("file-input") || $(data.source).hasClass("remove-file")) {
            $('.file-input').val("");
            $('.file').each(function () {
                $(this).text(changeFileNameLength($(this).text(), 26));
            });
        }
        if ($(data.source).hasClass("tag-link") || $(data.source).hasClass("tag")) {
            $('.tag-input').css("width", "calc(100% - " + $('.tags').css("width") + " - " + $('.tags').css("margin-right") + ")");
            if ($('.tag').length === 3) {
                $('.tag-input').hide();
            } else {
                $('.tag-input').show();
            }
            if ($(data.source).hasClass("tag-link")) {
                $('.tag-input').focus().select();
            }
        }
    }
});