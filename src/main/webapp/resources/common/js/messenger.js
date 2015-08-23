var wsocket = '';
var friends = [];

$(document).ready(function () {
    messengerFriends();
    $('#messenger_friends').on('click', 'li', openMessengerBox);
    $(document).on('click', '.messageButton', openMessengerBox);
    $(document).on('click', '.close', function () {
        $('#box-' + $(this).attr('socialprofileid')).remove();
    });
    $(".messenger").on('click', 'span', (function () {
        $("#messenger_friends").toggle();
        if ($("#messenger_friends").is(":visible")) {
            $("#messenger-menu").css("background-color", "#5a5a5a");
            $("#messenger-menu").css("color", "#fdfdfd");
            $("#messenger-menu").css("border-color", "#5a5a5a");
        } else {
            $("#messenger-menu").css("background-color", "#fdfdfd");
            $("#messenger-menu").css("color", "gray");
        }
        if ($("#messenger_friends").height() > 250) {
            $("#messenger_friends").css("height", "250px");
        } else {
            $("#messenger_friends").css("height", "");
        }
    }));
    $(document).on('click', '.messenger_title', function () {
        if ($(this).parent().height() > 33) {
            $(this).parent().css("height", "33px");
            $(this).parent().css("margin-top", "210px");

        } else {
            $(this).parent().css("height", "243px");
            $(this).parent().css("margin-top", "0");
        }
    });
    $(document).on('DOMMouseScroll mousewheel', '#messenger_friends, .messages', preventScrolling);
    $(document).on('keypress', '.send-message', function (e) {
        if (e.keyCode == 13 && !e.shiftKey)
        {
            e.preventDefault();
            sendMessage(this);
        }
    });
    $(document).on('focus', '.send-message', function () {
        $(this).closest(".box").find(".new-messages").remove();
        $(this).closest(".box").find(".new").removeClass("new").addClass("old");
        $(this).closest(".box").css("background-color", "#9d9d9d");
        json = '{"senderId":"' + $(this).closest(".box").attr("socialprofileid") + '", "receiverId":"' + logged_social_profile_id + '", "type":"MSG_SENT"}';
        wsocket.send(json);
    });
});

window.onbeforeunload = function () {
    $.each(Cookies.get(), function (name, value) {
        if (/box/.test(name)) {
            Cookies.remove(name);
        }
    });
    $.each($(".box"), function () {
        if ($(this).height() === 33) {
            Cookies.set('collapsed-' + $(this).attr("id"), $(this).attr("socialprofileid"));
        } else {
            Cookies.set($(this).attr("id"), $(this).attr("socialprofileid"));
        }
    });
};

function messengerFriends() {
    $.ajax({
        type: "GET",
        url: "/webresources/messengerFriends",
        data: {
            "socialProfileId": logged_social_profile_id
        },
        dataType: 'json',
        success: function (friends) {
            $('#messenger_friends p').html('');
            $('#messenger_friends li').remove();
            var friends_ids = "";
            for (var i = 0; i < friends.length; i++)
            {
                friends_ids += friends[i].id + ",";
                $('#messenger_friends').append(
                        "<li id='friend_" + friends[i].id + "' name='" + friends[i].name + "' socialprofileid='" + friends[i].id + "'>"
                        + "<img class='friend-photo' src='" + friends[i].photo + "'/>"
                        + "<div class='friend-name'>" + friends[i].name + "</div>"
                        + "</li>");
                $('.friend-name').each(function () {
                    if ($(this).text().length > 26) {
                        $(this).text($(this).text().substring(0, 23) + "...");
                    }
                })
            }
            wsocket = new WebSocket("ws://" + window.location.host + "/socket/" + logged_social_profile_id + "/" + encodeURIComponent(friends_ids));
            wsocket.onmessage = onMessageReceived;
        }
    });
}

function onMessageReceived(evt) {
    var msg = JSON.parse(evt.data); // native API
    if (typeof msg.status !== 'undefined') {
        if (msg.status === "online" && friends.indexOf(msg.id) === -1) {
            friends.push(msg.id);
        } else if (msg.status === "offline" && friends.indexOf(msg.id) !== -1) {
            friends.pop(msg.id);
        }
        $('#messenger-menu strong').text('(' + friends.length + ')');
        $.each($("#messenger_friends li"), function () {
            $(this).find(".friend-online").remove();
            if (friends.indexOf($(this).attr("socialprofileid")) !== -1) {
                $(this).find(".friend-name").append("<img class='friend-online' src='../../resources/common/images/online-dot.png' />");
                var friend = $(this);
                $(this).remove();
                $("#messenger_friends").prepend(friend);
            }
        });
        $.each($(".messenger_boxes .box"), function () {
            $(this).find(".friend-online").remove();
            if (friends.indexOf($(this).attr("socialprofileid")) !== -1) {
                $(this).find(".messenger_title").append("<img class='friend-online' src='../../resources/common/images/online-dot.png' />");
            }

        });
    } else if (typeof msg.message !== 'undefined') {
        if (msg.himself === true) {
            showBox(msg.receiverId, msg.receiverName, msg.message, msg.received, logged_social_profile_id);
        } else {
            showBox(msg.senderId, msg.senderName, msg.message, msg.received, null);
            showNewMessagesQuantity(msg.senderId);
        }
    } else if (typeof msg.offlineMessages !== 'undefined') {
        var offlineMessages = JSON.parse(msg.offlineMessages);
        $.each(offlineMessages, function () {
            showBox(this.senderId, this.senderName, this.message, this.received, null);
        });
        showNewMessagesQuantity(null);
    } else if (typeof msg.historyMessages !== 'undefined') {
        var historyMessages = JSON.parse(msg.historyMessages);
        var messageContainer = '';
        var id;
        $.each(historyMessages, function () {
            messageContainer += loadMessageBlock(this.senderId, this.message, this.received, false);
            id = (logged_social_profile_id === this.senderId) ? this.receiverId : this.senderId;
        });
        $('#box-' + id + ' .messages').prepend(messageContainer);
        $('#box-' + id + ' .messages').scrollTop($('#box-' + id + ' .messages').prop("scrollHeight"));
    } else if (typeof msg.onlineUsers !== 'undefined') {
        $('#registered_users_online').text(msg.onlineUsers + " online");
    }
    persistBoxesStates();
}

function showBox(id, name, message, received, himself) {
    var json;
    if ($('#box-' + id).length === 0) {
        $('.messenger_boxes').append(createBox(id, name));
        json = '{"senderId":"' + id + '", "receiverId":"' + logged_social_profile_id + '", "type":"MSG_HISTORY"}';
        wsocket.send(json);
    }
    if (message !== null) {
        var messageContainer = loadMessageBlock((himself !== null) ? himself : id, message, received, true);
        $('#box-' + id + ' .messages').append(messageContainer);
    }
    $('#box-' + id + ' .messages').scrollTop($('#box-' + id + ' .messages').prop("scrollHeight"));
    $('#box-' + id).show();
}

function createBox(id, name) {
    if (name.length > 23) {
        name = name.substring(0, 20) + "...";
    }
    var online = $("#messenger_friends li[socialprofileid=" + id + "]").find(".friend-online").length ? "<img class='friend-online' src='../../resources/common/images/online-dot.png' />" : "";
    var box = "<div class='box' id='box-" + id + "' socialprofileid='" + id + "'>"
            + "<div class='messenger_title'>" + name
            + "<img src='../../resources/common/images/close.png' class='close' socialprofileid='" + id + "'/>"
            + online
            + "</div>"
            + "<div class='messenger-content'>"
            + "<div class='messages'></div>"
            + "<div class='textarea-container'><textarea id='send-message-" + id + "' class='send-message' socialprofileid='" + id + "' type='text' value=''/></div>"
            + "</div>"
            + "</div>";
    return box;
}

function openMessengerBox() {
    var name = $(this).attr('name');
    var id = $(this).attr('socialprofileid');
    if ($('#box-' + id).length) {
        $('#box-' + id).remove();
    } else {
        var messenger_boxes_count = $('.messenger_boxes .box').size();
        if (messenger_boxes_count !== 0) {
            var messenger_boxes_width = $('.messenger_boxes .box').css('width').replace('px', '');
            var body_size = $('body').css('width').replace('px', '');
        }
        if (messenger_boxes_count === 0 || body_size * 2 / 3 > messenger_boxes_width * (messenger_boxes_count + 1)) {
            showBox(id, name, null, null, null);
            $('#send-message-' + id).focus().select();
        }
    }
}

function sendMessage(input) {
    var id = $(input).attr('socialprofileid');
    var message = $('#send-message-' + id).val();
    message = message.replace(new RegExp('\r?\n', 'g'), ' ').trim();
    if (message !== "") {
        var json = '{"message":"' + message + '", "senderId":"' + logged_social_profile_id + '",'
                + '"receiverId":"' + id + '", "type":"NEW_MSG"}';
        wsocket.send(json);
        $('#box-' + id + ' #send-message-' + id).val("");
    }
}

function loadMessageBlock(id, message, date, recent) {
    var classe = (id === logged_social_profile_id ? "your-message" : "friend-message");
    var direction = (id === logged_social_profile_id ? "right" : "left");
    var time = recent ? "new" : "old";
    var container = "<div class='message " + classe + " " + time + "'>"
            + "<div class='message-inner-container icon-container'><img class='float-" + direction + "' src='../../resources/common/images/triangle-" + direction + ".png'/></div>"
            + "<div class='message-inner-container'><p class='float-" + direction + " message-text'>" + message + "</p></div>"
            + "<div class='message-inner-container'><p class='float-" + direction + " message-date'>" + date + "</p></div>"
            + "</div>";
    return container;
}

function preventScrolling(ev) {
    var $this = $(this),
            scrollTop = this.scrollTop,
            scrollHeight = this.scrollHeight,
            height = $this.height(),
            delta = (ev.type == 'DOMMouseScroll' ?
                    ev.originalEvent.detail * -40 :
                    ev.originalEvent.wheelDelta),
            up = delta > 0;

    var prevent = function () {
        ev.stopPropagation();
        ev.preventDefault();
        ev.returnValue = false;
        return false;
    }

    if (!up && -delta > scrollHeight - height - scrollTop) {
        // Scrolling down, but this will take us past the bottom.
        $this.scrollTop(scrollHeight);

        return prevent();
    } else if (up && delta > scrollTop) {
        // Scrolling up, but this will take us past the top.
        $this.scrollTop(0);
        return prevent();
    }
}

function showNewMessagesQuantity(id) {
    if (id !== null) {
        var box = $("#box-" + id);
        if (!box.find(".send-message").is(':focus')) {
            var length = box.find(".friend-message.new").length;
            if (length > 0) {
                box.children(".messenger_title").children(".new-messages").remove();
                box.children(".messenger_title").append("<span class='new-messages'> [" + length + "]</span>");
                box.css("background-color", "gray");
                $("#new-message-sound")[0].play();
            }
        } else {
            box.find(".new").removeClass("new").addClass("old");
        }
    } else {
        $.each($(".box"), function () {
            var length = $(this).find(".friend-message.new").length;
            if (length > 0) {
                $(this).children(".messenger_title").children(".new-messages").remove();
                $(this).children(".messenger_title").append("<span class='new-messages'> [" + length + "]</span>");
                $(this).css("background-color", "gray");
            }
        });
    }
}

function persistBoxesStates() {
    $.each(Cookies.get(), function (name, value) {
        if (/box/.test(name)) {
            if (!$("#box-" + value).length) {
                var friendName = $('#messenger_friends').find("li[socialprofileid=" + value + "]").attr("name");
                showBox(value, friendName, null, null, null);
            }
            if (/collapsed/.test(name)) {
                $("#box-" + value).css("height", "33px");
                $("#box-" + value).css("margin-top", "210px");
            }
            Cookies.remove(name);
        }
    });
}