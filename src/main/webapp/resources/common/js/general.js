function setCursors(data) {
    if (!eventExceptions(data)) {
        if (data.status === "begin") {
            $('*').css('cursor', 'wait');
        } else {
            $('*').css('cursor', '');
        }
    }
}

jsf.ajax.addOnEvent(function (data) {
    switch (data.status) {
        case "begin":
            setCursors(data);
            break;
        case "success":
            setCursors(data);
            break;
    }
});

function changeNameLength(name, limit) {
    return (name.length > limit) ? name.substring(0, limit - 3) + "..." : name;
}

function eventExceptions(data) {
    if ($(data.source).parent().hasClass("general-search")
            || $(data.source).hasClass("friend-search")) {
        return true;
    } else {
        return false;
    }
}

function getTrackerCoords(c, image)
{
    // fix crop size: find ratio dividing current per real size
    var ratioW = $("." + image)[0].naturalWidth / $("." + image).width();
    var ratioH = $("." + image)[0].naturalHeight / $("." + image).height();
    var currentRatio = Math.min(ratioW, ratioH);
    $('#' + image + '-tracker-x').val(Math.round(c.x * currentRatio));
    $('#' + image + '-tracker-y').val(Math.round(c.y * currentRatio));
    $('#' + image + '-tracker-w').val(Math.round(c.w * currentRatio));
    $('#' + image + '-tracker-h').val(Math.round(c.h * currentRatio));
}