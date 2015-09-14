$(document).ready(function(){
    $(".menu-icon-one").parent().addClass("active");    
    
    $('.aside-image-title').find('a').each(function () {
        $(this).text(changeNameLength($(this).text(), 27));
    });

    $('.column-object-description').children('span').each(function () {
        $(this).text(changeNameLength($(this).text(), 100));
    });
    
    $('.column-object-title').find('a').each(function () {
        $(this).text(changeNameLength($(this).text(), 50));
    });
});

$(".educational-object-search").on('keypress', function (e) {
    if (e.keyCode == 13) {
        e.preventDefault();
    }
});

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() === $(document).height()) {
        $('.load-more-objects').click();
    }
});

jsf.ajax.addOnEvent(function (data) {
    $('.column-object-description').children('span').each(function () {
        $(this).text(changeNameLength($(this).text(), 100));
    });
});