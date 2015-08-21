$(document).ready(function(){
    var locale = $("#localeAcronym").val();
    $("." + locale).css("font-weight", "bold");
    changeNavMenu(index);
});

function changeNavMenu(index){
    $(".menu-icons.active").removeClass("active");
    $(".menu-icons").eq(index).addClass("active");
};