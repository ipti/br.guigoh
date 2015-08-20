locale = $("#localeAcronym").val();
var sendMessageButton = "";
locale == "enUS" ? sendMessageButton = "send" : locale == "frFR" ? sendMessageButton = "envoyer" : sendMessageButton = "enviar";

var wsocket = '';
var friends = [];

$(document).ready(function () {

    messengerFriends();
    $('#messenger_friends').on('click', 'li', openMessengerBox);
    $(document).on('click', '.messageButton', openMessengerBox);
    $(document).on('click', '.messageInputs .sendMessage', sendMessage);
    $(document).on('click', '.close', function () {
        $('#msg_' + $(this).attr('socialprofileid')).remove();
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
            $(this).next(".messenger-content").hide();
            
        } else {
            $(this).parent().css("height", "243px");
            $(this).parent().css("margin-top", "0");
            $(this).next(".messenger-content").show();
        }
    })
    $('#messenger_friends').on('DOMMouseScroll mousewheel', preventScrolling);
    $(document).on('keypress', '.messageInputs .textmessage', function (e) {
        if (e.which == 13) {
            $(this).parent().find('.sendMessage').click();
        }
    });
});

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
        showBox(msg.senderId, msg.senderName, msg.message);
    } else if (typeof msg.offlineMessages !== 'undefined') {
        var offlineMessages = JSON.parse(msg.offlineMessages);
        $.each(offlineMessages, function () {
            showBox(this.senderId, this.senderName, this.message);
        });
    } else if (typeof msg.historyMessages !== 'undefined') {
        var historyMessages = JSON.parse(msg.historyMessages);
        var messages = '';
        var id;
        $.each(historyMessages, function () {
            messages += "<p class='old'>" + this.senderName + ": " + this.message + "</p>";
            id = (logged_social_profile_id === this.senderId) ? this.receiverId : this.senderId;
        });
        $('#msg_' + id + ' #messages').prepend(messages);
        $('#msg_' + id + ' #messages').scrollTop($('#msg_' + id + ' #messages').prop("scrollHeight"));
    } else if (typeof msg.onlineUsers !== 'undefined') {
        $('#registered_users_online').text(msg.onlineUsers + " online");
    }
}

function showBox(id, name, message) {
    var json;
    if ($('#msg_' + id).length === 0) {
        $('.messenger_boxes').append(createBox(id, name));
        json = '{"senderId":"' + id + '", "receiverId":"' + logged_social_profile_id + '", "type":"MSG_HISTORY"}';
        wsocket.send(json);
    }
    if (message !== null) {
        $('#msg_' + id + ' #messages').append($("<p class='new'>" + name + ": " + message + "</p>"));
    }
    $('#msg_' + id + ' #messages').scrollTop($('#msg_' + id + ' #messages').prop("scrollHeight"));
    $('#msg_' + id).show();
    json = '{"senderId":"' + id + '", "receiverId":"' + logged_social_profile_id + '", "type":"MSG_SENT"}';
    wsocket.send(json);
}

function createBox(id, name) {
    if (name.length > 23) {
        name = name.substring(0, 20) + "...";
    }
    var online = $("#messenger_friends li[socialprofileid=" + id + "]").find(".friend-online").length ? "<img class='friend-online' src='../../resources/common/images/online-dot.png' />" : "";
    var box = "<div class='box' id='msg_" + id + "' socialprofileid='" + id + "'>"
            + "<div class='messenger_title'>" + name
            + "<img src='../../resources/common/images/close.png' class='close' socialprofileid='" + id + "'/>"
            + online
            + "</div>"
            + "<div class='messenger-content'>"
            + "<div class='messages'></div>"
            + "<div class='messageInputs'>"
            + "<input id='textmessage_" + id + "' class='textmessage' type='text' value=''></input>"
            + "<input socialprofileid=" + id + " class='sendMessage' type='button' value='" + sendMessageButton + "'></input>"
            + "</div></div>"
            + "</div>";
    return box;
}

function openMessengerBox() {
    var name = $(this).attr('name');
    var id = $(this).attr('socialprofileid');
    if ($('#msg_' + id).length > 0) {
        if (!$('#msg_' + id).is(":visible")) {
            $('#msg_' + id).show();
            $('#textmessage_' + id).focus().select();
        } else {
            $('#msg_' + id).remove();
        }
    } else {
        var messenger_boxes_count = $('.messenger_boxes .box').size();
        if (messenger_boxes_count !== 0) {
            var messenger_boxes_width = $('.messenger_boxes .box').css('width').replace('px', '');
            var body_size = $('body').css('width').replace('px', '');
        }
        if (messenger_boxes_count === 0 || body_size * 2 / 3 > messenger_boxes_width * (messenger_boxes_count + 1)) {
            showBox(id, name, null);
            $('#textmessage_' + id).focus().select();
        }
    }
}

function sendMessage(event) {
    event.preventDefault();
    var id = $(this).attr('socialprofileid');
    var message = $('#textmessage_' + id).val();
    if (message !== "") {
        var json = '{"message":"' + message + '", "senderId":"' + logged_social_profile_id + '",'
                + '"receiverId":"' + id + '", "type":"NEW_MSG"}';
        wsocket.send(json);
        $('#msg_' + id + ' #messages').append("<p> Você: " + message + "</p>");
        $('#msg_' + id + ' #messages').scrollTop($('#msg_' + id + ' #messages').prop("scrollHeight"));
        $('#msg_' + id + ' #textmessage_' + id).val("");
    }
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
;