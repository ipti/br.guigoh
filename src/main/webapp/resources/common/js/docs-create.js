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
    
    if (getParameterByName("id") != "") {
        connectWebSocket();
    }
    
    if (logged_social_profile_id != $(".creator-user").find(".user-id").text()) {
        $(".doc-title").attr("readonly", "true");
    }
});

function onMessageReceivedForDocs(evt) {
    var msg = JSON.parse(evt.data); // native API
    console.log(msg);
    if (typeof msg.status !== 'undefined') {
        $(".collaborator-user").each(function () {
            if ($(this).find(".user-id").text() == msg.id) {
                if (msg.status == "online") {
                    $(this).find(".status-icon").attr("src", "../resources/common/images/online-dot.png");
                } else {
                    $(this).find(".status-icon").attr("src", "../resources/common/images/offline-dot.png");
                }
            }
        });
    }
}

function connectWebSocket() {
    websocketDocs = new WebSocket("ws://" + window.location.host + "/socket/docs/" + logged_social_profile_id + "/" + getParameterByName("id"));
    websocketDocs.onmessage = onMessageReceivedForDocs;
}

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
                if (getParameterByName("id") == "") {
                    window.history.pushState("", "Docs", "/docs/create.xhtml?id=" + $("#doc-id").val());
                    connectWebSocket();
                }
            }
            $(".creator-user span, .guest-user span").each(function () {
                $(this).text(changeNameLength($(this).text(), 20));
            });
        } else if ($(data.source).hasClass("save-text")) {
            if (getParameterByName("id") == "") {
                window.history.pushState("", "Docs", "/docs/create.xhtml?id=" + $("#doc-id").val());
                connectWebSocket();
            }
        }
    }
});

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}