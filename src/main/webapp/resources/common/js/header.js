$(document).ready(function(){
    var locale = $("#localeAcronym").val();
    $("." + locale).css("font-weight", "bold");
});