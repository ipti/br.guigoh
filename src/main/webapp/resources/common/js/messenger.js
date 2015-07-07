locale = $("#localeAcronym").val();
var sendMessageButton = "";
locale == "enUS" ? sendMessageButton = "send" : locale == "frFR" ? sendMessageButton = "envoyer" : sendMessageButton = "enviar";

$(document).ready(function(){
    "use strict";
    
    getCurriculumMessages();
    messengerFriends();
    setInterval(function() {
        messengerFriends()
    }, 60000);
    setInterval(function() {
        getMessages()
    }, 3000);
    $('#messenger_friends').on('click','li',openMessengerBox);
    $(document).on('click','.messageButton',openMessengerBox);
    $(document).on('click','#messageInputs #sendMessage',sendMessage);
    $(document).on('click', '#history', openAllHistory);
    $(document).on('click', '#close', closeMessengerBox);
    $(".messenger").on('click','span',(function(){
        $("#messenger_friends").toggle();
        if($("#messenger_friends").height() > 250){
            $("#messenger_friends").css("height","250px");
        }else{
            $("#messenger_friends").css("height","");
        }
    }));
    $(document).on('keypress','#messageInputs .textmessage',function(e){
        if(e.which == 13){
            $(this).parent().find('#sendMessage').click();
        }
    });
    
    var header = $('#atm-header');
    var content = $('#atm-content');
    var input = $('#atm-input');
    var status = $('#atm-status');
    var myName = false;
    var author = null;
    var logged = false;
    var socket = atmosphere;
    var subSocket;
    var transport = 'websocket';

    // We are now ready to cut the request
    var request = { url: document.location.origin + '/chat',
        contentType : "application/json",
        transport : transport ,
        trackMessageLength : true,
        reconnectInterval : 5000 };


    request.onOpen = function(response) {
        content.html($('<p>', { text: 'Atmosphere connected using ' + response.transport }));
        input.removeAttr('disabled').focus();
        status.text('Choose name:');
        transport = response.transport;

        // Carry the UUID. This is required if you want to call subscribe(request) again.
        request.uuid = response.request.uuid;
    };

    request.onClientTimeout = function(r) {
        content.html($('<p>', { text: 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval }));
        subSocket.push(atmosphere.util.stringifyJSON({ author: author, message: 'is inactive and closed the connection. Will reconnect in ' + request.reconnectInterval }));
        input.attr('disabled', 'disabled');
        setTimeout(function (){
            subSocket = socket.subscribe(request);
        }, request.reconnectInterval);
    };

    request.onReopen = function(response) {
        input.removeAttr('disabled').focus();
        content.html($('<p>', { text: 'Atmosphere re-connected using ' + response.transport }));
    };

    // For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
    request.onTransportFailure = function(errorMsg, request) {
        atmosphere.util.info(errorMsg);
        request.fallbackTransport = "long-polling";
        header.html($('<h3>', { text: 'Atmosphere Chat. Default transport is WebSocket, fallback is ' + request.fallbackTransport }));
    };

    request.onMessage = function (response) {

        var message = response.responseBody;
        try {
            var json = atmosphere.util.parseJSON(message);
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message);
            return;
        }

        input.removeAttr('disabled').focus();
        if (!logged && myName) {
            logged = true;
            status.text(myName + ': ').css('color', 'blue');
        } else {
            var me = json.author == author;
            var date = typeof(json.time) == 'string' ? parseInt(json.time) : json.time;
            addMessage(json.author, json.message, me ? 'blue' : 'black', new Date(date));
        }
    };

    request.onClose = function(response) {
        content.html($('<p>', { text: 'Server closed the connection after a timeout' }));
        if (subSocket) {
            subSocket.push(atmosphere.util.stringifyJSON({ author: author, message: 'disconnecting' }));
        }
        input.attr('disabled', 'disabled');
    };

    request.onError = function(response) {
        content.html($('<p>', { text: 'Sorry, but there\'s some problem with your '
            + 'socket or the server is down' }));
        logged = false;
    };

    request.onReconnect = function(request, response) {
        content.html($('<p>', { text: 'Connection lost, trying to reconnect. Trying to reconnect ' + request.reconnectInterval}));
        input.attr('disabled', 'disabled');
    };

    subSocket = socket.subscribe(request);

    input.keydown(function(e) {
        if (e.keyCode === 13) {
            var msg = $(this).val();

            // First message is always the author's name
            if (author == null) {
                author = msg;
            }

            subSocket.push(atmosphere.util.stringifyJSON({ author: author, message: msg }));
            $(this).val('');

            input.attr('disabled', 'disabled');
            if (myName === false) {
                myName = msg;
            }
        }
    });

    function addMessage(author, message, color, datetime) {
        content.append('<p><span style="color:' + color + '">' + author + '</span> @ ' +
            + (datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()) + ':'
            + (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes())
            + ': ' + message + '</p>');
    }
});


function getMessages(){
    var friend;
    $.ajax({ 
        url: "/webresources/deliverMessages",
        dataType: "json", 
        data: {
            "socialProfileId":logged_social_profile_id
        },
        success: function(friends){
            for (var i=0;i<friends.length;i++){
                friend = friends[i];
                if($('#msg_'+friends[i].id).length==0){
                    if($('#msg_'+friends[i].id+' .flaghistory').length==0){
                        $.ajax({ 
                            url: "/webresources/messagesHistory", 
                            dataType: "json", 
                            data: {
                                "loggedSocialProfileId":logged_social_profile_id,
                                "friendSocialProfileId":friends[i].id
                            },
                            success: function(history){
                                for (var i=0;i<history.length;i++){
                                    if($('#msg_'+friend.id).length==0){
                                        $('.messenger_boxes').append("<li id='msg_"+friend.id+"'><span>"+friend.name+"</span><span id='close'></span><span id='history'></span><div id='messages'></div><div id='messageInputs'><input id='textmessage_"+friend.id+"' class='textmessage' type='text' value=''></input><input type='hidden' class='flaghistory' value='1'/><input socialprofileid="+friend.id+" id='sendMessage' type='button' value='"+sendMessageButton+"'></input></div></li>");
                                    }
                                    $('#msg_'+friend.id+' #messages').append("<p>"+history[i].name +": "+history[i].message+"</p>");
                                    $('#msg_'+friend.id+' #messages').scrollTop($('#msg_'+friend.id+' #messages').prop("scrollHeight"));
                                }
                            }
                            
                        });
                    }
                }
                var message = $("<p class='new_message'>"+friends[i].name +": "+friends[i].message+"</p>");
                $('#msg_'+friends[i].id).show();
                $('#msg_'+friends[i].id+' #messages').append(message);
                $('#msg_'+friends[i].id+' #messages').scrollTop($('#msg_'+friends[i].id+' #messages').prop("scrollHeight"));
            }
        }
    });
}

function getCurriculumMessages(){
    $.ajax({ 
        url: "/webresources/getCurriculumMessages",
        dataType: "json", 
        data: {
            "socialProfileId":logged_social_profile_id
        },
        success: function(messages){
            for(var i=0;i<messages.length;i++){
                $('.curriculum_pop_ups').append("<li class='curriculum_pop_up'><p>"+messages[i].businessName+" está interessado no seu currículo!</p>"
                    + "<p>E-mail: "+messages[i].email+"</p><p>Telefone: "+messages[i].phone+"</p><p>"+messages[i].message+"</p>"
                    + "</li>")
                
                
            }
        }
    });
}

function messengerFriends(){
    $.ajax({
        type:"GET",
        url:"/webresources/messengerFriends",
        data: {
            "socialProfileId":logged_social_profile_id
        },
        dataType: 'json',
        success:function(friends){
            var cont = 0;
            for(var i=0;i<friends.length;i++){
                if(friends[i].online == "true"){
                    cont = cont + 1;
                }
            }
            $('.messenger_container .messenger span').text('('+cont+')'); 
            $('#messenger_friends p').html('');   
            $('#messenger_friends li').remove();    
            for (var i=0;i<friends.length;i++)
            {
                $('#messenger_friends').append("<li id='friend_"+friends[i].id+"' name='"+friends[i].name+"' socialprofileid='"+friends[i].id+"'><span id='friend_photo'/>"+friends[i].name+"</li>");
                $("#friend_"+friends[i].id+" #friend_photo").css("background","url('"+friends[i].photo+"') no-repeat center center");
                $("#friend_"+friends[i].id+" #friend_photo").css("background-size","25px");
                if(friends[i].online == "true"){
                    $('#friend_'+friends[i].id).css("color","green");
                }else{
                    $('#friend_'+friends[i].id).css("color","red");  
                }
            }
        }
    });
}

function closeMessengerBox(){
    var socialProfileId = $(this).attr('socialprofileid');
    $('#msg_'+socialProfileId).css('visibility','hidden');
}

function openMessengerBox(){
    var name = $(this).attr('name');
    var socialProfileId = $(this).attr('socialprofileid');
    var createbox = "<li id='msg_"+socialProfileId+"'><span class='messenger_photo'></span><span class='messenger_title'>"+name+"</span><span id='close' socialprofileid='"+socialProfileId+"'></span><span id='history' socialprofileid='"+socialProfileId+"'></span><div id='messages'></div><div id='messageInputs'><input id='textmessage_"+socialProfileId+"' class='textmessage' type='text' value=''></input><input type='hidden' class='flaghistory' value='1'/><input socialprofileid="+socialProfileId+" id='sendMessage' type='button' value='"+sendMessageButton+"'></input></div></li>";
    if($('#msg_'+socialProfileId).length>0){
        if( $('#msg_'+socialProfileId).css('visibility') == "hidden"){
            $('#msg_'+socialProfileId).css('visibility','visible');
            $('#textmessage_'+socialProfileId).focus().select();
        }
        else{
            $('#msg_'+socialProfileId).remove();
        }
    }else{
        var messenger_boxes_count = $('.messenger_boxes li').size();
        if(messenger_boxes_count != 0){
            var messenger_boxes_width = $('.messenger_boxes li').css('width').replace('px','');
            var body_size = $('body').css('width').replace('px','');
        }
        if(messenger_boxes_count == 0 || body_size * 1/2 > messenger_boxes_width * (messenger_boxes_count + 1)){
                
            $.ajax({ 
                url: "/webresources/messagesHistory", 
                dataType: "json", 
                data: {
                    "loggedSocialProfileId":logged_social_profile_id,
                    "friendSocialProfileId":socialProfileId
                },
                error: function(history){
                    if($('#msg_'+socialProfileId).length==0){
                        $('.messenger_boxes').append(createbox);
                    }
                },
                success:function(history){
                    if($('#msg_'+socialProfileId).length==0){
                        $('.messenger_boxes').append(createbox);
                    }
                
                    for (var i=0;i<history.length;i++){
                        $('#msg_'+socialProfileId+' #messages').append("<p>"+history[i].name +": "+history[i].message+"</p>");
                        $('#msg_'+socialProfileId+' #messages').scrollTop($('#msg_'+socialProfileId+' #messages').prop("scrollHeight"));
                        $('#textmessage_'+socialProfileId).focus().select();
                    }
                }
            });
        }
    }
}

function openAllHistory(event){
    event.preventDefault();
    var socialProfileId = $(this).attr('socialprofileid');
    $('#msg_'+socialProfileId+' #messages p').remove();
    $.ajax({ 
        url: "/webresources/allMessagesHistory", 
        dataType: "json", 
        data: {
            "loggedSocialProfileId":logged_social_profile_id,
            "friendSocialProfileId":socialProfileId
        },
        success:function(history){
                
            for (var i=0;i<history.length;i++){
                $('#msg_'+socialProfileId+' #messages').append("<p>"+history[i].name +": "+history[i].message+"</p>");
                $('#msg_'+socialProfileId+' #messages').scrollTop($('#msg_'+socialProfileId+' #messages').prop("scrollHeight"));
                $('#textmessage_'+socialProfileId).focus().select();
            }
        }
    });
}

function sendMessage(event){
    event.preventDefault();
    var id = $(this).attr('socialprofileid'); 
    var message = $('#textmessage_'+id).val();
    if(message != ""){
        $.ajax({
            type:"GET",
            url:"/webresources/sendMessage",
            data: {
                "socialProfileIdSender":logged_social_profile_id,
                "socialProfileIdReceiver":id,
                "message": message
            },
            dataType: 'json',
            success:function(success){
                $('#msg_'+id+' #messages').append("<p> Você: "+message+"</p>");
                $('#msg_'+id+' #messages').scrollTop($('#msg_'+id+' #messages').prop("scrollHeight"));
                $('#msg_'+id+' #textmessage_'+id).val("");
            }
        });
    }
}