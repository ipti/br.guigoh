$(document).ready(function () {

    $('#profile-tab-about').click(function () {
        $('#profile-container-about').show();
        $('#profile-container-objects').hide();
        $('#profile-container-resume').hide();
        $('#profile-tab-about').addClass('active');
        $('#profile-tab-objects').removeClass('active');
        $('#profile-tab-resume').removeClass('active');
    });
    $('#profile-tab-objects').click(function () {
        $('#profile-container-about').hide();
        $('#profile-container-objects').show();
        $('#profile-container-resume').hide();
        $('#profile-tab-about').removeClass('active');
        $('#profile-tab-objects').addClass('active');
        $('#profile-tab-resume').removeClass('active');
    });
    $('#profile-tab-resume').click(function () {
        $('#profile-container-about').hide();
        $('#profile-container-objects').hide();
        $('#profile-container-resume').show();
        $('#profile-tab-about').removeClass('active');
        $('#profile-tab-objects').removeClass('active');
        $('#profile-tab-resume').addClass('active');
    });
    
    $("#profile-icon-mail span").text(changeNameLength($("#profile-icon-mail span").text(), 20));
});