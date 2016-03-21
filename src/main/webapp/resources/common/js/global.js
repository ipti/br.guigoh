$('.messenger.disabled').mousemove(function (e) {
        $('#visitorPermissionWarning').css({position: 'absolute', top: e.pageY - 85 + 'px', left: e.pageX - 210 + 'px'}).show();
});

$('.messenger.disabled').mouseleave(function (e) {
        $('#visitorPermissionWarning').hide();
});

$(document).on('click', function (e) {
    if ($(e.target).closest($(".down-arrow")).length > 0) {
        $("#logged-user-menu-box").toggle();
    } else if ($(e.target).closest("#logged-user-menu-box").length === 0) {
        $("#logged-user-menu-box").hide();
    } 
    
    if ($(e.target).closest($(".language-selected")).length > 0) {
        $("#language-menu-box").toggle();
    } else if ($(e.target).closest(".language-selected").length === 0) {
        $("#language-menu-box").hide();
    }
});

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

function changeFileNameLength(name, limit) {
    return (name.length > limit) ? name.substring(0, (limit / 2) - 3) + "..." + name.substring(name.length - limit / 2, name.length) : name;
}

function eventExceptions(data) {
    if ($(data.source).parent().hasClass("general-search")
            || $(data.source).hasClass("friend-search")
            || $(data.source).hasClass("topic-search")
            || $(data.source).hasClass("educational-object-search")
            || $(data.source).hasClass("increase-views")
            || $(data.source).hasClass("admin-search")
            || $(data.source).hasClass("like-object")
            || $(data.source).hasClass("guest-user-search")
            || $(data.source).hasClass("doc-status")) {
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

function changeSelectColors() {
    if ($(this).children('option:first-child').is(':selected')) {
        $(this).css("color", "gray");
    } else {
        $(this).css("color", "black");
    }
    $(this).children("option").not(":eq(0)").css("color", "black");
    $(this).children("option:first-child").css("color", "gray");
}

function preventScrolling(ev) {
    var $this = $(this),
            scrollTop = this.scrollTop,
            scrollHeight = this.scrollHeight,
            height = $this.height(),
            delta = (ev.type == 'DOMMouseScroll' ?
                    ev.originalEvent.detail * -40 :
                    ev.originalEvent.wheelDelta),
            up = delta > 0;

    var prevent = function () {
        ev.stopPropagation();
        ev.preventDefault();
        ev.returnValue = false;
        return false;
    }

    if (!up && -delta > scrollHeight - height - scrollTop) {
        // Scrolling down, but this will take us past the bottom.
        $this.scrollTop(scrollHeight);

        return prevent();
    } else if (up && delta > scrollTop) {
        // Scrolling up, but this will take us past the top.
        $this.scrollTop(0);
        return prevent();
    }
}

(function ($) {
    $.fn.focusTextToEnd = function () {
        this.focus();
        var $thisVal = this.val();
        this.val('').val($thisVal);
        return this;
    }
}(jQuery));