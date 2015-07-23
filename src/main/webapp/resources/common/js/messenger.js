locale = $("#localeAcronym").val();
var sendMessageButton = "";
locale == "enUS" ? sendMessageButton = "send" : locale == "frFR" ? sendMessageButton = "envoyer" : sendMessageButton = "enviar";

var wsocket = '';
var friends = [];

$(document).ready(function () {

    messengerFriends();
    $('#messenger_friends').on('click', 'li', openMessengerBox);
    $(document).on('click', '.messageButton', openMessengerBox);
    $(document).on('click', '#messageInputs #sendMessage', sendMessage);
    $(document).on('click', '#close', function () {
        $('#msg_' + $(this).attr('socialprofileid')).hide();
    });
    $(".messenger").on('click', 'span', (function () {
        $("#messenger_friends").toggle();
        if ($("#messenger_friends").height() > 250) {
            $("#messenger_friends").css("height", "250px");
        } else {
            $("#messenger_friends").css("height", "");
        }
    }));
    $(document).on('keypress', '#messageInputs .textmessage', function (e) {
        if (e.which == 13) {
            $(this).parent().find('#sendMessage').click();
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
                $('#messenger_friends').append("<li id='friend_" + friends[i].id + "' name='" + friends[i].name + "' socialprofileid='" + friends[i].id + "'><span id='friend_photo'/>" + friends[i].name + "</li>");
                $("#friend_" + friends[i].id + " #friend_photo").css("background", "url('" + friends[i].photo + "') no-repeat center center");
                $("#friend_" + friends[i].id + " #friend_photo").css("background-size", "25px");
                $("#messenger_friends li").css("color", "red");
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
            $(this).css("color", "red");
            if (friends.indexOf($(this).attr("socialprofileid")) !== -1) {
                $(this).css("color", "green");
                var friend = $(this);
                $(this).remove();
                $("#messenger_friends").prepend(friend);
            }
        });
    } else if (typeof msg.message !== 'undefined') {
        showBox(msg.senderId, msg.senderName, msg.message);
    } else if (typeof msg.offlineMessages !== 'undefined') {
        var offlineMessages = JSON.parse(msg.offlineMessages);
        $.each(offlineMessages, function () {
            showBox(this.senderId, this.senderName, this.message);
        });
    }
}

function showBox(id, name, message) {
    var json;
    if ($('#msg_' + id).length === 0) {
        $('.messenger_boxes').append(createBox(id, name));
        getMessagesHistory(id);
    }
    if (message !== null) {
        $('#msg_' + id + ' #messages').append($("<p class='new'>" + name + ": " + message + "</p>"));
    }
    $('#msg_' + id + ' #messages').scrollTop($('#msg_' + id + ' #messages').prop("scrollHeight"));
    $('#msg_' + id).show();
    json = '{"senderId":"' + id + '", "receiverId":"' + logged_social_profile_id + '", "type":"MSG_SENT"}';
    wsocket.send(json);
}

//passar via websocket
function getMessagesHistory(id) {
    $.ajax({
        url: "/webresources/messagesHistory",
        dataType: "json",
        async: false,
        data: {
            "loggedSocialProfileId": logged_social_profile_id,
            "friendSocialProfileId": id
        },
        success: function (history) {
            for (var i = 0; i < history.length; i++) {
                //appendar de uma vez só
                $('#msg_' + id + ' #messages').append("<p class='old'>" + history[i].name + ": " + history[i].message + "</p>");
                $('#msg_' + id + ' #messages').scrollTop($('#msg_' + id + ' #messages').prop("scrollHeight"));
            }
        }

    });
}

function createBox(id, name) {
    var box = "<li id='msg_" + id + "'>"
            + "<span class='messenger_photo'></span>"
            + "<span class='messenger_title'>" + name + "</span>"
            + "<span id='close' socialprofileid='" + id + "'></span>"
            + "<div id='messages'></div>"
            + "<div id='messageInputs'>"
            + "<input id='textmessage_" + id + "' class='textmessage' type='text' value=''></input>"
            + "<input socialprofileid=" + id + " id='sendMessage' type='button' value='" + sendMessageButton + "'></input>"
            + "</div></li>";
    return box;
}

function openMessengerBox() {
    var name = $(this).attr('name');
    var id = $(this).attr('socialprofileid');
    if ($('#msg_' + id).length > 0) {
        if ($('#msg_' + id).css('visibility') === "hidden") {
            $('#msg_' + id).show();
            $('#textmessage_' + id).focus().select();
        } else {
            $('#msg_' + id).remove();
        }
    } else {
        var messenger_boxes_count = $('.messenger_boxes li').size();
        if (messenger_boxes_count !== 0) {
            var messenger_boxes_width = $('.messenger_boxes li').css('width').replace('px', '');
            var body_size = $('body').css('width').replace('px', '');
        }
        if (messenger_boxes_count === 0 || body_size * 1 / 2 > messenger_boxes_width * (messenger_boxes_count + 1)) {
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