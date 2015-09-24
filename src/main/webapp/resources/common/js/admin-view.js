$(document).ready(function () {

    $.each($(".result-name a, .subresult"), function () {
        var limit = $(this).hasClass("subresult") ? 25 : 21;
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
            }
        }
    });
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        $.each($(".result-name a, .subresult"), function () {
            var limit = $(this).hasClass("subresult") ? 25 : 21;
            $(this).text(changeNameLength($(this).text(), limit));
        });
    }
});