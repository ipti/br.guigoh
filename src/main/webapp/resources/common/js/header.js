$(document).ready(function(){
    var locale = $("#localeAcronym").val();
    $("." + locale).css("font-weight", "bold");
});

$(document).on('click', function (e) {
    if ($(e.target).closest($(".down-arrow")).length > 0){
        $("#user-menu-box").toggle();
    } else if ($(e.target).closest("#user-menu-box").length === 0) {
        $("#user-menu-box").hide();
    }
});