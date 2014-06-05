var locale = $("#localeAcronym").val();
$(document).ready(function(){
    
    var inputLabelValue = "";
    if(locale == "ptBR"){
        inputLabelValue = "Pesquise tópicos ou objetos educacionais..."
        }
    else if(locale == "enUS"){
        inputLabelValue = "Search for topics or educational objects..."
        }
    else if(locale == "frFR"){
        inputLabelValue = "Rechercher des sujets ou des objets d'apprentissage..."
        }
    $('.general_search_input').val(inputLabelValue);
    $(document).on('focus', '.general_search_input', function(){
        if($('.general_search_input').val() == inputLabelValue){
            $('.general_search_input').css("color","black")
            $('.general_search_input').val("");
        }
    });
    $(document).on('focusout', '.general_search_input', function(){
        if($('.general_search_input').val() == ""){
            $('.general_search_input').css("color","#ccc")
            $('.general_search_input').val(inputLabelValue);
        }
    });
    
    if($(".discussion_topics_box").height() >= $(".publication_objects_box").height()){
        $(".discussion_topics_box").css("border-right","1px solid #B2B6B1");
    }else{
        $(".publication_objects_box").css("border-left","1px solid #B2B6B1");
    }
});


