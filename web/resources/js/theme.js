$(document).ready(function(){
    
    if($(".discussion_topics_box").height() >= $(".publication_objects_box").height()){
        $(".discussion_topics_box").css("border-right","1px solid #B2B6B1");
    }else{
        $(".publication_objects_box").css("border-left","1px solid #B2B6B1");
    }
    
    $.ajax({
        type:"GET",
        url:"/webresources/primata/lastreviewobjectsbytheme",
        data:{
            "theme_id":theme_id
        },
        success:function(lastobjects){
            lastobjects = eval(lastobjects);
            if(!(lastobjects instanceof Array)){
                lastobjects = new Array(lastobjects);
            }else{
                for(var i=0;i<lastobjects.length;i++){
                    if(lastobjects[i] != null){
                        var lastobject = lastobjects[i];
                        var html = $('<div class="public_tec"></div>');
                        var object_title;
                        if(lastobject.title.length > 16){
                            object_title = lastobject.title.substring(0,13) + "...";
                        }else{
                            object_title = lastobject.title;
                        }
                        var title = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><h2>'+object_title+'</h2></a>';
                        var time = '<font class="clock_public"></font><h3>'+lastobject.to_char+'</h3>';
                        if(typeof lastobject.picture_url != 'undefined'){
                            var thumb = '<a href="/ts/social-tech.do?stid='+lastobject.id+'" ><span style="background:url('+lastobject.picture_url+') no-repeat;background-size:77px" class="thumb_public"></span></a>';
                        }else{
                            var thumb = '<a href="/ts/social-tech.do?stid='+lastobject.id+'" ><span class="thumb_public"></span></a>';
                        }
                        var author = ' <div class="autor_public">por <span>'+lastobject.submitter_login+'</span></div>';
                        var tagList = lastobject.tags.split(',');
                        
                        var tagString = "";
                        
                        for(var j = 0; j<tagList.length;j++){
                            tagList[j] = tagList[j].replace(" ", "");
                            tagString += "#"+tagList[j]+" ";
                        }
                        var tags = '<div class="object_tags">'+tagString+'</div>';
                        html.append(thumb).append(time).append(title).append(author).append(tags);
                        $('.publication_objects').append(html);
                        
                        $('.tags option').each(function(){
                            var tagArray = new Array();
                            var i=0;
                            if($(this).val() != ""){
                                tagArray[i] = $(this).val().replace(" ", "");
                                var tagListLength = tagList.length;
                                for(var j = 0; j<tagListLength;j++){
                                    if(tagArray[i] == tagList[j].replace(" ", "")){
                                        tagList[j] = ""
                                    }
                                }
                                i++;
                            }
                        })
                        var new_tag = "";
                        for(var j = 0; j<tagList.length;j++){
                            if(tagList[j] != ""){
                                new_tag += '<option value="'+tagList[j]+'">'+tagList[j]+'</option>';
                            }
                        }
                        $('.tags').append(new_tag);
                    }
                }
            }
        }
    });
    
    $(document).on('keyup', '.general_search_input', function(){
        $.ajax({
            type:"GET",
            url:"/webresources/primata/searchreviewobjects",
            data:{
                "theme_id":theme_id,
                "generalSearchInput":$('.general_search_input').val()
            },
            success:function(lastobjects){
                lastobjects = eval(lastobjects);
                if(!(lastobjects instanceof Array)){
                    lastobjects = new Array(lastobjects);
                }else{
                    for(var i=0;i<lastobjects.length;i++){
                        if(lastobjects[i] != null){
                            var lastobject = lastobjects[i];
                            var html = $('<div class="public_tec"></div>');
                            var object_title;
                            if(lastobject.title.length > 16){
                                object_title = lastobject.title.substring(0,13) + "...";
                            }else{
                                object_title = lastobject.title;
                            }
                            var title = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><h2>'+object_title+'</h2></a>';
                            var time = '<font class="clock_public"></font><h3>'+lastobject.to_char+'</h3>';
                            if(typeof lastobject.picture_url != 'undefined'){
                                var thumb = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><span style="background:url('+lastobject.picture_url+') no-repeat;background-size:77px" class="thumb_public"></span></a>';
                            }else{
                                var thumb = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><span class="thumb_public"></span></a>';
                            }
                            var author = ' <div class="autor_public">por <span>'+lastobject.submitter_login+'</span></div>';
                            var tags = '<div class="object_tags">#'+lastobject.tags+'</div>';
                            html.append(thumb).append(time).append(title).append(author).append(tags);
                            $('.publication_objects').append(html);
                        }
                    }
                }
            }
        });
    });
    
    $(document).on('change', '.tags', function(){
        $.ajax({
            type:"GET",
            url:"/webresources/primata/searchreviewobjectsbytag",
            data:{
                "theme_id":theme_id,
                "tag":$('.tags').val()
            },
            success:function(lastobjects){
                lastobjects = eval(lastobjects);
                if(!(lastobjects instanceof Array)){
                    lastobjects = new Array(lastobjects);
                }else{
                    for(var i=0;i<lastobjects.length;i++){
                        if(lastobjects[i] != null){
                            var lastobject = lastobjects[i];
                            var html = $('<div class="public_tec"></div>');
                            var object_title;
                            if(lastobject.title.length > 16){
                                object_title = lastobject.title.substring(0,13) + "...";
                            }else{
                                object_title = lastobject.title;
                            }
                            var title = '<a href="/ts/social-tech.do?stid='+lastobject.id+'"><h2>'+object_title+'</h2></a>';
                            var time = '<font class="clock_public"></font><h3>'+lastobject.to_char+'</h3>';
                            if(typeof lastobject.picture_url != 'undefined'){
                                var thumb = '<span style="background:url('+lastobject.picture_url+') no-repeat;background-size:77px" class="thumb_public"></span>';
                            }else{
                                var thumb = '<span class="thumb_public"></span>';
                            }
                            var author = ' <div class="autor_public">por <a href="">'+lastobject.submitter_login+'</a></div>';
                            var tags = '<div class="object_tags">#'+lastobject.tags+'</div>';
                            html.append(thumb).append(time).append(title).append(author).append(tags);
                            $('.publication_objects').append(html);
                        }
                    }
                }
            }
        });
    });
    
});

