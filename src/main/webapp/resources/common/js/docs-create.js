var wsocket = '';
var savedSelection;

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

$(document).on("mousedown mouseup keyup keydown", ".editor", function (e) {
    var node = document.getSelection().anchorNode;
    var element = node.nodeType === 3 ? node.parentNode : node;
    insertMarker(element);
});

function insertMarker(element) {
    $(".marker[socialprofileid=" + logged_social_profile_id + "]").remove();
    savedSelection = saveSelection(element);
    if (savedSelection) {
        var str = $(element).html();
        $(element).html(str.substring(0, savedSelection.start) + addMarker(logged_social_profile_id) + str.substring(savedSelection.start, str.length));
        restoreSelection(element, savedSelection);
    }
}

function addMarker(socialProfileId) {
    return "<span socialprofileid='" + socialProfileId + "' class='marker'></span>";
}

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

if (window.getSelection && document.createRange) {
    saveSelection = function (containerEl) {
        var sel = window.getSelection && window.getSelection();
        if (sel && sel.rangeCount > 0) {
            var range = window.getSelection().getRangeAt(0);
            var preSelectionRange = range.cloneRange();
            preSelectionRange.selectNodeContents(containerEl);
            preSelectionRange.setEnd(range.startContainer, range.startOffset);
            var start = preSelectionRange.toString().length;
            return {
                start: start,
                end: start + range.toString().length
            };
        }
    };

    restoreSelection = function (containerEl, savedSel) {
        var charIndex = 0, range = document.createRange();
        range.setStart(containerEl, 0);
        range.collapse(true);
        var nodeStack = [containerEl], node, foundStart = false, stop = false;

        while (!stop && (node = nodeStack.pop())) {
            if (node.nodeType == 3) {
                var nextCharIndex = charIndex + node.length;
                if (!foundStart && savedSel.start >= charIndex && savedSel.start <= nextCharIndex) {
                    range.setStart(node, savedSel.start - charIndex);
                    foundStart = true;
                }
                if (foundStart && savedSel.end >= charIndex && savedSel.end <= nextCharIndex) {
                    range.setEnd(node, savedSel.end - charIndex);
                    stop = true;
                }
                charIndex = nextCharIndex;
            } else {
                var i = node.childNodes.length;
                while (i--) {
                    nodeStack.push(node.childNodes[i]);
                }
            }
        }

        var sel = window.getSelection();
        sel.removeAllRanges();
        sel.addRange(range);
    }
} else if (document.selection) {
    saveSelection = function (containerEl) {
        var selectedTextRange = document.selection.createRange();
        var preSelectionTextRange = document.body.createTextRange();
        preSelectionTextRange.moveToElementText(containerEl);
        preSelectionTextRange.setEndPoint("EndToStart", selectedTextRange);
        var start = preSelectionTextRange.text.length;

        return {
            start: start,
            end: start + selectedTextRange.text.length
        }
    };

    restoreSelection = function (containerEl, savedSel) {
        var textRange = document.body.createTextRange();
        textRange.moveToElementText(containerEl);
        textRange.collapse(true);
        textRange.moveEnd("character", savedSel.end);
        textRange.moveStart("character", savedSel.start);
        textRange.select();
    };
}