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
    $(document).on('click', '#history', openAllHistory);
    $(document).on('click', '#close', closeMessengerBox);
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


function getMessages() {
    var friend;
    $.ajax({
        url: "/webresources/deliverMessages",
        dataType: "json",
        data: {
            "socialProfileId": logged_social_profile_id
        },
        success: function (friends) {
            for (var i = 0; i < friends.length; i++) {
                friend = friends[i];
                if ($('#msg_' + friends[i].id).length == 0) {
                    if ($('#msg_' + friends[i].id + ' .flaghistory').length == 0) {
                        $.ajax({
                            url: "/webresources/messagesHistory",
                            dataType: "json",
                            data: {
                                "loggedSocialProfileId": logged_social_profile_id,
                                "friendSocialProfileId": friends[i].id
                            },
                            success: function (history) {
                                for (var i = 0; i < history.length; i++) {
                                    if ($('#msg_' + friend.id).length == 0) {
                                        $('.messenger_boxes').append("<li id='msg_" + friend.id + "'><span>" + friend.name + "</span><span id='close'></span><span id='history'></span><div id='messages'></div><div id='messageInputs'><input id='textmessage_" + friend.id + "' class='textmessage' type='text' value=''></input><input type='hidden' class='flaghistory' value='1'/><input socialprofileid=" + friend.id + " id='sendMessage' type='button' value='" + sendMessageButton + "'></input></div></li>");
                                    }
                                    $('#msg_' + friend.id + ' #messages').append("<p>" + history[i].name + ": " + history[i].message + "</p>");
                                    $('#msg_' + friend.id + ' #messages').scrollTop($('#msg_' + friend.id + ' #messages').prop("scrollHeight"));
                                }
                            }

                        });
                    }
                }
                var message = $("<p class='new_message'>" + friends[i].name + ": " + friends[i].message + "</p>");
                $('#msg_' + friends[i].id).show();
                $('#msg_' + friends[i].id + ' #messages').append(message);
                $('#msg_' + friends[i].id + ' #messages').scrollTop($('#msg_' + friends[i].id + ' #messages').prop("scrollHeight"));
            }
        }
    });
}

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
                $("#messenger_friends li").css("color","red");
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
        $.each($("#messenger_friends li"), function(){
            $(this).css("color", "red");
            if (friends.indexOf($(this).attr("socialprofileid")) !== -1){
                $(this).css("color", "green");
                var friend = $(this);
                $(this).remove();
                $("#messenger_friends").prepend(friend);
            } 
        });
    }
}

function closeMessengerBox() {
    var socialProfileId = $(this).attr('socialprofileid');
    $('#msg_' + socialProfileId).css('visibility', 'hidden');
}

function openMessengerBox() {
    var name = $(this).attr('name');
    var socialProfileId = $(this).attr('socialprofileid');
    var createbox = "<li id='msg_" + socialProfileId + "'><span class='messenger_photo'></span><span class='messenger_title'>" + name + "</span><span id='close' socialprofileid='" + socialProfileId + "'></span><span id='history' socialprofileid='" + socialProfileId + "'></span><div id='messages'></div><div id='messageInputs'><input id='textmessage_" + socialProfileId + "' class='textmessage' type='text' value=''></input><input type='hidden' class='flaghistory' value='1'/><input socialprofileid=" + socialProfileId + " id='sendMessage' type='button' value='" + sendMessageButton + "'></input></div></li>";
    if ($('#msg_' + socialProfileId).length > 0) {
        if ($('#msg_' + socialProfileId).css('visibility') == "hidden") {
            $('#msg_' + socialProfileId).css('visibility', 'visible');
            $('#textmessage_' + socialProfileId).focus().select();
        }
        else {
            $('#msg_' + socialProfileId).remove();
        }
    } else {
        var messenger_boxes_count = $('.messenger_boxes li').size();
        if (messenger_boxes_count != 0) {
            var messenger_boxes_width = $('.messenger_boxes li').css('width').replace('px', '');
            var body_size = $('body').css('width').replace('px', '');
        }
        if (messenger_boxes_count == 0 || body_size * 1 / 2 > messenger_boxes_width * (messenger_boxes_count + 1)) {

            $.ajax({
                url: "/webresources/messagesHistory",
                dataType: "json",
                data: {
                    "loggedSocialProfileId": logged_social_profile_id,
                    "friendSocialProfileId": socialProfileId
                },
                error: function (history) {
                    if ($('#msg_' + socialProfileId).length == 0) {
                        $('.messenger_boxes').append(createbox);
                    }
                },
                success: function (history) {
                    if ($('#msg_' + socialProfileId).length == 0) {
                        $('.messenger_boxes').append(createbox);
                    }

                    for (var i = 0; i < history.length; i++) {
                        $('#msg_' + socialProfileId + ' #messages').append("<p>" + history[i].name + ": " + history[i].message + "</p>");
                        $('#msg_' + socialProfileId + ' #messages').scrollTop($('#msg_' + socialProfileId + ' #messages').prop("scrollHeight"));
                        $('#textmessage_' + socialProfileId).focus().select();
                    }
                }
            });
        }
    }
}

function openAllHistory(event) {
    event.preventDefault();
    var socialProfileId = $(this).attr('socialprofileid');
    $('#msg_' + socialProfileId + ' #messages p').remove();
    $.ajax({
        url: "/webresources/allMessagesHistory",
        dataType: "json",
        data: {
            "loggedSocialProfileId": logged_social_profile_id,
            "friendSocialProfileId": socialProfileId
        },
        success: function (history) {

            for (var i = 0; i < history.length; i++) {
                $('#msg_' + socialProfileId + ' #messages').append("<p>" + history[i].name + ": " + history[i].message + "</p>");
                $('#msg_' + socialProfileId + ' #messages').scrollTop($('#msg_' + socialProfileId + ' #messages').prop("scrollHeight"));
                $('#textmessage_' + socialProfileId).focus().select();
            }
        }
    });
}

function sendMessage(event) {
    event.preventDefault();
    var id = $(this).attr('socialprofileid');
    var message = $('#textmessage_' + id).val();
    if (message != "") {
//        wsocket.send(message);
//        $.ajax({
//            type: "GET",
//            url: "/webresources/sendMessage",
//            data: {
//                "socialProfileIdSender": logged_social_profile_id,
//                "socialProfileIdReceiver": id,
//                "message": message
//            },
//            dataType: 'json',
//            success: function (success) {
//                $('#msg_' + id + ' #messages').append("<p> Você: " + message + "</p>");
//                $('#msg_' + id + ' #messages').scrollTop($('#msg_' + id + ' #messages').prop("scrollHeight"));
//                $('#msg_' + id + ' #textmessage_' + id).val("");
//            }
//        });
    }
}