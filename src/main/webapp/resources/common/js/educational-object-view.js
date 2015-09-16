$(document).ready(function () {
    $(".menu-icon-one").parent().addClass("active");

    $(".media-type li").first().addClass("active");
    $(".media").first().addClass("active");
    
    $('.message-textarea').on('DOMMouseScroll mousewheel', preventScrolling);

    $('.message-textarea').keypress(function (e) {
        if (e.keyCode === 13 && !e.shiftKey)
        {
            $('.publish-link').click();
            $('.publish-link').val("");
        }
    });
});

$('.tab').click(function () {
    if (!$(this).hasClass('active')) {
        var mediaTab = $(this).attr("media");
        var media = $('.media[media="' + mediaTab + '"]');
        $('.tab').not(this).removeClass('active');
        $(this).addClass('active');
        $('.media').each(function () {
            if ($(this).prop("tagName") !== 'IFRAME') {
                $(this).get(0).pause();
            }
        });
        $('.media').removeClass('active');
        media.addClass('active');
    }
});