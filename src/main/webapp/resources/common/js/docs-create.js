var wsocket = '';

$(document).ready(function () {
    var collaboratorsIds = "246";
    wsocket = new WebSocket("ws://" + window.location.host + "/socket/docs/" + logged_social_profile_id + "/" + encodeURIComponent(collaboratorsIds));
    wsocket.onmessage = onMessageReceived;
});

function onMessageReceived(evt) {
    var msg = JSON.parse(evt.data); // native API
    if (typeof msg.char !== 'undefined') {

    }
}


function getCaretPosition(editableDiv) {
    var caretPos = 0,
            sel, range;
    if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount) {
            range = sel.getRangeAt(0);
            if (range.commonAncestorContainer.parentNode == editableDiv) {
                caretPos = range.endOffset;
            }
        }
    } else if (document.selection && document.selection.createRange) {
        range = document.selection.createRange();
        if (range.parentElement() == editableDiv) {
            var tempEl = document.createElement("span");
            editableDiv.insertBefore(tempEl, editableDiv.firstChild);
            var tempRange = range.duplicate();
            tempRange.moveToElementText(tempEl);
            tempRange.setEndPoint("EndToEnd", range);
            caretPos = tempRange.text.length;
        }
    }
    return caretPos;
}

function setCaretPosition(position, editableDiv) {
    var node = editableDiv;
    node.focus();
    var textNode = node.lastChild;
    var caret = position;
    var range = document.createRange();
    range.setStart(textNode, caret);
    range.setEnd(textNode, caret);
    var sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(range);
}

function addMarker(socialProfileId){
    return "<span socialprofileid='" + socialProfileId + "' class='marker'></span>";
}

$(document).on("mousedown mouseup keydown keyup", ".editor div", function () {
    $(".marker[socialprofileid=" + logged_social_profile_id + "]").remove();
    var position = getCaretPosition(this);
    var str = $(this).html();
    $(this).html(str.substring(0, position) + addMarker(logged_social_profile_id) + str.substring(position, str.length));
    setCaretPosition(getCaretPosition(this), this);
});

$(".editor").on("mousedown mouseup keydown keyup", function() {
    $(".marker[socialprofileid=" + logged_social_profile_id + "]").remove();
    var position = getCaretPosition(this);
});







function getContent() {
    $("#text").val($(".editor").html());
    return true;
}

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        if ($(data.source).hasClass("save-text")) {
//            var bookmark = tinyMCE.activeEditor.selection.getBookmark(2, true);
//            tinyMCE.activeEditor.setContent($("#text").val());
//            tinyMCE.activeEditor.selection.moveToBookmark(bookmark);
        }
    }
});