var maxLength;

$(document).ready(function () {
    $(".menu-icon-one").parent().addClass("active");

    $(".media-type li").first().addClass("active");
    $(".media").first().addClass("active");
    
    $('.message-textarea').on('DOMMouseScroll mousewheel', preventScrolling);

    maxLength = 200;
    $(document).on('keypress', '.message-textarea', function (e) {
        if (e.keyCode === 13 && !e.shiftKey)
        {
            e.preventDefault();
            if ($(this).val().trim() !== "") {
                $('.publish-link').click();
                $('.message-textarea').val("");
                $('.max-length').text("(" + maxLength + ")");
            }
        }
    });

    $('.max-length').text("(" + maxLength + ")");
    $(document).on('keyup', '.message-textarea', function (e) {
        var length = $(this).val().length;
        length = maxLength - length;
        $('.max-length').text("(" + length + ")");
    });
    var subnetworks = $('.subnetworks').text();
    subnetworks = subnetworks.split(" / ").join("<br/>");
    $('.subnetworks').html(subnetworks);
});

$('span.like-object, .illusory-textarea').mousemove(function (e) {
    if ($(this).children(".message-textarea").length == 0 || $(this).children(".message-textarea").is(":disabled")) {
        $('#visitorPermissionWarning').css({position: 'absolute', top: e.pageY + 10 + 'px', left: e.pageX + 'px'}).show();
    }
});

$('span.like-object, .illusory-textarea').mouseleave(function (e) {
        $('#visitorPermissionWarning').hide();
});

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() === $(document).height()) {
        $('.load-more-messages').click();
    }
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

function likeObject(element) {
    if ($(element).children("span").hasClass("active")) {
        $(element).children(".like-count").text(parseInt($(element).children(".like-count").text()) - 1);
        $(element).children(".like-count, .like-icon").removeClass("active");
    } else {
        $(element).children(".like-count").text(parseInt($(element).children(".like-count").text()) + 1);
        $(element).children(".like-count, .like-icon").addClass("active");
    }
    return true;
}

jsf.ajax.addOnEvent(function (data) {
    $('.max-length').text("(" + maxLength + ")");
});