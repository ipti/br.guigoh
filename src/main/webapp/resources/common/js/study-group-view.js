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

    $('.file-attached a span').each(function () {
        $(this).text(changeFileNameLength($(this).text(), 70));
    });

    $(document).on('click', '.attach-link', function () {
        $('.file-input').click();
    });
    
    setTimeout(function(){
        $('.increase-views').click();
    }, 3000);
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        if ($(data.source).hasClass("file-input") || $(data.source).hasClass("remove-file")) {
            $('.file-input').val("");
            $('.file').each(function () {
                $(this).text(changeFileNameLength($(this).text(), 26));
            });
        }
        if ($(data.source).hasClass("publish-link")) {
            $('.file-attached a span').each(function () {
                $(this).text(changeFileNameLength($(this).text(), 70));
            });
        }
        if ($(data.source).hasClass("page-number") || $(data.source).hasClass("page-icons")) {
            $('html, body').animate({scrollTop: 0}, 'fast');
            $('.file-attached a span').each(function () {
                $(this).text(changeFileNameLength($(this).text(), 70));
            });
        }
    }
});