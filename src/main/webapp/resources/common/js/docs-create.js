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

/**
 * For each action done by a collaborator, this method will get the necessary params to replicate to every active participant
 * @param {JSON} senderId, senderName, initialIndex, finalIndex, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange, code
 * @returns void
 */
function onMessageReceivedForDocs(json) {
    var msg = JSON.parse(json.data); // native API
    var initialElement = $(".editor").children().get(msg.initialIndex);
    var finalElement = $(".editor").children().get(msg.finalIndex);
    msg.initialHtmlRange = Number(msg.initialHtmlRange);
    msg.finalHtmlRange = Number(msg.finalHtmlRange);
    msg.initialTextRange = Number(msg.initialTextRange);
    msg.finalTextRange = Number(msg.finalTextRange);
//    var savedSelection = saveSelection($(".editor")[0]);
    $(".marker[socialprofileid=" + msg.senderId + "]").contents().unwrap();
    $(".marker[socialprofileid=" + msg.senderId + "]").remove();
    if ($(initialElement).text() !== undefined && $(finalElement).text() !== undefined) {
        if (msg.type === "NEW_CODE") {
            switch (msg.code) {
                case keys.enter:
                    enterAction(msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange, msg.initialTextRange, msg.finalTextRange);
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
                    arrowAction(msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange);
                    break;
                case keys.backspace:
                    backspaceAction(msg.code, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange, msg.initialTextRange, msg.finalTextRange);
                    break;
                case keys.delete:
                    deleteAction(msg.code, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange, msg.initialTextRange, msg.finalTextRange);
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
                    shiftHomeAndEndAction(msg.code, msg.senderId, msg.senderName, initialElement, msg.initialHtmlRange);
                    break;
                case keys.ctrlShiftHome:
                case keys.ctrlShiftEnd:
                case keys.shiftPageUp:
                case keys.shiftPageDown:
                    ctrlShiftHomeAndEndAction(msg.code, msg.senderId, msg.senderName, initialElement, msg.initialHtmlRange);
                    break;
                case keys.ctrlA:
                    ctrlAAction(msg.senderId, msg.senderName);
                    break;
                default:
                    if (msg.code.indexOf("paste") > -1) {
                        // PASTE
                        pasteAction(msg.code.replace("paste+", ""), msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange, msg.initialTextRange, msg.finalTextRange);
                    } else {
                        // MOUSE CLICK AND LETTER KEYPRESS 
                        if (msg.code === "undefined") {
                            mouseClickAction(msg.code, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange, msg.initialTextRange, msg.finalTextRange)
                        } else {
                            keyPressAction(msg.code, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange);
                        }
                    }
                    break;
            }
            $(".editor").children().each(function () {
                insertBrOnDeepestChild(this);
            });
        }
    }
//    restoreSelection($(".editor")[0], savedSelection);
}

$(document).on("keydown keyup", ".editor", function (e) {
    if (e.keyCode == keys.insert) {
        e.preventDefault();
    } else if (e.keyCode == keys.backspace && e.type == "keydown" || e.keyCode == keys.delete && e.type == "keydown" || e.keyCode == keys.tab && e.type == "keydown") {
        e.preventDefault();
        sendCollaboratorChange(e);
    } else if (((e.keyCode == keys.left || e.keyCode == keys.right || e.keyCode == keys.up || e.keyCode == keys.down) && e.type == "keyup")
            || (e.keyCode == keys.home && e.type == "keydown")
            || (e.keyCode == keys.end && e.type == "keydown")
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
    e.preventDefault();
    sendCollaboratorChange(e);
});

$(document).on("paste", ".editor", function (e) {
    e.preventDefault();
    sendCollaboratorChange(e);
});

$(document).on("cut", ".editor", function (e) {
    sendCollaboratorChange(e);
});

$(document).on("mouseup", function (e) {
    sendCollaboratorChange(e);
});

function sendCollaboratorChange(e) {
    var selection = window.getSelection();
    var initialNode = selection.anchorNode;
    var finalNode = selection.focusNode;
    if ($(initialNode).closest(".editor").length && $(finalNode).closest(".editor").length) {
        var initialElement = getEditorChild(initialNode);
        var finalElement = getEditorChild(finalNode);
        var range = getHtmlOffset(initialElement, finalElement);
        var initialHtmlRange = range.initial;
        var finalHtmlRange = range.final;
        selection = range.selection;
        initialNode = selection.anchorNode;
        finalNode = selection.focusNode;
        var initialTextRange = getTextOffset(initialElement, initialNode, selection.anchorOffset);
        var finalTextRange = getTextOffset(finalElement, finalNode, selection.focusOffset);
        var code = checkCodeToSend(e, initialElement, finalElement, initialHtmlRange, finalHtmlRange);
        var json = '{"code":"' + code + '", "senderId":"' + logged_social_profile_id + '",'
                + '"senderName":"' + logged_social_profile_name + '", "initialHtmlRange":"' + initialHtmlRange + '",'
                + '"finalHtmlRange":"' + finalHtmlRange + '", "initialTextRange":"' + initialTextRange + '",'
                + '"finalTextRange":"' + finalTextRange + '", "initialIndex":"' + $(initialElement).index() + '",'
                + '"finalIndex":"' + $(finalElement).index() + '", "type":"NEW_CODE"}';
        websocketDocs.send(json);
        changeLocalActions(e, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
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

function checkCodeToSend(e, initialElement, finalElement, initialHtmlRange, finalHtmlRange) {
    if (e.type == "cut") {
        if (initialHtmlRange == finalHtmlRange && initialElement == finalElement) {
            return undefined;
        } else {
            return keys.delete;
        }
    } else if (e.type == "paste") {
        var htmlArray = $.parseHTML((event.originalEvent || event).clipboardData.getData('text/html'));
        if (htmlArray !== null) {
            var htmlString = "";
            htmlArray.splice(0, 1);
            for (var i in htmlArray) {
                htmlString += htmlArray[i].outerHTML;
            }
            return keys.paste + "+" + htmlString.replace(/'/g, "").replace(/"/g, "'");
        } else {
            return keys.paste + "+" + e.originalEvent.clipboardData.getData("text").replace(/'/g, "").replace(/"/g, "'");
        }
    } else {
        return e.keyCode;
    }
}

function changeLocalActions(e, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    if (e.type == "paste") {
        var code;
        var htmlArray = $.parseHTML((event.originalEvent || event).clipboardData.getData('text/html'));
        if (htmlArray !== null) {
            var htmlString = "";
            htmlArray.splice(0, 1);
            for (var i in htmlArray) {
                htmlString += htmlArray[i].outerHTML;
            }
            code = htmlString.replace(/'/g, "").replace(/"/g, "'");
        } else {
            code = e.originalEvent.clipboardData.getData("text").replace(/'/g, "").replace(/"/g, "'");
        }
        pasteAction(code, null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
    } else if (e.keyCode == keys.backspace) {
        backspaceAction(e.keyCode, null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
    } else if (e.keyCode == keys.delete) {
        deleteAction(e.keyCode, null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
    } else if (e.type == "keypress" || e.keyCode == keys.tab) {
        if (e.keyCode == keys.enter) {
            enterAction(null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
        } else {
            keyPressAction(e.keyCode, null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
        }
    }
}

function setLocalCaretPosition(el, sPos, senderId) {
    if (senderId == null) {
        /*range = document.createRange();                    
         range.setStart(el.firstChild, sPos);
         range.setEnd  (el.firstChild, sPos);*/
        var charIndex = 0, range = document.createRange();
        
        insertBrOnDeepestChild(el);
        
        range.setStart(el, 0);
        range.collapse(true);
        var nodeStack = [el], node, foundStart = false, stop = false;

        while (!stop && (node = nodeStack.pop())) {
            if (node.nodeType == 3) {
                var nextCharIndex = charIndex + node.length;
                if (!foundStart && sPos >= charIndex && sPos <= nextCharIndex) {
                    range.setStart(node, sPos - charIndex);
                    foundStart = true;
                }
                if (foundStart && sPos >= charIndex && sPos <= nextCharIndex) {
                    range.setEnd(node, sPos - charIndex);
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
        selection = window.getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
    }
}

function insertBrOnDeepestChild(element) {
    if ($(element).html() == "") {
        $(element).append("<br>");
    } else {
        if ($(element).text() == "") {
            if ($(element).children().not(".marker, br").length == 0) {
                $(element).find("br").remove();
                $(element).append("<br>");
            } else {
                var target = $(element).children().not(".marker, br"),
                        next = target;

                while (next.length) {
                    if (next.parent().find("br").length) {
                        next.parent().find("br").remove();
                    }
                    target = next;
                    next = next.children().not(".marker, br");
                }
                if (target.children("br").length == 0) {
                    target.find("br").remove();
                    target.append("<br>");
                }
            }
        } else {
            $(element).find("br").remove();
        }
    }
}

function getContent() {
    $("#text").val($(".editor").html());
    return true;
}

function getHtmlOffset(initialElement, finalElement) {
    var marker = $("<span id='offset-marker-" + logged_social_profile_id + "'></span>");
    var sel = document.getSelection();

    var range = document.createRange();

    range.setStart(sel.anchorNode, sel.anchorOffset);
    range.insertNode(marker[0]);
    var initialOffset = $(initialElement).html().indexOf(marker[0].outerHTML);
    marker.remove();

    range.setStart(sel.focusNode, sel.focusOffset);
    range.insertNode(marker[0]);
    var finalOffset = $(finalElement).html().indexOf(marker[0].outerHTML);
    marker.remove();

    return {initial: initialOffset, final: finalOffset, selection: sel};
}

function getTextOffset(container, node, offset) {
    var range = document.createRange();
    range.selectNodeContents(container);
    range.setEnd(node, offset);
    return range.toString().length;
}

function getHtmlSubstring(str, start, count) {

    var div = document.createElement('div');
    div.innerHTML = str;

    walk(div, track);

    function track(el) {
        if (count > 0) {
            var len = el.data.length;
            if (start <= len) {
                el.data = el.substringData(start, len);
                start = 0;
            } else {
                start -= len;
                el.data = '';
            }
            len = el.data.length;
            count -= len;
            if (count <= 0) {
                el.data = el.substringData(0, el.data.length + count);
            }

        } else {
            el.data = '';
        }
    }

    function walk(el, fn) {
        var node = el.firstChild;
        do {
            if (node.nodeType === 3) {
                fn(node);
            } else if (node.nodeType === 1 && node.childNodes && node.childNodes[0]) {
                walk(node, fn);
            }
        } while (node = node.nextSibling);
    }
    return div.innerHTML;
}

function addMarker(id, name, html, initialHtmlRange, finalHtmlRange) {
    if (id != null) {
        var marker = "";
        if (html != "" && initialHtmlRange != finalHtmlRange) {
            var parsedHtml = $.parseHTML(html.substring(initialHtmlRange, finalHtmlRange));
            for (var i = 0; i < parsedHtml.length; i++) {
                if (parsedHtml[i].nodeType == 3) {
                    marker += "<span contenteditable='false' socialprofileid='" + id + "' class='marker' title='" + name + "'>" + parsedHtml[i].nodeValue + "</span>";
                } else if (parsedHtml[i].nodeType == 1) {
                    parsedHtml[i].innerHTML = "<span contenteditable='false' socialprofileid='" + id + "' class='marker' title='" + name + "'>" + parsedHtml[i].innerHTML + "</span>";
                    marker += parsedHtml[i].outerHTML;
                    if (i == parsedHtml.length - 1) {
                        marker = marker.substring(0, marker.length - (3 + parsedHtml[i].nodeName.length));
                    }
                }
            }
        } else {
            marker = "<span contenteditable='false' socialprofileid='" + id + "' class='marker' title='" + name + "'></span>";
        }
        return marker;
    } else {
        return "";
    }
}

//ajeitar cursor
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

/**
 * For each one of these action methods, the general strategy adopted was:
 *      - For every action, we have 5 posibilities:
 *          - The initial element is before final element;
 *          - The initial element and final element are the same, but the user highlighted  more than one letter, from left to right;
 *          - The initial element and final element are the same, and the range is the same (i.e: a simple mouse click);
 *          - The initial element and final element are the same, but the user highlighted  more than one letter, from right to left;
 *          - The initial element is after final element;
 * Mostly of these methods are 'if' conditioned in that structure.
 * @param {String} code             : Code sent by a collaborator. Can be a simple keycode number or a text/html.
 * @param {String} senderId         : Sender unique id.
 * @param {String} senderName       : Sender name. Passed via Json to avoid a sql search in websocket's callback.
 * @param {String} initialElement   : DOM element of the initial index (where the sender action started).
 * @param {String} finalElement     : DOM element of the final index (where the sender action finished).
 * @param {String} initialHtmlRange     : Collaborator's initial caret offset (counting html structure, not only text).
 * @param {String} finalHtmlRange       : Collaborator's final caret offset (counting html structure, not only text).
 * @param {String} initialTextRange     : Collaborator's initial caret offset (counting only text).
 * @param {String} finalTextRange       : Collaborator's final caret offset (counting only text).
 * @returns void
 */

//tratar quando da highlight e digita um caractere
function keyPressAction(code, senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    if (code == keys.space) {
        code = keys.nonBreakingSpace;
    }
    if (code == keys.tab) {
        code = keys.emphasizedSpace;
    }
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialHtml + String.fromCharCode(code));
        $(initialElement).append(addMarker(senderId, senderName, "") + finalHtml);
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        setLocalCaretPosition(initialElement, initialTextRange + 1, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange || initialHtmlRange == finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + String.fromCharCode(code) + addMarker(senderId, senderName, "") + initialHtml.substring(finalHtmlRange));
            setLocalCaretPosition(initialElement, initialTextRange + 1, senderId);
        } else {
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + String.fromCharCode(code) + addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
            setLocalCaretPosition(initialElement, finalTextRange + 1, senderId);
        }
    } else {
        $(finalElement).html(finalHtml + String.fromCharCode(code));
        $(finalElement).append(addMarker(senderId, senderName, "") + initialHtml);
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
        setLocalCaretPosition(finalElement, finalTextRange + 1, senderId);
    }
}

function mouseClickAction(code, senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange) {
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, initialHtml, initialHtmlRange, initialHtml.length));
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html(), 0, $(this).html().length));
        });
        $(finalElement).html(addMarker(senderId, senderName, finalHtml, 0, finalHtmlRange) + finalHtml.substring(finalHtmlRange));
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, initialHtml, initialHtmlRange, finalHtmlRange) + initialHtml.substring(finalHtmlRange));
        } else if (initialHtmlRange == finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + String.fromCharCode(code) + addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
        } else {
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + addMarker(senderId, senderName, initialHtml, finalHtmlRange, initialHtmlRange) + initialHtml.substring(initialHtmlRange));
        }
    } else {
        $(initialElement).html(addMarker(senderId, senderName, initialHtml, 0, initialHtmlRange) + initialHtml.substring(initialHtmlRange));
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html(), 0, $(this).html().length));
        });
        $(finalElement).html(finalHtml.substring(0, finalHtmlRange) + addMarker(senderId, senderName, finalHtml, finalHtmlRange, finalHtml.length));
    }
}

function backspaceAction(code, senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange));
        $(initialElement).append(addMarker(senderId, senderName, "") + finalHtml.substring(finalHtmlRange));
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        setLocalCaretPosition(initialElement, initialTextRange, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + initialHtml.substring(finalHtmlRange));
            setLocalCaretPosition(initialElement, initialTextRange, senderId);
        } else if (initialHtmlRange == finalHtmlRange) {
            if (initialTextRange == 0 && $(initialElement).index() !== 0) {
                var prevRange = $(initialElement).prev().text().length;
                if ($(initialElement).prev().html() == "") {
                    $(initialElement).prev().html(addMarker(senderId, senderName, "") + initialHtml);
                } else {
                    $(initialElement).prev().html($(initialElement).prev().html() + addMarker(senderId, senderName, "") + initialHtml);
                }
                setLocalCaretPosition(initialElement.previousSibling, prevRange, senderId);
                $(initialElement).remove();
            } else {
                $(initialElement).html(initialHtml.substring(0, initialHtmlRange - 1) + addMarker(senderId, senderName, "") + initialHtml.substring(finalHtmlRange));
                //remover tag quando nao houver mais letra dentro                
//                var marker = $(initialElement).find(".marker");
//                if (marker.parent().text() == "") {
//                    if (!marker.parent().parent().hasClass("editor") && marker.parent().parent().text() != "") {
//                        var markerParent = marker.parent();
//                        markerParent.parent().append(marker);
//                        markerParent.remove();
//                    }
//                }
                setLocalCaretPosition(initialElement, initialTextRange - 1, senderId);
            }
        } else {
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
            setLocalCaretPosition(initialElement, finalTextRange, senderId);
        }
    } else {
        $(finalElement).html(finalHtml.substring(0, finalHtmlRange));
        $(finalElement).append(addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
        setLocalCaretPosition(finalElement, finalTextRange, senderId);
    }
}

function deleteAction(code, senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    var initialText = $(initialElement).text();
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange));
        $(initialElement).append(addMarker(senderId, senderName, "") + finalHtml.substring(finalHtmlRange));
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        setLocalCaretPosition(initialElement, initialTextRange, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + initialHtml.substring(finalHtmlRange));
            setLocalCaretPosition(initialElement, initialTextRange, senderId);
        } else if (initialHtmlRange == finalHtmlRange) {
            if (initialTextRange == initialText.length && !$(initialElement).is(':last-child')) {
                $(initialElement).html(initialHtml + addMarker(senderId, senderName, "") + $(initialElement).next().html());
                $(initialElement).next().remove();
                setLocalCaretPosition(initialElement, initialText.length, senderId);
            } else {
                //tratar essa porcaria
                //fazer direito (remover tag)
                var parsedHtml = $.parseHTML(getHtmlSubstring(initialHtml, initialTextRange, initialText.length - initialTextRange));
                if (parsedHtml[0].nodeType == 3) {
                    $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + initialHtml.substring(finalHtmlRange + 1));
                } else if (parsedHtml[0].nodeType == 1) {
                    parsedHtml[0].innerHTML = parsedHtml[0].innerHTML.substring(1, parsedHtml[0].innerHTML.length);
                    if (parsedHtml[0].innerHTML == "") {
                        parsedHtml.splice(0, 1);
                    }
                    var html = "";
                    for (var i = 0; i < parsedHtml.length; i++) {
                        html += (parsedHtml[i].nodeType == 3) ? parsedHtml[i].nodeValue : parsedHtml[i].outerHTML;
                    }
                    $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + html);
                }
                setLocalCaretPosition(initialElement, initialTextRange, senderId);
            }
        } else {
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
            setLocalCaretPosition(initialElement, finalTextRange, senderId);
        }
    } else {
        $(finalElement).html(finalHtml.substring(0, finalHtmlRange));
        $(finalElement).append(addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
        setLocalCaretPosition(finalElement, finalTextRange, senderId);
    }
}

function enterAction(senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    var initialText = $(initialElement).text();
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange));
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        $(initialElement).clone().html(addMarker(senderId, senderName, "") + finalHtml).insertAfter(initialElement);
        setLocalCaretPosition(initialElement.nextSibling, 0, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        var html;
        if (initialHtmlRange < finalHtmlRange || initialHtmlRange == finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange));
            html = getHtmlSubstring(initialHtml, finalTextRange, initialText.length - finalTextRange);
        } else {
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange));
            html = getHtmlSubstring(initialHtml, initialTextRange, initialText.length - initialTextRange);
        }
        $(initialElement).clone().html(addMarker(senderId, senderName, "") + html).insertAfter(initialElement);
        setLocalCaretPosition(initialElement.nextSibling, 0, senderId);
    } else {
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
        $(initialElement).clone().html(addMarker(senderId, senderName, "") + finalHtml).insertAfter(finalElement);
        setLocalCaretPosition(finalElement.nextSibling, 0, senderId);
    }
}

function arrowAction(senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange) {
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, initialHtml, initialHtmlRange, initialHtml.length));
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html(), 0, $(this).html().length));
        });
        $(finalElement).html(addMarker(senderId, senderName, finalHtml, 0, finalHtmlRange) + finalHtml.substring(finalHtmlRange));
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange || initialHtmlRange == finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, initialHtml, initialHtmlRange, finalHtmlRange) + initialHtml.substring(finalHtmlRange));
        } else {
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + addMarker(senderId, senderName, initialHtml, finalHtmlRange, initialHtmlRange) + initialHtml.substring(initialHtmlRange));
        }
    } else {
        $(initialElement).html(addMarker(senderId, senderName, initialHtml, 0, initialHtmlRange) + initialHtml.substring(initialHtmlRange));
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html(), 0, $(this).html().length));
        });
        $(finalElement).html(finalHtml.substring(0, finalHtmlRange) + addMarker(senderId, senderName, finalHtml, finalHtmlRange, finalHtml.length));
    }
}

function homeAndEndAction(code, senderId, senderName, initialElement) {
    if (code == keys.home) {
        $(initialElement).html(addMarker(senderId, senderName, "") + $(initialElement).html());
    } else {
        $(initialElement).append(addMarker(senderId, senderName, ""));
    }
}

function ctrlHomeAndEndAction(code, senderId, senderName) {
    if (code == keys.ctrlHome || code == keys.pageUp) {
        $(".editor").children().first().prepend(addMarker(senderId, senderName, ""));
    } else {
        $(".editor").children().last().append(addMarker(senderId, senderName, ""));
    }
}

function shiftHomeAndEndAction(code, senderId, senderName, initialElement, initialHtmlRange) {
    var initialHtml = $(initialElement).html();
    if (code == keys.shiftHome) {
        $(initialElement).html(addMarker(senderId, senderName, initialHtml, 0, initialHtmlRange) + initialHtml.substring(initialHtmlRange));
    } else {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, initialHtml, initialHtmlRange, initialHtml.length));
    }
}

function ctrlShiftHomeAndEndAction(code, senderId, senderName, initialElement, initialHtmlRange) {
    var initialHtml = $(initialElement).html();
    if (code == keys.ctrlShiftHome || code == keys.shiftPageUp) {
        $(initialElement).html(addMarker(senderId, senderName, initialHtml, 0, initialHtmlRange) + initialHtml.substring(initialHtmlRange));
        $(initialElement).prevAll().each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html(), 0, $(this).html().length));
        });
    } else {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, initialHtml, initialHtmlRange, initialHtml.length));
        $(initialElement).nextAll().each(function () {
            $(this).html(addMarker(senderId, senderName, $(this).html(), 0, $(this).html().length));
        });
    }
}

function ctrlAAction(senderId, senderName) {
    $(".editor").children().each(function () {
        $(this).html(addMarker(senderId, senderName, $(this).html(), 0, $(this).html().length));
    })
}

//ajeitar pra trazer só texto
function pasteAction(code, senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    code = code.replace(/'/g, '"');
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    var parsedHtml = $.parseHTML(code);
    var pasteLength = parsedHtml[0].innerHTML == undefined ? parsedHtml[0].length : parsedHtml[0].innerHTML.length;
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange))
        $(initialElement).append(code + addMarker(senderId, senderName, "") + finalHtml.substring(finalHtmlRange));
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        setLocalCaretPosition(initialElement, initialTextRange + pasteLength, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange || initialHtmlRange == finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + code + addMarker(senderId, senderName, "") + initialHtml.substring(finalHtmlRange));
            setLocalCaretPosition(initialElement, initialTextRange + pasteLength, senderId);
        } else {
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + code + addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
            setLocalCaretPosition(initialElement, finalTextRange + pasteLength, senderId);
        }
    } else {
        $(finalElement).html(finalHtml.substring(0, finalHtmlRange));
        $(finalElement).append(code + addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
        setLocalCaretPosition(finalElement, finalTextRange + pasteLength, senderId);
    }
}