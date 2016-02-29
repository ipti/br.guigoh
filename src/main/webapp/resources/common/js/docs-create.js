var saveTimer;
var imageFieldName;

$(document).ready(function () {
    $(".menu-icon-four").parent().addClass("active");
    tinymce.init({
        selector: '#editor',
        height: 450,
        statusbar: false,
        plugins: [
            'advlist autolink lists link image charmap print preview anchor',
            'searchreplace visualblocks fullscreen',
            'insertdatetime table contextmenu paste'
        ],
        toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image history',
        relative_urls: false,
        setup: function (ed) {
            ed.addButton('history', {
                title: 'Exibir histórico',
                image: '../resources/common/images/history.png',
                onclick: function () {
                    $(".load-history").click();
                }
            });
            ed.on('keyup change undo redo', function (e) {
                if (tinymce.activeEditor.getBody().getAttribute('contenteditable') != "false") {
                    $(".collaborator-user").each(function () {
                        if (logged_social_profile_id == $(this).find(".user-id").text()) {
                            $(this).find(".user-situation").css("color", "darkgoldenrod");
                        }
                    });
                    var json = "";
                    if (getParameterByName("id") != "") {
                        json = '{"doc":"' + $("#doc-id").val() + '", "user":"' + logged_social_profile_id + '", "action":"EDITING_ON"}';
                        websocketDocs.send(json);
                        var text = tinyMCE.activeEditor.getContent();
                        json = '{"doc":"' + $("#doc-id").val() + '", "text":"' + text.replace(/"/g, "'").replace(/\n/g, "\\n") + '", "action":"UPDATE"}';
                        websocketDocs.send(json);
                    }
                    clearTimeout(saveTimer);
                    saveTimer = setTimeout(function () {
                        $(".collaborator-user").each(function () {
                            if (logged_social_profile_id == $(this).find(".user-id").text()) {
                                $(this).find(".user-situation").css("color", "rgb(51, 51, 51)");
                            }
                        });
                        $("#text").val(tinyMCE.activeEditor.getContent());
                        $(".save-text").click();
                        $(".doc-saved").addClass("visible");
                        if (getParameterByName("id") != "") {
                            json = '{"doc":"' + $("#doc-id").val() + '", "user":"' + logged_social_profile_id + '", "action":"EDITING_OFF"}';
                            websocketDocs.send(json);
                        }
                        setTimeout(function () {
                            $(".doc-saved").removeClass("visible");
                        }, 5000);
                    }, 3000);
                }
            });
        },
        init_instance_callback: function (editor) {
            editor.setContent($("#text").val());
            $(".guest-user").each(function () {
                if (logged_social_profile_id == $(this).find(".user-id").text()) {
                    $(".doc-title").attr("readonly", "true");
                    if ($(this).find(".user-permission").text() == "R") {
                        tinymce.activeEditor.getBody().setAttribute('contenteditable', false);
                    }
                }
            });
        },
        file_browser_callback: function (field_name, url, type, win) {
            if (type == 'image') {
                $("#image").click();
                imageFieldName = win.document.getElementById(field_name);
            }
        }
    });

    $("#image").change(function () {
        if (this.files && this.files[0]) {
            var FR = new FileReader();
            FR.onload = function (e) {
                imageFieldName.value = e.target.result;
            };
            FR.readAsDataURL(this.files[0]);
        }
        $("#image").val("");
    });

    $(".creator-user span, .guest-user span").each(function () {
        $(this).text(changeNameLength($(this).text(), 20));
    });

    if (getParameterByName("id") != "") {
        connectWebSocket();
    }

    $(document).on('DOMMouseScroll mousewheel', '.chat-messages', preventScrolling);
    $('.chat-messages').scrollTop($('.chat-messages').prop("scrollHeight"));
});

function onMessageReceivedForDocs(evt) {
    var msg = JSON.parse(evt.data); // native API
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
    } else if (typeof msg.title !== 'undefined') {
        $("#title").val(msg.title);
    } else if (typeof msg.text !== 'undefined') {
        tinyMCE.activeEditor.setContent(msg.text);
    } else if (msg.action === 'KICK') {
        $(".guest-user").each(function () {
            if ($(this).children(".user-id").text() === msg.user) {
                $(this).remove();
            }
        });
        if (Number(msg.user) === logged_social_profile_id) {
            document.getElementById("open-kicked-user-modal").click();
        }
    } else if (typeof msg.dateToHimself !== 'undefined') {
        $(".chat-message .chat-message-date:empty:first").text(msg.dateToHimself);
        $('.chat-messages').scrollTop($('.chat-messages').prop("scrollHeight"));
    } else if (typeof msg.chatMessage !== 'undefined') {
        loadChatMessageBlock(msg.user, msg.userName, msg.chatMessage, msg.date);
    } else if (typeof msg.previousMessages !== 'undefined') {
        var previousMessages = JSON.parse(msg.previousMessages);
        $.each(previousMessages, function () {
            loadChatMessageBlock(this.user, this.userName, this.chatMessage, this.date);
        });
    } else if (msg.action === 'PERMISSION') {
        $(".guest-user").each(function () {
            if ($(this).children(".user-id").text() === msg.user) {
                if ($(this).children(".user-situation-container").hasClass("read-and-write")) {
                    $(this).children(".user-situation-container").addClass("read").removeClass("read-and-write");
                    $(this).children(".user-situation-container").children("i").removeClass("fa-pencil").addClass("fa-eye");
                    $(this).children(".user-permission").text("R");
                    tinymce.activeEditor.getBody().setAttribute('contenteditable', false);
                } else {
                    $(this).children(".user-situation-container").removeClass("read").addClass("read-and-write");
                    $(this).children(".user-situation-container").children("i").addClass("fa-pencil").removeClass("fa-eye");
                    $(this).children(".user-permission").text("RW");
                    tinymce.activeEditor.getBody().setAttribute('contenteditable', true);
                }
            }
        });
    } else if (msg.action === "EDITING_ON") {
        var loggedUserPermission;
        $(".collaborator-user").each(function () {
            if ($(this).children(".user-id").text() == msg.user) {
                $(this).find(".user-situation").css("color", "darkgoldenrod");
            }
            if ($(this).children(".user-id").text() == logged_social_profile_id) {
                loggedUserPermission = $(this).children(".user-permission").text();
            }
        });
        if (logged_social_profile_id != msg.user && loggedUserPermission == "RW") {
            tinymce.activeEditor.getBody().setAttribute('contenteditable', false);
        }
    } else if (msg.action === "EDITING_OFF") {
        var loggedUserPermission;
        $(".collaborator-user").each(function () {
            if ($(this).children(".user-id").text() == msg.user) {
                $(this).find(".user-situation").css("color", "rgb(51, 51, 51)");
            }
            if ($(this).children(".user-id").text() == logged_social_profile_id) {
                loggedUserPermission = $(this).children(".user-permission").text();
            }
        });
        if (logged_social_profile_id != msg.user && loggedUserPermission == "RW") {
            tinymce.activeEditor.getBody().setAttribute('contenteditable', true);
        }
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
    if (tinymce.activeEditor.getBody().getAttribute('contenteditable') != "false") {
        clearTimeout(saveTimer);
        if (getParameterByName("id") != "") {
            var json = '{"doc":"' + $("#doc-id").val() + '", "title":"' + $("#title").val() + '", "action":"UPDATE"}';
            websocketDocs.send(json);
        }
        saveTimer = setTimeout(function () {
            $("#text").val(tinyMCE.activeEditor.getContent());
            $(".save-text").click();
            $(".doc-saved").addClass("visible");
            setTimeout(function () {
                $(".doc-saved").removeClass("visible");
            }, 5000);
        }, 3000);
    }
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
                if ($(data.source).hasClass("add") && getParameterByName("id") == "") {
                    window.history.pushState("", "Docs", "/docs/create.xhtml?id=" + $("#doc-id").val());
                    connectWebSocket();
                }
            } else if ($(data.source).hasClass("remove-guest")) {
                var json = '{"doc":"' + $("#doc-id").val() + '", "user":"' + $(data.source).parent().children(".user-id").text() + '", "action":"KICK"}';
                websocketDocs.send(json);
            }
            $(".creator-user span, .guest-user span").each(function () {
                $(this).text(changeNameLength($(this).text(), 20));
            });
        } else if ($(data.source).hasClass("save-text")) {
            if (getParameterByName("id") == "") {
                window.history.pushState("", "Docs", "/docs/create.xhtml?id=" + $("#doc-id").val());
                connectWebSocket();
            }
            $(".guest-user").each(function () {
                if (logged_social_profile_id == $(this).find(".user-id").text()) {
                    $(".doc-title").attr("readonly", "true");
                }
            });
        } else if ($(data.source).hasClass("close-add-guest-modal")) {
            document.getElementById("close-add-guest-modal").click();
        } else if ($(data.source).hasClass("user-situation-container")) {
            if ($(data.source).hasClass("read-and-write")) {
                $(data.source).addClass("read").removeClass("read-and-write");
                $(data.source).children("i").removeClass("fa-pencil").addClass("fa-eye");
                $(data.source).parent().children(".user-permission").text("R");
            } else {
                $(data.source).removeClass("read").addClass("read-and-write");
                $(data.source).children("i").addClass("fa-pencil").removeClass("fa-eye");
                $(data.source).parent().children(".user-permission").text("RW");
            }
            var json = '{"doc":"' + $("#doc-id").val() + '", "user":"' + $(data.source).parent().children(".user-id").text() + '", "action":"PERMISSION"}';
            websocketDocs.send(json);
        } else if ($(data.source).hasClass("load-history")) {
            if ($(".doc-history").length) {
                document.getElementById("open-history-modal").click();
            }
        } else if ($(data.source).hasClass("restore")) {
            tinyMCE.activeEditor.setContent($("#text").val());
            document.getElementById("close-history-modal").click();
            json = '{"doc":"' + $("#doc-id").val() + '", "text":"' + $("#text").val() + '", "action":"UPDATE"}';
            websocketDocs.send(json);
        } else if ($(data.source).hasClass("load-more-history")) {
            $('#modal-history-container').scrollTop($('#modal-history-container').prop("scrollHeight"));
        }
    }
});

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

$(document).on('keypress', '.chat-send-message', function (e) {
    if (e.keyCode == 13 && !e.shiftKey)
    {
        e.preventDefault();
        var message = $(this).val();
        message = message.replace(new RegExp('\r?\n', 'g'), ' ').trim();
        if (message !== "") {
            var json = '{"message":"' + message + '", "doc":"' + $("#doc-id").val() + '",'
                    + '"user":"' + logged_social_profile_id + '", "action":"MESSAGE"}';
            websocketDocs.send(json);
            loadChatMessageBlock(logged_social_profile_id, logged_social_profile_name, message, "", "right");
            $(this).val("");
        }
    }
});

function loadChatMessageBlock(id, name, message, date, direction) {
    direction = (direction !== undefined) ? direction : (id === logged_social_profile_id ? "right" : "left");
    var classe = (direction === "right" ? "chat-your-message" : "chat-friend-message");
    var container = "<div class='chat-message " + classe + "'>"
            + "<div class='chat-message-inner-container'><p class='float-" + direction + " chat-message-sender'>" + name + "</div>"
            + "<div class='chat-message-inner-container'><p class='float-" + direction + " chat-message-text'>" + message + "</p></div>"
            + "<div class='chat-message-inner-container'><p class='float-" + direction + " chat-message-date'>" + date + "</p></div>"
            + "</div>";
    $('.chat-messages').append(container);
    $('.chat-messages').scrollTop($('.chat-messages').prop("scrollHeight"));
}

$(document).on("click", ".doc-collapsed", function () {
    $(this).next().slideToggle("slow");
    if ($(this).find(".fa-plus-circle").length) {
        $(".modal-history-container").find(".fa-minus-circle").addClass("fa-plus-circle").removeClass("fa-minus-circle");
        $(".history-button.restore").not($(this).next().next()).hide();
        $(".doc-history").not($(this).next()).hide();
        $(this).next().next().show();
        $(this).find(".fa-plus-circle").addClass("fa-minus-circle").removeClass("fa-plus-circle");
    } else {
        $(this).next().next().hide();
        $(this).find(".fa-minus-circle").addClass("fa-plus-circle").removeClass("fa-minus-circle");
    }
});

$(document).on("click", ".doc-status", function() {
    if ($(this).children().hasClass("fa-lock")) {
        $(this).children().removeClass("fa-lock").addClass("fa-unlock-alt");
    } else {
        $(this).children().addClass("fa-lock").removeClass("fa-unlock-alt");
    }
});