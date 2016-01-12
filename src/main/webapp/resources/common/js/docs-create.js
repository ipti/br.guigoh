var saveTimer;

$(document).ready(function () {
    tinymce.init({
        selector: 'textarea',
        height: 400,
        width: 650,
        statusbar: false,
        plugins: [
            'advlist autolink lists link image charmap print preview anchor',
            'searchreplace visualblocks fullscreen',
            'insertdatetime media table contextmenu paste'
        ],
        toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
        setup: function (ed) {
            ed.on('keyup change', function (e) {
                clearTimeout(saveTimer);
                saveTimer = setTimeout(function () {
                    $("#text").val(tinyMCE.activeEditor.getContent());
                    $(".save-text").click();
                    $(".doc-saved").addClass("visible");
                    setTimeout(function () {
                        $(".doc-saved").removeClass("visible");
                    }, 5000);
                }, 3000);
            });
        },
        init_instance_callback: function (editor) {
            editor.setContent($("#text").val());
        }
    });

    $(".creator-user span, .guest-user span").each(function () {
        $(this).text(changeNameLength($(this).text(), 20));
    });
});

function fillText() {
    $("#text").val(tinyMCE.activeEditor.getContent());
    return true;
}

$("#title").keyup(function (e) {
    clearTimeout(saveTimer);
    saveTimer = setTimeout(function () {
        $("#text").val(tinyMCE.activeEditor.getContent());
        $(".save-text").click();
        $(".doc-saved").addClass("visible");
        setTimeout(function () {
            $(".doc-saved").removeClass("visible");
        }, 5000);
    }, 3000);
});

$("#title").focus(function () {
    var save_this = $(this);
    window.setTimeout(function () {
        save_this.select();
    }, 100);
});

$(".add-guest").click(function () {
    document.getElementById("open-add-guest-modal").click();
});

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        if ($(data.source).hasClass("guest-user-search") || $(data.source).hasClass("user-container")) {
            $.each($(".result-name, .subresult"), function () {
                var limit = $(this).hasClass("subresult") ? 25 : 21;
                $(this).text(changeNameLength($(this).text(), limit));
            });
        } else if ($(data.source).hasClass("add-guest-button") || $(data.source).hasClass("remove-guest")) {
            if ($(data.source).hasClass("add-guest-button")) {
                document.getElementById("close-add-guest-modal").click();
            }
            $(".creator-user span, .guest-user span").each(function () {
                $(this).text(changeNameLength($(this).text(), 20));
            });
        }
    }
});