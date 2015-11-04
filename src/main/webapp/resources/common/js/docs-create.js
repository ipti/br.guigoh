var websocketDocs = '';
var savedSelection;
var keys = {
    enter: "13",
    left: "37",
    up: "38",
    right: "39",
    down: "40",
    ctrlLeft: "17+37",
    ctrlUp: "17+38",
    ctrlRight: "17+39",
    ctrlDown: "17+40",
    shiftLeft: "16+37",
    shiftUp: "16+38",
    shiftRight: "16+39",
    shiftDown: "16+40",
    ctrlShiftLeft: "17+16+37",
    ctrlShiftUp: "17+16+38",
    ctrlShiftRight: "17+16+39",
    ctrlShiftDown: "17+16+40",
    backspace: "8",
    delete: "46",
    space: "32",
    nonBreakingSpace: "160",
    ctrl: "17",
    shift: "16",
    ctrlHome: "17+36",
    ctrlEnd: "17+35",
    shiftHome: "16+36",
    shiftEnd: "16+35",
    ctrlShiftHome: "17+16+36",
    ctrlShiftEnd: "17+16+35",
    home: "36",
    end: "35",
    tab: "9",
    emphasizedSpace: "8195",
    a: "65",
    ctrlA: "17+65",
    insert: "45",
    pageUp: "33",
    pageDown: "34",
    shiftPageUp: "16+33",
    shiftPageDown: "16+34",
    paste: "paste",
}

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
//    var savedSelection = saveSelection($(".editor")[0]);
    if ($(initialElement).text() !== undefined && $(finalElement).text() !== undefined) {
        if (msg.type === "NEW_CODE") {
            switch (msg.code) {
                case keys.enter:
                    enterAction(msg.senderId, msg.senderName, initialElement, finalElement, msg.initialRange, msg.finalRange);
                    break;
                case keys.left:
                case keys.right:
                case keys.up:
                case keys.down:
                case keys.ctrlLeft:
                case keys.ctrlRight:
                case keys.ctrlUp:
                case keys.ctrlDown:
                case keys.shiftLeft:
                case keys.shiftRight:
                case keys.shiftUp:
                case keys.shiftDown:
                case keys.ctrlShiftLeft:
                case keys.ctrlShiftRight:
                case keys.ctrlShiftUp:
                case keys.ctrlShiftDown:
                    arrowAction(msg.senderId, msg.senderName, initialElement, finalElement, msg.initialRange, msg.finalRange);
                    break;
                case keys.backspace:
                case keys.delete:
                    backspaceAndDeleteAction(msg.code, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialRange, msg.finalRange);
                    break;
                case keys.home:
                case keys.end:
                    homeAndEndAction(msg.code, msg.senderId, msg.senderName, initialElement);
                    break;
                case keys.ctrlHome:
                case keys.ctrlEnd:
                case keys.pageUp:
                case keys.pageDown:
                    ctrlHomeAndEndAction(msg.code, msg.senderId, msg.senderName, initialElement, finalElement);
                    break;
                case keys.shiftHome:
                case keys.shiftEnd:
                    shiftHomeAndEndAction(msg.code, msg.senderId, msg.senderName, initialElement, msg.initialRange);
                    break;
                case keys.ctrlShiftHome:
                case keys.ctrlShiftEnd:
                case keys.shiftPageUp:
                case keys.shiftPageDown:
                    ctrlShiftHomeAndEndAction(msg.code, msg.senderId, msg.senderName, initialElement, msg.initialRange);
                    break;
                case keys.ctrlA:
                    ctrlAAction(msg.senderId, msg.senderName);
                    break;
                default:
                    if (msg.code.indexOf("paste") > -1) {
                        // PASTE
                        pasteAction(msg.code.replace("paste+", ""), msg.senderId, msg.senderName, initialElement, finalElement, msg.initialRange, msg.finalRange);
                    } else {
                        // MOUSE CLICK AND LETTER KEYPRESS 
                        // When keyCode is "undefined", jquery trigger event was "mouseup"
                        mouseClickAndLetterKeyPressAction(msg.code, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialRange, msg.finalRange);
                    }
                    break;
            }
            $(".editor div").each(function () {
                if ($(this).html() == "") {
                    $(this).append("<br>");
                }
            });
        }
    }
//    restoreSelection($(".editor")[0], savedSelection);
}

$(document).on("keydown keyup", ".editor", function (e) {
    if ((e.keyCode == keys.backspace && $(".editor").children().length == 1 && $(".editor").children().text() == "") || e.keyCode == keys.insert) {
        e.preventDefault();
    } else if (((e.keyCode == keys.left || e.keyCode == keys.right || e.keyCode == keys.up || e.keyCode == keys.down) && e.type == "keyup")
            || (e.keyCode == keys.backspace && e.type == "keydown")
            || (e.keyCode == keys.delete && e.type == "keydown")
            || (e.keyCode == keys.home && e.type == "keydown")
            || (e.keyCode == keys.end && e.type == "keydown")
            || (e.keyCode == keys.tab && e.type == "keydown")
            || (e.keyCode == keys.a && e.type == "keydown" && e.ctrlKey)
            || (e.keyCode == keys.pageDown && e.type == "keydown" && ((!e.shiftKey && !e.ctrlKey) || (e.shiftKey && !e.ctrlKey)))
            || (e.keyCode == keys.pageUp && e.type == "keydown" && ((!e.shiftKey && !e.ctrlKey) || (e.shiftKey && !e.ctrlKey)))) {
        if (e.keyCode == keys.left || e.keyCode == keys.right || e.keyCode == keys.up || e.keyCode == keys.down
                || e.keyCode == keys.home || e.keyCode == keys.end) {
            if (e.shiftKey) {
                e.keyCode = keys.shift + "+" + e.keyCode;
            }
            if (e.ctrlKey) {
                e.keyCode = keys.ctrl + "+" + e.keyCode;
            }
        }
        if (e.keyCode == keys.a && e.ctrlKey) {
            e.keyCode = keys.ctrl + "+" + e.keyCode;
        }
        if ((e.keyCode == keys.pageDown || e.keyCode == keys.pageUp) && e.shiftKey) {
            e.keyCode = keys.shift + "+" + e.keyCode;
        }
        sendCollaboratorChange(e);
    }
});

$(document).on("keypress", ".editor", function (e) {
    sendCollaboratorChange(e);
});

$(document).on("paste", ".editor", function (e) {
    sendCollaboratorChange(e);

});

$(document).on("cut", ".editor", function (e) {
    sendCollaboratorChange(e);
});

$(document).on("mouseup", function (e) {
    sendCollaboratorChange(e);
});

function sendCollaboratorChange(e) {
    var initialNode = window.getSelection().anchorNode;
    var finalNode = window.getSelection().focusNode;
    if (initialNode !== null && finalNode !== null) {
        if ($(initialNode).closest(".editor").length && $(finalNode).closest(".editor").length) {
            var initialElement = getEditorChild(initialNode);
            var finalElement = getEditorChild(finalNode);
            var initialRange = getCharOffsetRelativeTo(initialElement, initialNode, document.getSelection().anchorOffset);
            var finalRange = getCharOffsetRelativeTo(finalElement, finalNode, document.getSelection().focusOffset);
            var code = checkCodeToSend(e, initialElement, finalElement, initialRange, finalRange);
            var json = '{"code":"' + code + '", "senderId":"' + logged_social_profile_id + '",'
                    + '"senderName":"' + logged_social_profile_name + '", "initialRange":"' + initialRange + '",'
                    + '"finalRange":"' + finalRange + '", "initialIndex":"' + $(initialElement).index() + '",'
                    + '"finalIndex":"' + $(finalElement).index() + '", "type":"NEW_CODE"}';
            websocketDocs.send(json);
        }
        changeLocalActions(e, initialElement, finalElement, initialRange, finalRange);
    }
}

function getEditorChild(node) {
    if (node.nodeType === 3) {
        return getEditorChild(node.parentNode);
    } else {
        if (node.parentNode.className === "editor") {
            return node;
        } else {
            return getEditorChild(node.parentNode);
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

function checkCodeToSend(e, initialElement, finalElement, initialRange, finalRange) {
    if (e.type == "cut") {
        if (initialRange == finalRange && initialElement == finalElement) {
            return undefined;
        } else {
            return keys.delete;
        }
    } else if (e.type == "paste") {
        var htmlArray = $.parseHTML(e.originalEvent.clipboardData.getData("text/html"));
        if (htmlArray !== null) {
            var htmlString = "";
            htmlArray.splice(0, 1);
            for (var i in htmlArray) {
                htmlString += htmlArray[i].outerHTML;
            }
            return keys.paste + "+" + htmlString.replace(/'/g, "").replace(/"/g, "'");
        } else {
            return keys.paste + "+" + e.originalEvent.clipboardData.getData("text").replace(/'/g, "").replace(/"/g, "'");;
        }
    } else {
        return e.keyCode;
    }
}

function changeLocalActions(e, initialElement, finalElement, initialRange, finalRange) {
    if (e.keyCode == keys.tab) {
        e.preventDefault();
        var initialStr = $(initialElement).html();
        var finalStr = $(finalElement).html();
        if ($(initialElement).index() < $(finalElement).index()) {
            $(initialElement).html(initialStr.substring(0, initialRange) + String.fromCharCode(keys.emphasizedSpace) + finalStr.substring(finalRange));
            $(initialElement).nextUntil($(finalElement)).each(function () {
                $(this).remove();
            });
            $(finalElement).remove();
            moveCaret(initialRange + 1);
        } else if ($(initialElement).index() == $(finalElement).index()) {
            if ((initialRange < finalRange) || (initialRange == finalRange)) {
                $(initialElement).html(initialStr.substring(0, initialRange) + String.fromCharCode(keys.emphasizedSpace) + initialStr.substring(finalRange));
                moveCaret(initialRange + 1);
            } else {
                $(initialElement).html(initialStr.substring(0, finalRange) + String.fromCharCode(keys.emphasizedSpace) + initialStr.substring(initialRange));
                moveCaret(finalRange + 1, true);
            }
        } else {
            $(finalElement).html(finalStr.substring(0, finalRange) + String.fromCharCode(keys.emphasizedSpace) + initialStr.substring(initialRange));
            $(finalElement).nextUntil($(initialElement)).each(function () {
                $(this).remove();
            });
            $(initialElement).remove();
            moveCaret(finalRange + 1, true);
        }
    }
}

function getCharOffsetRelativeTo(container, node, offset) {
    var range = document.createRange();
    range.selectNodeContents(container);
    range.setEnd(node, offset);
    return range.toString().length;
}

function moveCaret(charCount, invertedSelection) {
    invertedSelection = invertedSelection == null ? false : invertedSelection;
    var sel;
    if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount > 0) {
            var textNode = invertedSelection ? sel.focusNode : sel.anchorNode;
            var nodeLength = textNode.length;
            var newOffset = charCount;
            if (nodeLength == undefined) {
                nodeLength = 1;
            }
            sel.collapse(textNode, Math.min(nodeLength, newOffset));
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

function mouseClickAndLetterKeyPressAction(code, senderId, senderName, initialElement, finalElement, initialRange, finalRange) {
    if (code === "undefined") {
        $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    }
    if (code == keys.space) {
        if ((($(initialElement).index() < $(finalElement).index() || $(initialElement).index() == $(finalElement).index()) && initialText.charCodeAt(initialRange - 1) == Number(keys.space))
                || ($(initialElement).index() > $(finalElement).index() && finalText.charCodeAt(finalRange - 1) == Number(keys.space))) {
            code = keys.nonBreakingSpace;
        }
    }
    if (code == keys.tab) {
        code = keys.emphasizedSpace;
    }
    $(".marker[socialprofileid=" + senderId + "]").remove();
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    var initialText = $(initialElement).text();
    var finalText = $(finalElement).text();
    if ($(initialElement).index() < $(finalElement).index()) {
        if (code !== "undefined") {
            $(initialElement).append(String.fromCharCode(code) + addMarker(senderId, senderName, "") + finalHtml);
            $(initialElement).nextUntil($(finalElement)).each(function () {
                $(this).remove();
            });
            $(finalElement).remove();
        } else {
            $(initialElement).html(initialText.substring(0, initialRange) + addMarker(senderId, senderName, initialText.substring(initialRange)));
            $(initialElement).nextUntil($(finalElement)).each(function () {
                $(this).html(addMarker(senderId, senderName, $(this).html()));
            });
            $(finalElement).html(addMarker(senderId, senderName, finalText.substring(0, finalRange)) + finalText.substring(finalRange));
        }
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialRange < finalRange) {
            if (code !== "undefined") {
                $(initialElement).html(initialText.substring(0, initialRange) + String.fromCharCode(code) + addMarker(senderId, senderName, "") + initialText.substring(finalRange));
            } else {
                $(initialElement).html(initialText.substring(0, initialRange) + addMarker(senderId, senderName, initialText.substring(initialRange, finalRange)) + initialText.substring(finalRange));
            }
        } else if (initialRange == finalRange) {
            $(initialElement).html(initialHtml.substring(0, initialRange) + String.fromCharCode(code) + addMarker(senderId, senderName, "") + initialHtml.substring(initialRange));
        } else {
            if (code !== "undefined") {
                $(initialElement).html(initialText.substring(0, finalRange) + String.fromCharCode(code) + addMarker(senderId, senderName, "") + initialText.substring(initialRange));
            } else {
                $(initialElement).html(initialText.substring(0, finalRange) + addMarker(senderId, senderName, initialText.substring(initialRange, finalRange)) + initialText.substring(initialRange));
            }
        }
    } else {
        if (code !== "undefined") {
            $(finalElement).append(String.fromCharCode(code) + addMarker(senderId, senderName, "") + initialHtml);
            $(finalElement).nextUntil($(initialElement)).each(function () {
                $(this).remove();
            });
            $(initialElement).remove();
        } else {
            $(initialElement).html(addMarker(senderId, senderName, initialText.substring(0, initialRange)) + initialText.substring(initialRange));
            $(finalElement).nextUntil($(initialElement)).each(function () {
                $(this).html(addMarker(senderId, senderName, $(this).html()));
            });
            $(finalElement).html(finalText.substring(0, finalRange) + addMarker(senderId, senderName, finalText.substring(finalRange)));
        }
    }
}

function backspaceAndDeleteAction(code, senderId, senderName, initialElement, finalElement, initialRange, finalRange) {
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
            if (code == keys.backspace && initialRange == 0 && $(initialElement).index() !== 0) {
                var text = $(initialElement).html();
                if ($(initialElement).prev().text() == "") {
                    $(initialElement).prev().html(addMarker(senderId, senderName, "") + text);
                } else {
                    $(initialElement).prev().append(addMarker(senderId, senderName, "") + text);
                }
                $(initialElement).remove();
            } else if (code == keys.delete && initialRange == initialStr.length && !$(initialElement).is(':last-child')) {
                var text = $(initialElement).next().html();
                $(initialElement).next().remove();
                $(initialElement).append(addMarker(senderId, senderName, "") + text);
            } else {
                if (code == keys.backspace) {
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

function arrowAction(senderId, senderName, initialElement, finalElement, initialRange, finalRange) {
    var initialStr = $(initialElement).text();
    var finalStr = $(finalElement).text();
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, initialStr.substring(initialRange)));
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html()));
        });
        $(finalElement).html(addMarker(senderId, senderName, finalStr.substring(0, finalRange)) + finalStr.substring(finalRange));
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialRange < finalRange || initialRange == finalRange) {
            $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, initialStr.substring(initialRange, finalRange)) + initialStr.substring(finalRange));
        } else {
            $(initialElement).html(initialStr.substring(0, finalRange) + addMarker(senderId, senderName, initialStr.substring(finalRange, initialRange)) + initialStr.substring(initialRange));
        }
    } else {
        $(initialElement).html(addMarker(senderId, senderName, initialStr.substring(0, initialRange)) + initialStr.substring(initialRange));
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html()));
        });
        $(finalElement).html(finalStr.substring(0, finalRange) + addMarker(senderId, senderName, finalStr.substring(finalRange)));
    }
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

function homeAndEndAction(code, senderId, senderName, initialElement) {
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    if (code == keys.home) {
        $(initialElement).html(addMarker(senderId, senderName, "") + $(initialElement).html());
    } else {
        $(initialElement).append(addMarker(senderId, senderName, ""));
    }
}

function ctrlHomeAndEndAction(code, senderId, senderName) {
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    if (code == keys.ctrlHome || code == keys.pageUp) {
        $(".editor").children().first().prepend(addMarker(senderId, senderName, ""));
    } else {
        $(".editor").children().last().append(addMarker(senderId, senderName, ""));
    }
}

function shiftHomeAndEndAction(code, senderId, senderName, initialElement, initialRange) {
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    var initialStr = $(initialElement).html();
    if (code == keys.shiftHome) {
        $(initialElement).html(addMarker(senderId, senderName, initialStr.substring(0, initialRange)) + initialStr.substring(initialRange));
    } else {
        $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, initialStr.substring(initialRange)));
    }
}

function ctrlShiftHomeAndEndAction(code, senderId, senderName, initialElement, initialRange) {
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    var initialStr = $(initialElement).html();
    if (code == keys.ctrlShiftHome || code == keys.shiftPageUp) {
        $(initialElement).html(addMarker(senderId, senderName, initialStr.substring(0, initialRange)) + initialStr.substring(initialRange));
        $(initialElement).prevAll().each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html()));
        });
    } else {
        $(initialElement).html(initialStr.substring(0, initialRange) + addMarker(senderId, senderName, initialStr.substring(initialRange)));
        $(initialElement).nextAll().each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html()));
        });
    }
}

function ctrlAAction(senderId, senderName) {
    $(".marker[socialprofileid=" + senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    $(".editor").children().each(function () {
        $(this).html(addMarker(senderId, senderName, $(this).html()));
    })
}

function pasteAction(code, senderId, senderName, initialElement, finalElement, initialRange, finalRange) {
    code = code.replace(/'/g, '"');
    var initialStr = $(initialElement).text();
    $(".marker[socialprofileid=" + senderId + "]").remove();
    if ($(initialElement).index() < $(finalElement).index()) {
        var text = $(finalElement).html();
        $(initialElement).append(code + addMarker(senderId, senderName, "") + text);
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialRange < finalRange || initialRange == finalRange) {
            $(initialElement).html(initialStr.substring(0, initialRange) + code + addMarker(senderId, senderName, "") + initialStr.substring(finalRange));
        } else {
            $(initialElement).html(initialStr.substring(0, finalRange) + code + addMarker(senderId, senderName, "") + initialStr.substring(initialRange));
        }
    } else {
        var text = $(initialElement).html();
        $(finalElement).append(code + addMarker(senderId, senderName, "") + text);
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
    }
}