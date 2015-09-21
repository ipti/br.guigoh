$(document).ready(function () {
    $(".menu-icon-three").parent().addClass("active");

    $('.message-textarea').on('DOMMouseScroll mousewheel', preventScrolling);

    $(document).on('keypress', '.message-textarea', function (e) {
        if (e.keyCode === 13 && !e.shiftKey)
        {
            e.preventDefault();
            if ($(this).val().trim() !== "") {
                $('.publish-link').click();
                $('.message-textarea').val("");
            }
        }
    });

    $(document).on('click', '.attach-link', function () {
        $('.file-input').click();
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
    }
});