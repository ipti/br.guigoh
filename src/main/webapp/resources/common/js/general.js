function setCursors(ajax) {
    if (ajax === "begin") {
        $('body').css('cursor', 'wait');
        $('a').css('cursor', 'wait');
    } else {
        $('body').css('cursor', 'auto');
        $('a').css('cursor', 'pointer');
    }
}

jsf.ajax.addOnEvent(function (data) {
    switch (data.status) {
        case "begin":
            setCursors(data.status);
            break;
        case "success":
            setCursors(data.status);
            break;
    }
});

$(document).on('click', function (e) {
    if ($(e.target).closest($(".down-arrow")).length > 0){
        $("#user-menu-box").show();
    } else if ($(e.target).closest("#user-menu-box").length === 0) {
        $("#user-menu-box").hide();
    }
    console.log(e.target);
});