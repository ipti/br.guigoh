/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function(){
    $.ajax({
        type:"GET",
        url:"/webresources/primata/submittedreviewobjects",
        success:function(lastobjects){
            if (lastobjects.length > 0){
                $('.authorization_objects_alert').html("");
            
                var submitted_objects = "";
                for (var i=0;i<lastobjects.length;i++){
                    submitted_objects += "<div id='submitted_object_box_"+lastobjects[i].id+"' class='submitted_object_box'><span class='submitted_object'>"+lastobjects[i].title+"</span><input type='checkbox' value='"+lastobjects[i].id+"' class='submitted_object_checkbox'></input></div>";
                }
                var submit = "<input class='publish_objects' type='button' value='Publicar objetos'></input>";
                $('.authorization_objects_box').append(submitted_objects).append(submit);
            }
        }
    })
    $(document).on('click', '.publish_objects', function(){
        $.each($('.submitted_object_checkbox:checked'), function(i){
            var id = $(this).attr('value');
            $.ajax({
                type:"GET",
                url:"/webresources/primata/publishreviewobjects",
                data:{
                    "publishObjectId":id
                },
                success:function(success){
                    $("#submitted_object_box_"+id).remove();
                    if($('.submitted_object_checkbox').size() == 0){
                        $('.authorization_objects_alert').html("Não existem Objetos Educacionais pendentes.");
                        $('.publish_objects').hide();
                    }
                }
            })
        });
    });
})

jQuery(function($){
    $('#checkall').bind('click',function(e){
        $(".kselItems").attr("checked", true);
        e.preventDefault();
    });
    $('#uncheckall').bind('click',function(e){
        $(".kselItems").attr("checked", false);
        e.preventDefault();
    });  
});

