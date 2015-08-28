var wsocket = '';
var friends = [];
var focus = false;
var sound = true;

$(document).ready(function () {
    messengerFriends()
    $(document).on('click', '#messenger-friends li', function () {
        openMessengerBox($(this).attr('socialprofileid'), $(this).attr('name'));
    });
    $(document).on('click', '.close-chat', function () {
        var box = $('#box-' + $(this).attr('socialprofileid'));
        organizeBoxes(box);
        $(box).remove();
    });
    $(".messenger").on('click', 'span', (function () {
        $("#messenger-friends").toggle();
        if ($("#messenger-friends").is(":visible")) {
            $("#messenger-menu").removeClass("messenger-menu-collapsed");
        } else {
            $("#messenger-menu").addClass("messenger-menu-collapsed");
        }
    }));
    $(document).on('click', '.messenger-title', function () {
        if (!$(this).parent().hasClass("box-collapsed")) {
            $(this).parent().addClass("box-collapsed");
        } else {
            $(this).parent().removeClass("box-collapsed");
        }
    });
    $(document).on('DOMMouseScroll mousewheel', '#messenger-friends, .messages', preventScrolling);
    $(document).on('keypress', '.send-message', function (e) {
        if (e.keyCode == 13 && !e.shiftKey)
        {
            e.preventDefault();
            sendMessage(this);
        }
    });
    $(document).on('focus', '.send-message', function () {
        focusChat($(this).attr("socialprofileid"));
    });
});

window.onbeforeunload = function () {
    $.each(Cookies.get(), function (name, value) {
        if (/box/.test(name)) {
            Cookies.remove(name);
        }
    });
    $.each($(".box"), function () {
        if ($(this).hasClass("box-collapsed")) {
            Cookies.set('collapsed_' + $(this).attr("id"), $(this).find(".messenger-user-name").text());
        } else {
            Cookies.set($(this).attr("id"), $(this).find(".messenger-user-name").text());
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
            $('#messenger-friends p').html('');
            $('#messenger-friends li').remove();
            var friendsIds = "";
            for (var i = 0; i < friends.length; i++)
            {
                friendsIds += friends[i].id + ",";
                $('#messenger-friends').append(
                        "<li name='" + friends[i].name + "' socialprofileid='" + friends[i].id + "'>"
                        + "<img class='friend-photo' src='" + friends[i].photo + "'/>"
                        + "<div class='friend-name'>" + friends[i].name + "</div>"
                        + "</li>");
                $('.friend-name').each(function () {
                    $(this).text(changeNameLength($(this).text(), 26));
                })
            }
            wsocket = new WebSocket("ws://" + window.location.host + "/socket/" + logged_social_profile_id + "/" + encodeURIComponent(friendsIds));
            wsocket.onopen = persistBoxesAfterPageReload;
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
        $.each($("#messenger-friends li"), function () {
            $(this).find(".friend-online").remove();
            if (friends.indexOf($(this).attr("socialprofileid")) !== -1) {
                $(this).find(".friend-name").append("<img class='friend-online' src='../../resources/common/images/online-dot.png' />");
                var friend = $(this);
                $(this).remove();
                $("#messenger-friends").prepend(friend);
            }
        });
        $.each($(".messenger-boxes .box"), function () {
            $(this).find(".friend-online").remove();
            if (friends.indexOf($(this).attr("socialprofileid")) !== -1) {
                $(this).find(".messenger-title").append("<img class='friend-online' src='../../resources/common/images/online-dot.png' />");
            }

        });
    } else if (typeof msg.message !== 'undefined') {
        if (msg.himself === true) {
            showBox(msg.receiverId, msg.receiverName, msg.message, msg.date, logged_social_profile_id, true);
        } else {
            showBox(msg.senderId, msg.senderName, msg.message, msg.date, null, true);
            showNewMessagesQuantity(msg.senderId);
        }
    } else if (typeof msg.offlineMessages !== 'undefined') {
        var offlineMessages = JSON.parse(msg.offlineMessages);
        $.each(offlineMessages, function () {
            showBox(this.id, this.name, null, null, null, null);
        });
        sound = false;
    } else if (typeof msg.lastMessages !== 'undefined') {
        var lastMessages = JSON.parse(msg.lastMessages);
        var messageContainer = '';
        var id;
        $.each(lastMessages, function () {
            var recent = this.read === "N" ? true : false;
            messageContainer += loadMessageBlock(this.senderId, this.message, this.date, recent);
            id = (logged_social_profile_id === this.senderId) ? this.receiverId : this.senderId;
        });
        $('#box-' + id + ' .messages').prepend(messageContainer);
        $('#box-' + id + ' .messages').scrollTop($('#box-' + id + ' .messages').prop("scrollHeight"));
        showNewMessagesQuantity(id);
        if (focus) {
            focusChat(id);
            focus = false;
        }
    } else if (typeof msg.onlineUsers !== 'undefined') {
        $('#registered-users-online').text(msg.onlineUsers + " online");
    } else if (typeof msg.saw !== 'undefined') {
        messagesViewed($("#box-" + msg.saw));
    }
}

function showBox(id, name, message, date, himself, recent) {
    var json;
    if (!$('#box-' + id).length) {
        $('.messenger-boxes').append(createBox(id, name));
        json = '{"senderId":"' + id + '", "receiverId":"' + logged_social_profile_id + '", "type":"LAST_MSGS"}';
        wsocket.send(json);
    } else if (message !== null) {
        var friendId;
        if (himself !== null) {
            friendId = himself;
            messagesViewed($("#box-" + id));
        } else {
            friendId = id;
        }
        var messageContainer = loadMessageBlock(friendId, message, date, recent);
        $('#box-' + id + ' .messages').append(messageContainer);
    }
    $('#box-' + id + ' .messages').scrollTop($('#box-' + id + ' .messages').prop("scrollHeight"));
    $('#box-' + id).show();
}

function createBox(id, name) {
    name = changeNameLength(name, 23);
    var boxesQuantity = $(".box").length;
    var position = "style='left: " + ((243 * boxesQuantity) + (20 * (boxesQuantity + 1))) + "px'"
    var online = $("#messenger-friends li[socialprofileid=" + id + "]").find(".friend-online").length ? "<img class='friend-online' src='../../resources/common/images/online-dot.png' />" : "";
    var box = "<div class='box' id='box-" + id + "' socialprofileid='" + id + "' " + position + ">"
            + "<div class='messenger-title'><span class='messenger-user-name'>" + name + "</span>"
            + "<img src='../../resources/common/images/close-chat.png' class='close-chat' socialprofileid='" + id + "'/>"
            + online
            + "</div>"
            + "<div class='messenger-content'>"
            + "<div class='messages'></div>"
            + "<div class='textarea-container'><textarea id='send-message-" + id + "' class='send-message' socialprofileid='" + id + "' type='text' value=''/></div>"
            + "</div>"
            + "</div>";
    return box;
}

function openMessengerBox(id, name) {
    if (!$('#box-' + id).length) {
        var boxesQuantity = $('.messenger-boxes .box').size();
        if (boxesQuantity !== 0) {
            var boxWidth = $('.messenger-boxes .box').css('width').replace('px', '');
            var bodySize = $('body').css('width').replace('px', '');
        }
        if (boxesQuantity === 0 || bodySize * 0.75 > boxWidth * (boxesQuantity + 1)) {
            showBox(id, name, null, null, logged_social_profile_id, null);
            $('#send-message-' + id).focus().select();
            focus = true;
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
    var box = $("#box-" + id);
    var length = box.find(".friend-message.new").length;
    if (!box.find(".send-message").is(':focus')) {
        if (length > 0) {
            box.children(".messenger-title").children(".new-messages").remove();
            box.children(".messenger-title").append("<span class='new-messages'> [" + length + "]</span>");
            box.css("background-color", "gray");
            if (sound) {
                $("#new-message-sound")[0].play();
            }
            sound = true;
        }
    } else {
        if (length > 0) {
            box.find(".new").removeClass("new").addClass("old");
        }
    }
}

function persistBoxesAfterPageReload() {
    $.each(Cookies.get(), function (cookieName, name) {
        if (/box/.test(cookieName)) {
            var id = cookieName.split("-")[1];
            if ($("#box-" + id).length === 0) {
                showBox(id, name, null, null, logged_social_profile_id, null);
            }
            if (/collapsed/.test(cookieName)) {
                $("#box-" + id).addClass("box-collapsed")
            }
            Cookies.remove(cookieName);
        }
    });
}

function focusChat(id) {
    var box = $("#box-" + id);
    if (box.find(".friend-message.new").length) {
        messagesViewed(box);
        json = '{"senderId":"' + box.attr("socialprofileid") + '", "receiverId":"' + logged_social_profile_id + '", "type":"MSG_SENT"}';
        wsocket.send(json);
    }
}

function messagesViewed(box) {
    box.find(".new-messages").remove();
    box.find(".new").removeClass("new").addClass("old");
    box.css("background-color", "#9d9d9d");
}

function organizeBoxes(box) {
    var nextBox = $(box).next();
    if (nextBox.length) {
        organizeBoxes(nextBox);
        nextBox.css("left", box.offset().left);
    }
}
