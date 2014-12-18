$(document).ready(function (){

    $(document).on("click", "#expand_pending_users", function(){
        $("#pending_users_box").css("display", "block");
    });
    
    $(document).on("click", "#collapse_pending_users", function(){
        $("#pending_users_box").css("display", "none");
    });
    
    $(document).on("click", "#expand_active_users", function(){
        $("#active_users_box").css("display", "block");
    });
    
    $(document).on("click", "#collapse_active_users", function(){
        $("#active_users_box").css("display", "none");
    });
    
    $(document).on("click", "#expand_inactive_users", function(){
        $("#inactive_users_box").css("display", "block");
    });
    
    $(document).on("click", "#collapse_inactive_users", function(){
        $("#inactive_users_box").css("display", "none");
    });
    
    $(document).on("click", "#expand_pending_objects", function(){
        $("#pending_objects_box").css("display", "block");
    });
    
    $(document).on("click", "#collapse_pending_objects", function(){
        $("#pending_objects_box").css("display", "none");
    });
    $(document).on("click", "#expand_active_objects", function(){
        $("#active_objects_box").css("display", "block");
    });
    
    $(document).on("click", "#collapse_active_objects", function(){
        $("#active_objects_box").css("display", "none");
    });
    
    $(document).on("click", "#expand_inactive_objects", function(){
        $("#inactive_objects_box").css("display", "block");
    });
    
    $(document).on("click", "#collapse_inactive_objects", function(){
        $("#inactive_objects_box").css("display", "none");
    });
    
    $("#collapse_pending_users").click();
    $("#collapse_active_users").click();
    $("#collapse_inactive_users").click();
    $("#collapse_pending_objects").click();
    $("#collapse_active_objects").click();
    $("#collapse_inactive_objects").click();
});