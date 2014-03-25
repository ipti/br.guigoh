$(document).ready(function(){
    $.ajax({
        type:"GET",
        url:"/webresources/primata/lastreviewobjects",
        success:function(lastobjects){
            lastobjects = eval(lastobjects);
            if(!(lastobjects instanceof Array)){
                lastobjects = new Array(lastobjects);
                $('.more_objects').css('display','none');
            }else{
                var count = 10;
                if(lastobjects.length < count){
                    $('.more_objects').css('display','none');
                }
                for(var i=0;i<count;i++){
                    if(lastobjects[i] != null){
                        var lastobject = lastobjects[i];
                        var html = $('<div class="public_tec"></div>');
                        var title = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><h2>'+lastobject.title+'</h2></a>';
                        var time = '<font class="clock_public"></font><h3>'+lastobject.to_char+'</h3>';
                        if(typeof lastobject.picture_url != 'undefined'){
                            var thumb = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><span style="background:url('+lastobject.picture_url+') no-repeat;" class="thumb_public"></span></a>';
                        }else{
                            var thumb = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><span class="thumb_public"></span></a>';
                        }
                        var author = ' <div class="autor_public">Enviado por <a href="">'+lastobject.submitter_login+' </a></div>';
                        var theme = '<div class="theme_public"> em <a href="">'+lastobject.comunity+'</a><span class="clear"></span></div>';
                        //var desc = '<h4>Lorem ipsum dolor sit amet, consectetuer ad</h4>';
                        html.append(thumb).append(time).append(title).append(author).append(theme);
                        $('#list_objects').append(html);
                    }
                }
                $(document).on('click', '.more_objects', function(){
                    count += 5;
                    for(var i=count-5;i<count;i++){
                        if(lastobjects[i] != null){
                            var lastobject = lastobjects[i];
                            if(typeof lastobject.picture_url != 'undefined'){
                                var thumb = '<span style="background:url('+lastobject.picture_url+') no-repeat;" class="thumb_public"></span>';
                            }else{
                                var thumb = '<span class="thumb_public"></span>';
                            }
                            var html = $('<div class="public_tec"></div>');
                            var title = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><h2>'+lastobject.title+'</h2></a>';
                            var time = '<font class="clock_public"></font><h3>'+lastobject.to_char+'</h3>';
                            var author = ' <div class="autor_public">Enviado por <a href="">'+lastobject.submitter_login+'</a></div>';
                            var theme = '<div class="theme_public"> em <a href="">'+lastobject.comunity+'</a><span class="clear"></span></div>';
                            //var desc = '<h4>Lorem ipsum dolor sit amet, consectetuer ad</h4>';
                            html.append(thumb).append(time).append(title).append(author).append(theme);
                            $('#list_objects').append(html);
                        }
                    }
                    if(lastobjects.length <= count){
                        $('.more_objects').css('display','none');
                    }
                });
            }
            
        }
    });
    //    $.ajax({
    //        type:"GET",
    //        url:"/webresources/primata/feednews",
    //        success:function(feednews){
    //            feednews = eval(feednews);
    //            if(!(feednews instanceof Array)){
    //                feednews = new Array(feednews);
    //            }else{
    //                for(var i=0;i<feednews.length;i++){
    //                    var feed = feednews[i];
    //                    var userfeed = $("<div class='user_feed'></div>");
    //                    var userphoto = $('<img class="publisher_image"></img>');
    //                    if(feed.profile_id == 0){
    //                        userphoto.attr('src','/resources/images/avatar.png');
    //                    }else{
    //                        //foto do usuário
    //                        userphoto.attr('src','/resources/images/avatar.png');
    //                    }
    //                    var info = $("<font class='text_feed'></font>");
    //                    var username = '<a class="user_news_name" href="">'+feed.nick+'</a>';
    //                    switch (feed.type){
    //                        case 'ST' :
    //                            var whatdo = 'criou a tecnologia social <a class="social_news_name" href="">'+feed.name+'</a>';
    //                            break;
    //                        case 'Doc':
    //                            var whatdo = ' criou o documento <a class="social_news_name" href="">'+feed.name+'</a>';
    //                            break;
    //                        case 'community':
    //                            var whatdo = 'criou a comunidade <a class="social_news_name" href="">'+feed.name+'</a>';
    //                            break;
    //                        case 'cTopic':
    //                            var whatdo = 'criou o tópico <a class="social_news_name" href="">'+feed.name+'</a>';
    //                            break;
    //                                
    //                    }
    //                    
    //                    var wheredo = ' no tema<a class="theme_news_name" href="">'+feed.theme+'</a>';
    //                    info.append(username).append(whatdo).append(wheredo);
    //                    userfeed.append(userphoto).append(info);
    //                    $('#newsfeed').append(userfeed);
    //                }
    //            }
    //        }
    //    });
    
    /*var home_size = 0;
    if($(".left_column").height() > home_size){
        home_size = $(".left_column").height();
    }
    if($(".middle_column").height() > home_size){
        home_size = $(".left_column").height();
    }
    if($(".right_column").height() > home_size){
        home_size = $(".left_column").height();
    }
    $(".left_column").css("height", home_size);
    $(".middle_column").css("height", home_size);
    $(".right_column").css("height", home_size);*/
});