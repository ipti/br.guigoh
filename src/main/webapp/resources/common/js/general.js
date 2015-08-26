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
    if ($(data.source).hasClass("general-search")) {
        if ($(data.source).val().length >= 3) {
            return false;
        } else {
            return true;
        }
    }
}