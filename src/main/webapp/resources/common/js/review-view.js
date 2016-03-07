$(document).ready(function () {

    $.each($(".result-name a, .subresult"), function () {
        var limit = $(this).hasClass("subresult") ? 21 : 20;
        $(this).text(changeNameLength($(this).text(), limit));
    });

    $('.tab').click(function () {
        if (!$(this).parent().hasClass('active')) {
            $(this).closest("ul").find(".tab").parent().removeClass('active');
            $(this).parent().addClass('active');
            $(this).closest(".review-tabs").nextAll(':lt(2)').hide();
            if ($(this).hasClass("tab-object-pending")) {
                $('#object-container-pending').show();
            } else if ($(this).hasClass("tab-object-deactivated")) {
                $('#object-container-deactivated').show();
            }
        }
    });

    $('.max-length').text("(200)");
});

$(document).on("click", ".reject-button, .deactivate-reason", function () {
    $(".reason-container").not($(this).parent().find(".reason-container")).hide();
    $(this).parent().find(".reason-container").toggle();
});

$(document).on("click", ".close-reason-container", function () {
    $(this).parent().toggle();
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        $.each($(".result-name a, .subresult"), function () {
            var limit = $(this).hasClass("subresult") ? 21 : 20;
            $(this).text(changeNameLength($(this).text(), limit));
        });
    }
});

$(document).on('keyup', '.close-reason-textarea', function (e) {
    var length = $(this).val().length;
    length = 200 - length;
    $(this).parent().find(".max-length").text("(" + length + ")");
});

function isRejectJustified(data) {
    if ($(data).parent().find("textarea").val() == "") {
        $(data).parent().find(".reason-error").show();
        $(data).parent().find("textarea").css("border", "1px solid red");
        return false;
    } else {
        $(data).parent().find(".reason-error").hide();
        $(data).parent().find("textarea").css("border", "0");
        return true;
    }
}