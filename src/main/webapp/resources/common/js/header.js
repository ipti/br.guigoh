$(document).ready(function(){
    var locale = $("#localeAcronym").val();
    $("." + locale).css("font-weight", "bold");
    changeNavMenu(index);
});

$(".down-arrow").click(function(){
    if ($("#user-menu-box").attr("data-collapse") == "true") {
        $("#user-menu-box").attr("data-collapse", "false");
        $("#user-menu-box").show();
    } else {
        $("#user-menu-box").attr("data-collapse", "true");
        $("#user-menu-box").hide();
    }
});

function changeNavMenu(index){
    $(".menu-icons.active").removeClass("active");
    $(".menu-icons").eq(index).addClass("active");
};