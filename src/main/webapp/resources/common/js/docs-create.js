var websocketDocs = '';
var savedSelection;
var keys = {
    enter: "13",
    left: "37",
    up: "38",
    right: "39",
    down: "40",
    backspace: "8",
    delete: "46",
    space: "32",
    nonBreakingSpace: "160",
    shiftHome: "{16}36",
    shiftEnd: "{16}35",
    home: "36",
    end: "35",
}
var previousInitialTextLength;

$(document).ready(function () {
    var collaboratorsIds = logged_social_profile_id == 26 ? "246" : "26";
    websocketDocs = new WebSocket("ws://" + window.location.host + "/socket/docs/" + logged_social_profile_id + "/" + encodeURIComponent(collaboratorsIds));
    websocketDocs.onmessage = onMessageReceivedForDocs;
});

function onMessageReceivedForDocs(evt) {
    var msg = JSON.parse(evt.data); // native API
    var initialElement = $(".editor").children().get(msg.initialIndex);
    var finalElement = $(".editor").children().get(msg.finalIndex);
    msg.initialRange = Number(msg.initialRange);
    msg.finalRange = Number(msg.finalRange);
    var savedSelection = saveSelection($(".editor")[0]);
    if ($(initialElement).text() !== undefined && $(finalElement).text() !== undefined) {
        if (msg.type === "NEW_CODE") {
            switch (msg.keyCode) {
                case keys.enter:
                    enterAction(msg.senderId, msg.senderName, initialElement, finalElement, msg.initialRange, msg.finalRange);
                    break;
                case keys.left:
                case keys.right:
                case keys.up:
                case keys.down:
                    arrowAction(msg.senderId, msg.senderName, initialElement, msg.initialRange, msg.finalRange);
                    break;
                case keys.backspace:
                case keys.delete:
                    backspaceAndDeleteAction(msg.keyCode, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialRange, msg.finalRange);
                    break;
                case keys.home:
                case keys.end:
                    homeAndEndAction(msg.keyCode, msg.senderId, msg.senderName, initialElement);
                    break;
                case keys.shiftHome:
                case keys.shiftEnd:
                    shiftHomeAndEndAction(msg.keyCode, msg.senderId, msg.senderName, initialElement, msg.initialRange);
                    break;
                default:
                    // MOUSE CLICK AND LETTER KEYPRESS 
                    // When keyCode is "undefined", jquery trigger event was "mouseup"
                    mouseClickAndLetterKeyPressAction(msg.keyCode, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialRange, msg.finalRange);
                    break;
            }
            $(".editor div").each(function () {
                if ($(this).html() == "") {
                    $(this).append("<br>");
                }
            });
        }
    }
    restoreSelection($(".editor")[0], savedSelection);
}

$(document).on("keydown keyup", ".editor", function (e) {
    if (e.keyCode == keys.backspace && $(".editor").children().length == 1 && $(".editor").children().text() == "") {
        e.preventDefault();
    } else if (((e.keyCode == keys.left || e.keyCode == keys.right || e.keyCode == keys.up || e.keyCode == keys.down) && e.type == "keyup")
            || (e.keyCode == keys.backspace && e.type == "keydown")
            || (e.keyCode == keys.delete && e.type == "keydown")
            || (e.keyCode == keys.home && e.type == "keydown")
            || (e.keyCode == keys.end && e.type == "keydown")) {
        if (e.shiftKey && (e.keyCode == keys.home || e.keyCode == keys.end)){
            e.keyCode = "{16}" + e.keyCode;
        }
        sendCollaboratorChange(e);
    }
    if (e.type == "keydown") {
        $(".marker").attr("contenteditable", "true");
    } else {
        $(".marker").attr("contenteditable", "false");
    }
});

$(document).on("keypress", ".editor", function (e) {
    sendCollaboratorChange(e);
});

$(document).on("mouseup", function (e) {
    sendCollaboratorChange(e);
});

function sendCollaboratorChange(e) {
    var initialNode = document.getSelection().anchorNode;
    var finalNode = document.getSelection().focusNode;
    if (initialNode !== null && finalNode !== null) {
        var initialElement = initialNode.nodeType === 3 ? initialNode.parentNode : initialNode;
        var finalElement = finalNode.nodeType === 3 ? finalNode.parentNode : finalNode;
        var initialRange = getCharOffsetRelativeTo(initialElement, initialNode, document.getSelection().anchorOffset);
        var finalRange = getCharOffsetRelativeTo(finalElement, finalNode, document.getSelection().focusOffset);
        if ($(initialNode).closest(".editor").length && $(finalNode).closest(".editor").length) {
            var json = '{"keyCode":"' + e.keyCode + '", "senderId":"' + logged_social_profile_id + '",'
                    + '"senderName":"' + logged_social_profile_name + '", "initialRange":"' + initialRange + '",'
                    + '"finalRange":"' + finalRange + '", "initialIndex":"' + $(initialElement).index() + '",'
                    + '"finalIndex":"' + $(finalElement).index() + '", "type":"NEW_CODE"}';
            websocketDocs.send(json);
        }
    }
}

function addMarker(id, name, text) {
    return "<span contenteditable='false' socialprofileid='" + id + "' class='marker' title='" + name + "'>" + text + "</span>";
}

function getContent() {
    $("#text").val($(".editor").html());
    return true;
}

function getCharOffsetRelativeTo(container, node, offset) {
    var range = document.createRange();
    range.selectNodeContents(container);
    range.setEnd(node, offset);
    return range.toString().length;
}

function moveCaret(charCount) {
    var sel, range;
    if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount > 0) {
            var textNode = sel.focusNode;
            var newOffset = charCount;
            sel.collapse(textNode, Math.min(textNode.length, newOffset));
        }
    }
}

//AJEITAR CURSOR
if (window.getSelection && document.createRange) {
    saveSelection = function (containerEl) {
        var doc = containerEl.ownerDocument, win = doc.defaultView;
        var sel = window.getSelection && window.getSelection();
        if (sel && sel.rangeCount > 0) {
            var range = win.getSelection().getRangeAt(0);
            var preSelectionRange = range.cloneRange();
            preSelectionRange.selectNodeContents(containerEl);
            preSelectionRange.setEnd(range.startContainer, range.startOffset);
            var start = preSelectionRange.toString().length;

            return {
                start: start,
                end: start + range.toString().length
            }
        } else {
            return {
                start: 0,
                end: 0
            }
        }
    };

    restoreSelection = function (containerEl, savedSel) {
        var doc = containerEl.ownerDocument, win = doc.defaultView;
        var charIndex = 0, range = doc.createRange();
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

        var sel = win.getSelection();
        sel.removeAllRanges();
        sel.addRange(range);
    }
} else if (document.selection) {
    saveSelection = function (containerEl) {
        var doc = containerEl.ownerDocument, win = doc.defaultView || doc.parentWindow;
        var selectedTextRange = doc.selection.createRange();
        var preSelectionTextRange = doc.body.createTextRange();
        preSelectionTextRange.moveToElementText(containerEl);
        preSelectionTextRange.setEndPoint("EndToStart", selectedTextRange);
        var start = preSelectionTextRange.text.length;

        return {
            start: start,
            end: start + selectedTextRange.text.length
        }
    };

    restoreSelection = function (containerEl, savedSel) {
        var doc = containerEl.ownerDocument, win = doc.defaultView || doc.parentWindow;
        var textRange = doc.body.createTextRange();
        textRange.moveToElementText(containerEl);
        textRange.collapse(true);
        textRange.moveEnd("character", savedSel.end);
        textRange.moveStart("character", savedSel.start);
        textRange.select();
    };
}

function mouseClickAndLetterKeyPressAction(keyCode, senderId, senderName, initialElement, finalElement, initialRange, finalRange) {
    var initialStr = $(initialElement).text();
    var finalStr = $(finalElement).text();
    if (keyCode === "undefined") {
        $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    }
    if (keyCode == keys.space) {
        if ((($(initialElement).index() < $(finalElement).index() || $(initialElement).index() == $(finalElement).index()) && initialStr.charCodeAt(initialRange - 1) == Number(keys.space))
                || ($(initialElement).index() > $(finalElement).index() && finalStr.charCodeAt(finalRange - 1) == Number(keys.space))) {
            keyCode = keys.nonBreakingSpace;
        }
    }
    $(".marker[socialprofileid=" + senderId + "]").remove();
    if ($(initialElement).index() < $(finalElement).index()) {
        if (keyCode !== "undefined") {
            var text = $(finalElement).html();
            $(initialElement).append(String.fromCharCode(keyCode) + addMarker(senderId, senderName, "") + text);
            $(initialElement).nextUntil($(finalElement)).each(function () {
                $(this).remove();
            });
            $(finalElement).remove();
        } else {
            $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, initialStr.substring(initialRange)));
            $(initialElement).nextUntil($(finalElement)).each(function () {
                $(this).html(addMarker(senderId, senderName, $(this).html()));
            });
            $(finalElement).html(addMarker(senderId, senderName, finalStr.substring(0, finalRange)) + finalStr.substring(finalRange));
        }
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialRange < finalRange) {
            if (keyCode !== "undefined") {
                $(initialElement).html(initialStr.substring(0, initialRange) + String.fromCharCode(keyCode) + addMarker(senderId, senderName, "") + initialStr.substring(finalRange));
            } else {
                $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, initialStr.substring(initialRange, finalRange)) + initialStr.substring(finalRange));
            }
        } else if (initialRange == finalRange) {
            $(initialElement).html(initialStr.substring(0, initialRange) + String.fromCharCode(keyCode) + addMarker(senderId, senderName, "") + initialStr.substring(finalRange));
        } else {
            if (keyCode !== "undefined") {
                $(initialElement).html(initialStr.substring(0, finalRange) + String.fromCharCode(keyCode) + addMarker(senderId, senderName, "") + initialStr.substring(initialRange));
            } else {
                $(initialElement).html(initialStr.substring(0, finalRange) + addMarker(senderId, senderName, initialStr.substring(initialRange, finalRange)) + initialStr.substring(initialRange));
            }
        }
    } else {
        if (keyCode !== "undefined") {
            var text = $(initialElement).html();
            $(finalElement).append(String.fromCharCode(keyCode) + addMarker(senderId, senderName, "") + text);
            $(finalElement).nextUntil($(initialElement)).each(function () {
                $(this).remove();
            });
            $(initialElement).remove();
        } else {
            $(initialElement).html(addMarker(senderId, senderName, initialStr.substring(0, initialRange)) + initialStr.substring(initialRange));
            $(finalElement).nextUntil($(initialElement)).each(function () {
                $(this).html(addMarker(senderId, senderName, $(this).html()));
            });
            $(finalElement).html(finalStr.substring(0, finalRange) + addMarker(senderId, senderName, finalStr.substring(finalRange)));
        }
    }
}

function backspaceAndDeleteAction(keyCode, senderId, senderName, initialElement, finalElement, initialRange, finalRange) {
    var initialStr = $(initialElement).text();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    if ($(initialElement).index() < $(finalElement).index()) {
        var text = $(finalElement).html();
        $(initialElement).append(addMarker(senderId, senderName, "") + text);
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialRange < finalRange) {
            $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, "") + initialStr.substring(finalRange));
        } else if (initialRange == finalRange) {
            if (keyCode == keys.backspace && initialRange == 0 && $(initialElement).index() !== 0) {
                var text = $(initialElement).html();
                if ($(initialElement).prev().text() == "") {
                    $(initialElement).prev().html(addMarker(senderId, senderName, "") + text);
                } else {
                    $(initialElement).prev().append(addMarker(senderId, senderName, "") + text);
                }
                $(initialElement).remove();
            } else if (keyCode == keys.delete && initialRange == initialStr.length && !$(initialElement).is(':last-child')) {
                var text = $(initialElement).next().html();
                $(initialElement).next().remove();
                $(initialElement).append(addMarker(senderId, senderName, "") + text);
            } else {
                if (keyCode == keys.backspace) {
                    $(initialElement).html(initialStr.substring(0, initialRange - 1) + addMarker(senderId, senderName, "") + initialStr.substring(finalRange));
                } else {
                    $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, "") + initialStr.substring(finalRange + 1));
                }
            }
        } else {
            $(initialElement).html(initialStr.substring(0, finalRange) + addMarker(senderId, senderName, "") + initialStr.substring(initialRange));
        }
    } else {
        var text = $(initialElement).html();
        $(finalElement).append(addMarker(senderId, senderName, "") + text);
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
    }
}

function arrowAction(senderId, senderName, initialElement, initialRange, finalRange) {
    var initialStr = $(initialElement).text();
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, "") + initialStr.substring(finalRange));
}

function enterAction(senderId, senderName, initialElement, finalElement, initialRange, finalRange) {
    $(".marker[socialprofileid=" + senderId + "]").remove();
    if ($(initialElement).index() < $(finalElement).index()) {
        var text = $(finalElement).text();
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        $("<div>" + addMarker(senderId, senderName, "") + text + "</div>").insertAfter(initialElement);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        var text = $(initialElement).text();
        $(initialElement).html(text.substring(0, initialRange < finalRange ? initialRange : finalRange));
        $("<div>" + addMarker(senderId, senderName, "") + text.substring(initialRange < finalRange ? initialRange : finalRange) + "</div>").insertAfter(initialElement);
    } else {
        var text = $(initialElement).text();
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
        $("<div>" + addMarker(senderId, senderName, "") + text + "</div>").insertAfter(finalElement);
    }
}

function homeAndEndAction(keyCode, senderId, senderName, initialElement) {
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    if (keyCode == keys.home) {
        $(initialElement).html(addMarker(senderId, senderName, "") + $(initialElement).html());
    } else {
        $(initialElement).append(addMarker(senderId, senderName, ""));
    }
}

function shiftHomeAndEndAction(keyCode, senderId, senderName, initialElement, initialRange) {
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    var initialStr = $(initialElement).html();
    if (keyCode == keys.shiftHome) {
        $(initialElement).html(addMarker(senderId, senderName, initialStr.substring(0, initialRange)) + initialStr.substring(initialRange));
    } else {
        $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, initialStr.substring(initialRange)));
    }
}