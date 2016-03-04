$(document).ready(function () {

    $.each($(".result-name a, .subresult"), function () {
        var limit = $(this).hasClass("subresult") ? 25 : 20;
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

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        if ($(data.source).hasClass("user-action") || $(data.source).hasClass("object-action")
                || $(data.source).parent().hasClass("load-more") || $(data.source).hasClass("admin-user-search")
                || $(data.source).hasClass("user-container")) {
            $.each($(".result-name, .result-name a, .subresult"), function () {
                var limit = $(this).hasClass("subresult") ? 25 : 20;
                $(this).text(changeNameLength($(this).text(), limit));
            });
        } else if ($(data.source).hasClass("close-add-admin-modal") || $(data.source).hasClass("add-admin-button")) {
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