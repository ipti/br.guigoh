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
    var savedSelection = saveSelection($(".editor")[0]);
    if (initialStr !== undefined && finalStr !== undefined) {
        if (msg.type === "NEW_CODE") {
            switch (msg.keyCode) {
                case keys.enter:
                    var textAfterMarker = $(initialElement).first().contents().filter(function () {
                        return this.nodeType === 3 && this.nodeValue.trim() !== '' && $(this).nextAll('.marker').length === 0;
                    });
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    $("<div>" + addMarker(msg.senderId, msg.senderName, "") + textAfterMarker.text() + "</div>").insertAfter(initialElement);
                    textAfterMarker.remove();

                    break;
                case keys.left:
                case keys.right:
                case keys.up:
                case keys.down:
                    var text = $(".marker[socialprofileid=" + msg.senderId + "]").text();
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    $(initialElement).html(initialStr.substring(0, msg.initialRange) + text + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange, initialStr.length));
                    break;
                case keys.backspace:
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    if (msg.initialRange == 0) {
                        if (msg.finalRange == 0) {
                            var text = $(initialElement).text();
                            $(initialElement).prev().find("br").remove();
                            $(initialElement).prev().append(addMarker(msg.senderId, msg.senderName, "") + text);
                            $(initialElement).remove();
                        } else {

                        }
                    }
                    if (msg.initialRange == msg.finalRange) {
                        $(initialElement).html(initialStr.substring(0, msg.initialRange - 1) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange, initialStr.length));
                    } else {
                        $(initialElement).html(initialStr.substring(0, msg.initialRange) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange, initialStr.length));
                    }
                    break;
                default: // MOUSE CLICK
                    $(".marker[socialprofileid=" + msg.senderId + "]").contents().unwrap();
                    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
                    if (msg.initialIndex < msg.finalIndex) {
                        $(initialElement).html(initialStr.substring(0, msg.initialRange) + addMarker(msg.senderId, msg.senderName, initialStr.substring(msg.initialRange, initialStr.length)));
                        $(initialElement).nextUntil($(finalElement)).each(function () {
                            $(this).html(addMarker(msg.senderId, msg.senderName, $(this).text()));
                        });
                        $(finalElement).html(addMarker(msg.senderId, msg.senderName, finalStr.substring(0, msg.finalRange)) + finalStr.substring(msg.finalRange, finalStr.length));
                    } else if (msg.initialIndex == msg.finalIndex) {
                        if (msg.initialRange < msg.finalRange) {
                            $(initialElement).html(initialStr.substring(0, msg.initialRange) + addMarker(msg.senderId, msg.senderName, initialStr.substring(msg.initialRange, msg.finalRange)) + initialStr.substring(msg.finalRange, initialStr.length));
                        } else if (msg.initialRange == msg.finalRange) {
                            $(initialElement).html(initialStr.substring(0, msg.initialRange) + String.fromCharCode(msg.keyCode) + addMarker(msg.senderId, msg.senderName, "") + initialStr.substring(msg.finalRange, initialStr.length));
                        } else {
                            $(initialElement).html(initialStr.substring(0, msg.finalRange) + addMarker(msg.senderId, msg.senderName, initialStr.substring(msg.initialRange, msg.finalRange)) + initialStr.substring(msg.initialRange, initialStr.length));
                        }
                    } else {
                        $(initialElement).html(addMarker(msg.senderId, msg.senderName, initialStr.substring(0, msg.initialRange)) + initialStr.substring(msg.initialRange, initialStr.length));
                        $(finalElement).nextUntil($(initialElement)).each(function () {
                            $(this).html(addMarker(msg.senderId, msg.senderName, $(this).text()));
                        });
                        $(finalElement).html(finalStr.substring(0, msg.finalRange) + addMarker(msg.senderId, msg.senderName, finalStr.substring(msg.finalRange, finalStr.length)));
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
    restoreSelection($(".editor")[0], savedSelection);
}

function addMarker(id, name, text) {
    return "<span socialprofileid='" + id + "' class='marker' title='" + name + "'>" + text + "</span>"
}

$(document).on("keydown keyup", ".editor", function (e) {
    if (e.keyCode == "8" && $(".editor").children().length == 1 && $(".editor").children().text() == "") {
        e.preventDefault();
    } else if (e.keyCode == "37" || e.keyCode == "38" || e.keyCode == "39" || e.keyCode == "40" || (e.keyCode == "8" && e.type == "keydown")) {
        sendCollaboratorChange();
    }
});

$(document).on("mouseup keypress", ".editor", function (e) {
    sendCollaboratorChange(e);
});

function sendCollaboratorChange(e) {
    var initialRange = window.getSelection().anchorOffset;
    var finalRange = window.getSelection().extentOffset;
    var initialNode = document.getSelection().anchorNode;
    var finalNode = document.getSelection().extentNode;
    if (initialNode !== null && finalNode !== null) {
        var initialElement = initialNode.nodeType === 3 ? initialNode.parentNode : initialNode;
        var finalElement = finalNode.nodeType === 3 ? finalNode.parentNode : finalNode;
        var json = '{"keyCode":"' + e.keyCode + '", "senderId":"' + logged_social_profile_id + '",'
                + '"senderName":"' + logged_social_profile_name + '", "initialRange":"' + initialRange + '",'
                + '"finalRange":"' + finalRange + '", "initialIndex":"' + $(initialElement).index() + '",'
                + '"finalIndex":"' + $(finalElement).index() + '", "type":"NEW_CODE"}';
        websocketDocs.send(json);
    }
}

function getContent() {
    $("#text").val($(".editor").html());
    return true;
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