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

function changeNameLength(name, limit) {
    return (name.length > limit) ? name.substring(0, limit - 3) + "..." : name;
}