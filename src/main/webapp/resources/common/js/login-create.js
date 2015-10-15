$('.carousel').slick({
    slidesToShow: 5,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 2000,
    arrows: false,
    pauseOnHover: false
});

$('select').each(changeSelectColors);
$('select').on("change", changeSelectColors);
$('select').on("change", checkLength);
function changeSelectColors() {
    if ($(this).children('option:first-child').is(':selected')) {
        $(this).css("color", "gray");
    } else {
        $(this).css("color", "black");
    }
    $(this).children("option").not(":eq(0)").css("color", "black");
    $(this).children("option:first-child").css("color", "gray");
}
function checkLength() {
    if ($(this).children("option:selected").text().trim().length > 45) {
        var string = $(this).children("option:selected").text();
        $(this).children("option:selected").text(string.substring(0, 45) + "...");
    }
}

$(document).on("keypress", "input", function (event) {
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
            $('select').each(changeSelectColors);
            $('select').on("change", changeSelectColors);
            break;
    }
});

$(document).ready(function () {
    $.post("../ping.html");
    window.setInterval(function () {
        $.post("../ping.html");
    }, 1500000);
    $(".name").focus();
})
