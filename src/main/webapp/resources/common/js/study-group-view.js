$(document).ready(function () {
    $(".menu-icon-three").parent().addClass("active");

    $('.aside-image-title').find('a').each(function () {
        $(this).text(changeNameLength($(this).text(), 30));
    });

    $('.column-object-description').children('span').each(function () {
        $(this).text(changeNameLength($(this).text(), 100));
    });
});

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() === $(document).height()) {
        $('.load-more-topics').click();
    }
});

jsf.ajax.addOnEvent(function (data) {
    $('.column-object-description').children('span').each(function () {
        $(this).text(changeNameLength($(this).text(), 100));
    });
});