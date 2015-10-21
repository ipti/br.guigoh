var websocketDocs = '';
var savedSelection;
var keys = {
    enter: "13",
    left: "37",
    up: "38",
    right: "39",
    down: "40",
    backspace: "8",
}

$(document).ready(function () {
    var collaboratorsIds = "246";
    websocketDocs = new WebSocket("ws://" + window.location.host + "/socket/docs/" + logged_social_profile_id + "/" + encodeURIComponent(collaboratorsIds));
    websocketDocs.onmessage = onMessageReceivedForDocs;
});

function onMessageReceivedForDocs(evt) {
    var msg = JSON.parse(evt.data); // native API
    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
    var element = $(".editor").children().get(msg.index);
    var str = $(element).html();
    if (msg.type === "NEW_CODE") {
        switch (msg.keyCode) {
            case keys.enter:
                break;
            default:
                //        savedSelection = saveSelection($(".editor").children().index(msg.index));
                //        if (savedSelection) {
                $(element).html(str.substring(0, msg.offset) + String.fromCharCode(msg.keyCode) + "<span socialprofileid='" + msg.senderId + "' class='marker' title='" + msg.senderName + "'></span>" + str.substring(msg.offset, str.length));
                //            restoreSelection(element, savedSelection);
                //        }
                break;
        }
    } else if (msg.type === "FAKE_CODE") {
        switch (msg.keyCode) {
            case keys.left:
            case keys.right:
                $(element).html(str.substring(0, msg.offset) + "<span socialprofileid='" + msg.senderId + "' class='marker' title='" + msg.senderName + "'></span>" + str.substring(msg.offset, str.length));
                break;
            case keys.up:
                break;
            case keys.down:
                break;
        }
    }
}

$(document).on("keydown keyup", ".editor", function (e) {
    if (e.keyCode == "37" || e.keyCode == "38" || e.keyCode == "39" || e.keyCode == "40") {
        var node = document.getSelection().anchorNode;
        if (node !== null) {
            var element = node.nodeType === 3 ? node.parentNode : node;
            sendMarker(element, e.keyCode, "FAKE_CODE");
        }
    }
});

$(document).on("mouseup keypress", ".editor", function (e) {
    var node = document.getSelection().anchorNode;
    if (node !== null) {
        var element = node.nodeType === 3 ? node.parentNode : node;
        sendMarker(element, e.keyCode, "NEW_CODE");
    }
});

function sendMarker(element, keyCode, type) {
    var offset = getCaretCharacterOffsetWithin(element);
    var index = $(element).index();
    var json = '{"keyCode":"' + keyCode + '", "senderId":"' + logged_social_profile_id + '",'
            + '"senderName":"' + logged_social_profile_name + '", "offset":"' + offset + '", "index":"' + index + '", "type":"' + type + '"}';
    websocketDocs.send(json);
}

function getContent() {
    $("#text").val($(".editor").html());
    return true;
}

function getCaretCharacterOffsetWithin(element) {
    var caretOffset = 0;
    if (typeof window.getSelection != "undefined") {
        var range = window.getSelection().getRangeAt(0);
        var preCaretRange = range.cloneRange();
        preCaretRange.selectNodeContents(element);
        preCaretRange.setEnd(range.endContainer, range.endOffset);
        caretOffset = preCaretRange.toString().length;
    } else if (typeof document.selection != "undefined" && document.selection.type != "Control") {
        var textRange = document.selection.createRange();
        var preCaretTextRange = document.body.createTextRange();
        preCaretTextRange.moveToElementText(element);
        preCaretTextRange.setEndPoint("EndToEnd", textRange);
        caretOffset = preCaretTextRange.text.length;
    }
    return caretOffset;
}

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