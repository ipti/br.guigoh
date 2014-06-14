var locale = $("#localeAcronym").val();
$(document).ready(function(){
    if($(".discussion_topics_box").height() >= $(".publication_objects_box").height()){
        $(".discussion_topics_box").css("border-right","1px solid #B2B6B1");
    }else{
        $(".publication_objects_box").css("border-left","1px solid #B2B6B1");
    }
});


