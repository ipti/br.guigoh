var poster = ["1", "2", "3", "4"];
var rand = poster[Math.floor(Math.random() * poster.length)];
var url = "../resources/common/images/banners/objeto-" + rand + ".jpg";
$(".header-picture img").attr("src", url);

$('.carousel').slick({
    slidesToShow: 5,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 2000,
    arrows: false,
    pauseOnHover: false
});

$(document).on("keypress", ".panel input", function (event) {
    if (event.which == 13) {
        event.preventDefault();
        $(".forward").click();
    }
});
function setCursors(ajax) {
    if (ajax === "begin") {
        $('body').css('cursor', 'wait');
        $('a').css('cursor', 'wait');
    } else {
        $('body').css('cursor', 'auto');
        $('a').css('cursor', 'pointer');
    }
}

jsf.ajax.addOnEvent(function (data) {
    switch (data.status) {
        case "begin":
            setCursors(data.status);
            break;
        case "success":
            setCursors(data.status);
            setFocus();
            break;
    }
});

function setFocus() {
    if ($(".panel-1").length) {
        $(".username").focusTextToEnd();
    } else if ($(".panel-3").length) {
        $(".email-recovery").focusTextToEnd();
    } else if ($(".panel-4").length) {
        $('.secret-answer').focusTextToEnd();
    } else if ($(".panel-5").length) {
        $(".email-recovery").focusTextToEnd();
    } else if ($(".panel-6").length) {
        $(".new-password").focusTextToEnd();
    }
}

$(document).ready(function () {
    $.post("../ping.html");
    window.setInterval(function () {
        $.post("../ping.html");
    }, 1500000);
    $(".username").focus();
})

if (navigator.appName == 'Microsoft Internet Explorer')
{
    window.location = '/resources/notsupported.htm';
}

(function ($) {
    $.fn.focusTextToEnd = function () {
        this.focus();
        var $thisVal = this.val();
        this.val('').val($thisVal);
        return this;
    }
}(jQuery));