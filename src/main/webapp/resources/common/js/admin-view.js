$(document).ready(function () {

    $.each($(".result-name a, .subresult"), function () {
        var limit = $(this).hasClass("subresult") ? 21 : 20;
        $(this).text(changeNameLength($(this).text(), limit));
    });

    $('.tab').click(function () {
        if (!$(this).parent().hasClass('active')) {
            $(this).closest("ul").find(".tab").parent().removeClass('active');
            $(this).parent().addClass('active');
            $(this).closest(".admin-tabs").nextAll(':lt(2)').hide();
            if ($(this).hasClass("tab-user-pending")) {
                $('#user-container-pending').show();
            } else if ($(this).hasClass("tab-user-deactivated")) {
                $('#user-container-deactivated').show();
            } else if ($(this).hasClass("tab-object-pending")) {
                $('#object-container-pending').show();
            } else if ($(this).hasClass("tab-object-deactivated")) {
                $('#object-container-deactivated').show();
            } else if ($(this).hasClass("tab-validation")) {
                $('.admin-general-tab-validation').show();
            } else if ($(this).hasClass("tab-permission")) {
                $('.admin-general-tab-permission').show();
            } else if ($(this).hasClass("tab-administrators")) {
                $('#admin-container').show();
            } else if ($(this).hasClass("tab-revisers")) {
                $('#reviser-container').show();
            }
        }
    });

    $('.max-length').text("(200)");
});

$(document).on("click", ".add-admin", function () {
    document.getElementById("open-password-modal").click();
    $("#password-modal-action").val("add");
    $("#password-search").focus();
});

$(document).on("keypress", "#password-search", function (e) {
    if (e.keyCode == 13) {
        $(".password-button-add").click();
    }
});

var removeButton;
$(document).on("click", ".remove-admin", function () {
    removeButton = $(this).parent().find(".remove-admin-button");
    document.getElementById("open-password-modal").click();
    $("#password-modal-action").val("remove");
    $("#password-search").focus();
});

$(document).on("click", ".interest", function () {
    $(".interest").removeClass("active");
    $(this).addClass("active");
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
        $.each($(".modal-users-container .result-name, .result-name a, .subresult"), function () {
            var limit = $(this).hasClass("subresult") ? 21 : 20;
            $(this).text(changeNameLength($(this).text(), limit));
        });
        if ($(data.source).hasClass("close-add-admin-modal") || $(data.source).hasClass("add-admin-button")) {
            document.getElementById("close-add-admin-modal").click();
        } else if ($(data.source).hasClass("close-password-modal") || $(data.source).hasClass("password-button-cancel")
                || $(data.source).hasClass("remove-admin-button")) {
            document.getElementById("close-password-modal").click();
            $(".password-error").hide();
        } else if ($(data.source).hasClass("password-button-add")) {
            if ($("#password").val() == "true") {
                if ($("#password-modal-action").val() == "add") {
                    document.getElementById("open-add-admin-modal").click();
                    $("#admin-user-search").focus();
                } else if ($("#password-modal-action").val() == "remove") {
                    removeButton.click();
                }
                $(".password-error").hide();
            } else {
                if ($("#password-search").val() == "") {
                    document.getElementById("close-password-modal").click();
                    $(".password-error").hide();
                } else {
                    $(".password-error").show();
                    $("#password-search").focusTextToEnd();
                }
            }
        } else if ($(data.source).hasClass("add-reviser")) {
            document.getElementById("open-add-admin-modal").click();
            $("#admin-user-search").focus();
        }
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