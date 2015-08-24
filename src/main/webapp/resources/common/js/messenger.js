var wsocket = '';
var friends = [];

$(document).ready(function () {
    messengerFriends();
    $('#messenger-friends').on('click', 'li', openMessengerBox);
    $(document).on('click', '.messageButton', openMessengerBox);
    $(document).on('click', '.close', function () {
        $('#box-' + $(this).attr('socialprofileid')).remove();
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
        messagesViewed($(this).closest(".box"));
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
        if ($(this).hasClass("box-collapsed")) {
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
        $('#registered-users-online').text(msg.onlineUsers + " online");
    }
}

function showBox(id, name, message, received, himself) {
    var json;
    if ($('#box-' + id).length === 0) {
        $('.messenger-boxes').append(createBox(id, name));
        json = '{"senderId":"' + id + '", "receiverId":"' + logged_social_profile_id + '", "type":"MSG_HISTORY", "himself":"' + (himself !== null ? "true" : "false") + '"}';
        wsocket.send(json);
        if (himself !== null) {
            message = null;
        }
    }
    if (message !== null) {
        var friendId;
        if (himself !== null) {
            friendId = himself;
            messagesViewed($("#box-" + id));
        } else {
            friendId = id;
        }
        var messageContainer = loadMessageBlock(friendId, message, received, true);
        $('#box-' + id + ' .messages').append(messageContainer);
    }
    $('#box-' + id + ' .messages').scrollTop($('#box-' + id + ' .messages').prop("scrollHeight"));
    $('#box-' + id).show();
}

function createBox(id, name) {
    name = changeNameLength(name, 23);
    var online = $("#messenger-friends li[socialprofileid=" + id + "]").find(".friend-online").length ? "<img class='friend-online' src='../../resources/common/images/online-dot.png' />" : "";
    var box = "<div class='box' id='box-" + id + "' socialprofileid='" + id + "'>"
            + "<div class='messenger-title'>" + name
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
        var boxesQuantity = $('.messenger-boxes .box').size();
        if (boxesQuantity !== 0) {
            var boxWidth = $('.messenger-boxes .box').css('width').replace('px', '');
            var bodySize = $('body').css('width').replace('px', '');
        }
        if (boxesQuantity === 0 || bodySize * 2 / 3 > boxWidth * (boxesQuantity + 1)) {
            showBox(id, name, null, null, logged_social_profile_id);
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
                box.children(".messenger-title").children(".new-messages").remove();
                box.children(".messenger-title").append("<span class='new-messages'> [" + length + "]</span>");
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
                $(this).children(".messenger-title").children(".new-messages").remove();
                $(this).children(".messenger-title").append("<span class='new-messages'> [" + length + "]</span>");
                $(this).css("background-color", "gray");
            }
        });
    }
}

function persistBoxesAfterPageReload() {
    $.each(Cookies.get(), function (name, value) {
        if (/box/.test(name)) {
            if ($("#box-" + value).length === 0) {
                var friendName = $('#messenger-friends').find("li[socialprofileid=" + value + "]").attr("name");
                showBox(value, friendName, null, null, logged_social_profile_id);
            }
            if (/collapsed/.test(name)) {
                $("#box-" + value).addClass("box-collapsed")
            }
            Cookies.remove(name);
        }
    });
}

function messagesViewed(box) {
    box.find(".new-messages").remove();
    box.find(".new").removeClass("new").addClass("old");
    box.find(".messenger-content").css("height", "inherit");
    box.css("background-color", "#9d9d9d");
}

function changeNameLength(name, limit) {
    return (name.length > limit) ? name.substring(0, limit - 3) + "..." : name;
}