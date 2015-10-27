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
    var initialStr = $(initialElement).text();
    var finalStr = $(finalElement).text();
//    var savedSelection = saveSelection($(".editor")[0]);
    msg.initialIndex = Number(msg.initialIndex);
    msg.finalIndex = Number(msg.finalIndex);
    msg.initialRange = Number(msg.initialRange);
    msg.finalRange = Number(msg.finalRange);

    if (initialStr !== undefined && finalStr !== undefined) {
        if (msg.type === "NEW_CODE") {
            switch (msg.keyCode) {
                case keys.space:
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    if (msg.initialIndex < msg.finalIndex) {
                        var text = $(finalElement).html();
                        $(initialElement).append(String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + text);
                        $(initialElement).nextUntil($(finalElement)).each(function () {
                            $(this).remove();
                        });
                        $(finalElement).remove();
                    } else if (msg.initialIndex == msg.finalIndex) {
                        var space;
                        if (msg.initialRange < msg.finalRange) {
                            space = initialStr.substring(msg.finalRange) == "" ? "&nbsp;" : " ";
                            $(initialElement).html(initialStr.substring(0, msg.initialRange) + space + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                        } else if (msg.initialRange == msg.finalRange) {
                            var text = initialStr.substring(0, msg.initialRange)
                            if (text.charCodeAt(text.length - 1) == Number(keys.nonBreakingSpace) && text.charCodeAt(text.length - 2) != Number(keys.space)) {
                                space = " &nbsp;";
                                $(initialElement).html(initialStr.substring(0, msg.initialRange - 1) + space + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                            } else {
                                space = "&nbsp;";
                                $(initialElement).html(initialStr.substring(0, msg.initialRange) + space + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                            }
                        } else {
                            space = initialStr.substring(msg.initialRange) == "" ? "&nbsp;" : " ";
                            $(initialElement).html(initialStr.substring(0, msg.finalRange) + space + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.initialRange));
                        }
                    } else {
                        var text = $(initialElement).html();
                        $(finalElement).append(String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + text);
                        $(finalElement).nextUntil($(initialElement)).each(function () {
                            $(this).remove();
                        });
                        $(initialElement).remove();
                    }
                    break;
                case keys.enter:
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    if (msg.initialIndex < msg.finalIndex) {
                        var text = $(finalElement).html();
                        $(initialElement).nextUntil($(finalElement)).each(function () {
                            $(this).remove();
                        });
                        $(finalElement).remove();
                        $("<div>" + addMarker(msg.senderId, msg.senderName, "") + text + "</div>").insertAfter(initialElement);
                    } else if (msg.initialIndex == msg.finalIndex) {
                        var text = $(initialElement).html();
                        $(initialElement).html(text.substring(0, msg.initialRange < msg.finalRange ? msg.initialRange : msg.finalRange));
                        $("<div>" + addMarker(msg.senderId, msg.senderName, "") + text.substring(msg.initialRange < msg.finalRange ? msg.initialRange : msg.finalRange) + "</div>").insertAfter(initialElement);
                    } else {
                        var text = $(initialElement).html();
                        $(finalElement).nextUntil($(initialElement)).each(function () {
                            $(this).remove();
                        });
                        $(initialElement).remove();
                        $("<div>" + addMarker(msg.senderId, msg.senderName, "") + text + "</div>").insertAfter(finalElement);
                    }
                    break;
                case keys.left:
                case keys.right:
                case keys.up:
                case keys.down:
                    $(".marker[socialprofileid=" + msg.senderId + "]").contents().unwrap();
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    $(initialElement).html(initialStr.substring(0, msg.initialRange) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                    break;
                case keys.backspace:
                case keys.delete:
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    if (msg.initialIndex < msg.finalIndex) {
                        var text = $(finalElement).html();
                        $(initialElement).append(addMarker(msg.senderId, msg.senderName, "") + text);
                        $(initialElement).nextUntil($(finalElement)).each(function () {
                            $(this).remove();
                        });
                        $(finalElement).remove();
                    } else if (msg.initialIndex == msg.finalIndex) {
                        if (msg.initialRange < msg.finalRange) {
                            $(initialElement).html(initialStr.substring(0, msg.initialRange) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                        } else if (msg.initialRange == msg.finalRange) {
                            if (msg.keyCode == keys.backspace && msg.initialRange == 0 && msg.initialIndex !== 0) {
                                var text = $(initialElement).html();
                                if ($(initialElement).prev().text() == "") {
                                    $(initialElement).prev().html(addMarker(msg.senderId, msg.senderName, "") + text);
                                } else {
                                    $(initialElement).prev().append(addMarker(msg.senderId, msg.senderName, "") + text);
                                }
                                $(initialElement).remove();
                            } else if (msg.keyCode == keys.delete && msg.initialRange == initialStr.length && !$(initialElement).is(':last-child')) {
                                var text = $(initialElement).next().html();
                                $(initialElement).next().remove();
                                $(initialElement).append(addMarker(msg.senderId, msg.senderName, "") + text);
                            } else {
                                if (msg.keyCode == keys.backspace) {
                                    $(initialElement).html(initialStr.substring(0, msg.initialRange - 1) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                                } else {
                                    $(initialElement).html(initialStr.substring(0, msg.initialRange) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange + 1));
                                }
                            }
                        } else {
                            $(initialElement).html(initialStr.substring(0, msg.finalRange) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.initialRange));
                        }
                    } else {
                        var text = $(initialElement).html();
                        $(finalElement).append(addMarker(msg.senderId, msg.senderName, "") + text);
                        $(finalElement).nextUntil($(initialElement)).each(function () {
                            $(this).remove();
                        });
                        $(initialElement).remove();
                    }
                    break;
                default: 
                    // MOUSE CLICK AND LETTER KEYPRESS 
                    // When keyCode is "undefined", jquery trigger event was "mouseup"
                    if (msg.keyCode === "undefined") {
                        $(".marker[socialprofileid=" + msg.senderId + "]").contents().unwrap();
                    }
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    if (msg.initialIndex < msg.finalIndex) {
                        if (msg.keyCode !== "undefined") {
                            var text = $(finalElement).html();
                            $(initialElement).append(String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + text);
                            $(initialElement).nextUntil($(finalElement)).each(function () {
                                $(this).remove();
                            });
                            $(finalElement).remove();
                        } else {
                            $(initialElement).html(initialStr.substring(0, msg.initialRange) + addMarker(msg.senderId, msg.senderName, initialStr.substring(msg.initialRange)));
                            $(initialElement).nextUntil($(finalElement)).each(function () {
                                $(this).html(addMarker(msg.senderId, msg.senderName, $(this).html()));
                            });
                            $(finalElement).html(addMarker(msg.senderId, msg.senderName, finalStr.substring(0, msg.finalRange)) + finalStr.substring(msg.finalRange));
                        }
                    } else if (msg.initialIndex == msg.finalIndex) {
                        if (msg.initialRange < msg.finalRange) {
                            if (msg.keyCode !== "undefined") {
                                $(initialElement).html(initialStr.substring(0, msg.initialRange) + String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                            } else {
                                $(initialElement).html(initialStr.substring(0, msg.initialRange) + addMarker(msg.senderId, msg.senderName, initialStr.substring(msg.initialRange, msg.finalRange)) + initialStr.substring(msg.finalRange));
                            }
                        } else if (msg.initialRange == msg.finalRange) {
                            if (msg.keyCode !== "undefined") {
                                var text = initialStr.substring(0, msg.initialRange);
                                if (text.charCodeAt(text.length - 1) == Number(keys.nonBreakingSpace) && text.charCodeAt(text.length - 2) != Number(keys.space)) {
                                    $(initialElement).html(initialStr.substring(0, msg.initialRange - 1) + " " + String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                                } else {
                                    $(initialElement).html(initialStr.substring(0, msg.initialRange) + String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                                }
                            } else {
                                $(initialElement).html(initialStr.substring(0, msg.initialRange) + String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange));
                            }
                        } else {
                            if (msg.keyCode !== "undefined") {
                                $(initialElement).html(initialStr.substring(0, msg.finalRange) + String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.initialRange));
                            } else {
                                $(initialElement).html(initialStr.substring(0, msg.finalRange) + addMarker(msg.senderId, msg.senderName, initialStr.substring(msg.initialRange, msg.finalRange)) + initialStr.substring(msg.initialRange));
                            }
                        }
                    } else {
                        if (msg.keyCode !== "undefined") {
                            var text = $(initialElement).html();
                            $(finalElement).append(String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + text);
                            $(finalElement).nextUntil($(initialElement)).each(function () {
                                $(this).remove();
                            });
                            $(initialElement).remove();
                        } else {
                            $(initialElement).html(addMarker(msg.senderId, msg.senderName, initialStr.substring(0, msg.initialRange)) + initialStr.substring(msg.initialRange));
                            $(finalElement).nextUntil($(initialElement)).each(function () {
                                $(this).html(addMarker(msg.senderId, msg.senderName, $(this).html()));
                            });
                            $(finalElement).html(finalStr.substring(0, msg.finalRange) + addMarker(msg.senderId, msg.senderName, finalStr.substring(msg.finalRange)));
                        }
                    }
                    break;
            }
            $(".editor div").each(function () {
                if ($(this).html() == "") {
                    $(this).append("<br>");
                }
            })
        }
    }
//    restoreSelection($(".editor")[0], savedSelection);
}

function addMarker(id, name, text) {
    return "<span contenteditable='false' socialprofileid='" + id + "' class='marker' title='" + name + "'>" + text + "</span>";
}

$(document).on("keydown keyup", ".editor", function (e) {
    if (e.keyCode == keys.backspace && $(".editor").children().length == 1 && $(".editor").children().text() == "") {
        e.preventDefault();
    } else if (((e.keyCode == keys.left || e.keyCode == keys.right || e.keyCode == keys.up || e.keyCode == keys.down) && e.type == "keyup")
            || (e.keyCode == keys.backspace && e.type == "keydown")
            || (e.keyCode == keys.delete && e.type == "keydown")) {
        sendCollaboratorChange(e);
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
    var finalNode = document.getSelection().extentNode;
    if (initialNode !== null && finalNode !== null) {
        var initialElement = initialNode.nodeType === 3 ? initialNode.parentNode : initialNode;
        var finalElement = finalNode.nodeType === 3 ? finalNode.parentNode : finalNode;
        var initialRange = getCharOffsetRelativeTo(initialElement, document.getSelection().anchorNode, document.getSelection().anchorOffset);
        var finalRange = getCharOffsetRelativeTo(finalElement, document.getSelection().extentNode, document.getSelection().extentOffset);
        if ($(initialNode).closest(".editor").length && $(finalNode).closest(".editor").length) {
            var json = '{"keyCode":"' + e.keyCode + '", "senderId":"' + logged_social_profile_id + '",'
                    + '"senderName":"' + logged_social_profile_name + '", "initialRange":"' + initialRange + '",'
                    + '"finalRange":"' + finalRange + '", "initialIndex":"' + $(initialElement).index() + '",'
                    + '"finalIndex":"' + $(finalElement).index() + '", "type":"NEW_CODE"}';
            websocketDocs.send(json);
        }
    }
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