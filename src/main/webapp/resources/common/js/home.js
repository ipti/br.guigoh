$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() === $(document).height()) {
        $('.load-more-objects').click();
        $('.load-more-activities').click();
    }
});

$(document).ready(function () {
    $('.column-object-description').children('span').each(function () {
        $(this).text(changeNameLength($(this).text(), 100));
    });
    $('.column-object-title').find('a').each(function () {
        $(this).text(changeNameLength($(this).text(), 42));
    });
});

jsf.ajax.addOnEvent(function (data) {
    if ($(data.source).hasClass("load-more-objects")
            || $(data.source).hasClass("load-more-activities")) {
        $('.column-object-description').children('span').each(function () {
            $(this).text(changeNameLength($(this).text(), 100));
        });
        $('.column-object-title').find('a').each(function () {
            $(this).text(changeNameLength($(this).text(), 42));
        });
    }
});
