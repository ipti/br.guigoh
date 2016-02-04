var websocketDocs = '';
var savedSelection;
var keys = {
    enter: "13", left: "37", up: "38", right: "39", down: "40", ctrlLeft: "17+37", ctrlUp: "17+38", ctrlRight: "17+39", ctrlDown: "17+40",
    shiftLeft: "16+37", shiftUp: "16+38", shiftRight: "16+39", shiftDown: "16+40", ctrlShiftLeft: "17+16+37", ctrlShiftUp: "17+16+38",
    ctrlShiftRight: "17+16+39", ctrlShiftDown: "17+16+40", backspace: "8", delete: "46", space: "32", nonBreakingSpace: "160", ctrl: "17",
    shift: "16", ctrlHome: "17+36", ctrlEnd: "17+35", shiftHome: "16+36", shiftEnd: "16+35", ctrlShiftHome: "17+16+36", ctrlShiftEnd: "17+16+35",
    home: "36", end: "35", tab: "9", emphasizedSpace: "8195", a: "65", ctrlA: "17+65", insert: "45", pageUp: "33", pageDown: "34",
    shiftPageUp: "16+33", shiftPageDown: "16+34", paste: "paste", lowerThan: "60", greaterThan: "62", ampersand: "38", semicolon: "59",
    z: "90", y: "89", ctrlZ: "17+90", ctrlY: "17+89"
}
var undoStack = new stack();
var redoStack = new stack();

$(document).ready(function () {
    var collaboratorsIds = logged_social_profile_id == 26 ? "246" : "26";
    websocketDocs = new WebSocket("ws://" + window.location.host + "/socket/docs-old/" + logged_social_profile_id + "/" + encodeURIComponent(collaboratorsIds));
    websocketDocs.onmessage = onMessageReceivedForDocs;
});

/**
 * For each action done by a collaborator, this method will get the necessary params to replicate to every active participant
 * @param {JSON} senderId, senderName, initialIndex, finalIndex, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange, code, type
 * @returns void
 */
function onMessageReceivedForDocs(json) {
    var msg = JSON.parse(json.data); // native API
    if (msg.type === "NEW_CODE") {
        var initialElement = $(".editor").children().get(msg.initialIndex);
        var finalElement = $(".editor").children().get(msg.finalIndex);
        msg.code = msg.code.toString();
        var savedSelection = saveSelection($(".editor")[0]);
        $(".marker[socialprofileid=" + msg.senderId + "]").contents().unwrap();
        $(".marker[socialprofileid=" + msg.senderId + "]").remove();
        if ($(initialElement).text() !== undefined && $(finalElement).text() !== undefined) {
            switch (msg.code) {
                case keys.enter:
                    enterAction(msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange, msg.initialTextRange, msg.finalTextRange);
                    break;
                case "caret" + keys.left:
                case "caret" + keys.right:
                case "caret" + keys.up:
                case "caret" + keys.down:
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
                case "caret" + keys.home:
                case "caret" + keys.end:
                    homeAndEndAction(msg.code, msg.senderId, msg.senderName, initialElement);
                    break;
                case keys.ctrlHome:
                case keys.ctrlEnd:
                case "caret" + keys.pageUp:
                case "caret" + keys.pageDown:
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
                case keys.ctrlZ:
                    undoAction();
                    break;
                case keys.ctrlY:
                    redoAction();
                    break;
                default:
                    if (msg.code.indexOf("paste") > -1) {
                        pasteAction(msg.code.replace("paste+", ""), msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange, msg.initialTextRange, msg.finalTextRange);
                    } else {
                        if (msg.code === "undefined") {
                            mouseClickAction(msg.code, msg.senderId, msg.senderName, initialElement, finalElement, msg.initialHtmlRange, msg.finalHtmlRange, msg.initialTextRange, msg.finalTextRange);
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
        restoreSelection($(".editor")[0], savedSelection);
    } else if (msg.type === "STACK") {
        undoStack.push(msg);
        redoStack = new stack();
    }
}

$(document).on("keydown keyup", ".editor", function (e) {
    if (e.keyCode == keys.insert) {
        e.preventDefault();
    } else if (e.keyCode == keys.backspace && e.type == "keydown" || e.keyCode == keys.delete && e.type == "keydown"
            || e.keyCode == keys.tab && e.type == "keydown" || e.keyCode == keys.z && e.type == "keydown" && e.ctrlKey
            || e.keyCode == keys.y && e.type == "keydown" && e.ctrlKey) {
        if (e.keyCode == keys.z && e.ctrlKey || e.keyCode == keys.y && e.ctrlKey) {
            e.keyCode = keys.ctrl + "+" + e.keyCode;
        }
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
            if (!e.ctrlKey && !e.shiftKey) {
                e.keyCode = "caret" + e.keyCode
            }
        }
        if (e.keyCode == keys.a && e.ctrlKey) {
            e.keyCode = keys.ctrl + "+" + e.keyCode;
        }
        if ((e.keyCode == keys.pageDown || e.keyCode == keys.pageUp)) {
            if (e.shiftKey) {
                e.keyCode = keys.shift + "+" + e.keyCode;
            } else {
                e.keyCode = "caret" + e.keyCode;
            }
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
    if ($(e.target).hasClass("editor-plugin")) {
        var savedSelection = saveSelection($(".editor")[0]);
        sendCollaboratorChange(e);
        restoreSelection($(".editor")[0], savedSelection);
    } else {
        sendCollaboratorChange(e);
    }
});

$(document).on("dragover drop", function (e) {
    e.preventDefault();
});

function sendCollaboratorChange(e) {
    var selection = window.getSelection();
    var initialNode = selection.anchorNode;
    var finalNode = selection.focusNode;
    if (($(initialNode).closest(".editor").length && $(finalNode).closest(".editor").length)) {
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
        var json = {
            code: code == undefined ? "undefined" : code,
            senderId: logged_social_profile_id,
            senderName: logged_social_profile_name,
            initialHtmlRange: initialHtmlRange,
            finalHtmlRange: finalHtmlRange,
            initialTextRange: initialTextRange,
            finalTextRange: finalTextRange,
            initialIndex: $(initialElement).index(),
            finalIndex: $(finalElement).index(),
            type: "NEW_CODE"
        }
        websocketDocs.send(JSON.stringify(json));
        changeLocalActions(e, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
    }
}

function stack() {
    this.list = new Array();

    this.push = function (obj) {
        if (this.list.length >= 500) {
            this.list.splice(0, 1);
        }
        this.list[this.list.length] = obj;
    }

    this.pop = function () {
        if (this.list.length > 0) {
            var obj = this.list[this.list.length - 1];
            this.list.splice(this.list.length - 1, 1);
            return obj;
        }
    }

    this.read = function () {
        if (this.list.length > 0) {
            return this.list[this.list.length - 1];
        }
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
        return keys.paste + "+" + e.originalEvent.clipboardData.getData("text").replace(/'/g, "").replace(/"/g, "'").replace(/\n/g, "\\n");
    } else {
        return e.keyCode;
    }
}

function changeLocalActions(e, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    var insertOnStack = true;
    var editorPreviousHtml = $(".editor").html();
    var elementIndex;
    var range;
    if ($(initialElement).index() < $(finalElement).index()) {
        elementIndex = $(initialElement).index();
        range = initialTextRange;
    } else if ($(initialElement).index() == $(finalElement).index()) {
        elementIndex = $(initialElement).index();
        range = initialTextRange <= finalTextRange ? initialTextRange : finalTextRange;
    } else {
        elementIndex = $(finalElement).index();
        range = finalTextRange;
    }

    if (e.type == "paste") {
        pasteAction(e.originalEvent.clipboardData.getData("text").replace("paste+", ""), null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
    } else if (e.keyCode == keys.backspace) {
        backspaceAction(e.keyCode, null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
    } else if (e.keyCode == keys.delete) {
        deleteAction(e.keyCode, null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
    } else if (e.type == "keypress" || e.keyCode == keys.tab) {
        if (e.keyCode == keys.enter) {
            enterAction(null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
            insertBrOnDeepestChild(initialElement);
        } else {
            keyPressAction(e.keyCode, null, null, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange);
        }
    } else if (e.keyCode == keys.ctrlZ) {
        undoAction();
        insertOnStack = false;
    } else if (e.keyCode == keys.ctrlY) {
        redoAction();
        insertOnStack = false;
    } else if (e.keyCode == undefined) {
        insertOnStack = false;
    }


    if (insertOnStack) {
        var stackElement = {
            previousEditor: editorPreviousHtml,
            currentEditor: $(".editor").html(),
            elementIndex: elementIndex,
            range: range,
            type: "STACK"
        }
        undoStack.push(stackElement);
        redoStack = new stack();
        websocketDocs.send(JSON.stringify(stackElement));
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
    var marker = $("<span contenteditable='false' id='offset-marker-" + logged_social_profile_id + "'></span>");
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

function addMarker(id, name, html, initialHtmlRange, finalHtmlRange) {
    if (id != null) {
        var marker = "";
        if (html != "" && initialHtmlRange != finalHtmlRange) {
            var htmlArray = html.substring(initialHtmlRange, finalHtmlRange).split(/(<[^>]*>)/);
            for (var i = 0; i < htmlArray.length; i++) {
                if (!htmlArray[i].match(/(<[^>]*>)/)) {
                    htmlArray[i] = "<span contenteditable='false' socialprofileid='" + id + "' class='marker' title='" + name + "'>" + htmlArray[i] + "</span>";
                }
                marker += htmlArray[i];
            }
        } else {
            marker = "<span contenteditable='false' socialprofileid='" + id + "' class='marker' title='" + name + "'></span>";
        }
        return marker;
    } else {
        return "";
    }
}

function replaceCode(code) {
    if (code == keys.space) {
        return String.fromCharCode(keys.nonBreakingSpace);
    } else if (code == keys.tab) {
        return String.fromCharCode(keys.emphasizedSpace);
    } else if (code == keys.lowerThan) {
        return "&lt;";
    } else if (code == keys.greaterThan) {
        return "&gt;";
    } else {
        return String.fromCharCode(code);
    }
}

function insertDeleteMarkerOnTextNodes(htmlArray, senderId) {
    var markedHtml = "";
    htmlArray = $.grep(htmlArray, function (n) {
        return (n);
    });
    for (var i = 0; i < htmlArray.length; i++) {
        if (!htmlArray[i].match(/(<[^>]*>)/)) {
            htmlArray[i] = "<span contenteditable='false' class='delete-marker-" + senderId + "'>" + htmlArray[i] + "</span>";
        }
        markedHtml += htmlArray[i];
    }
    return markedHtml;
}

function deleteMarkerAndEmptyParents(element, senderId) {
    $(element).find(".delete-marker-" + senderId).each(function () {
        $(this).parentsUntil(".editor").each(function () {
            if ($(this).clone().children(".delete-marker-" + senderId).remove().end().text() == "") {
                if (!$(this).parent().hasClass("editor")) {
                    $(this).remove();
                }
            }
        });
        $(this).remove();
    });
}

function findFirstTextPositionAfterElement(html, range, insideElement, insideSpecialCharacter) {
    if (html.charCodeAt(range) == keys.lowerThan && !insideElement) {
        return findFirstTextPositionAfterElement(html, range + 1, true, false);
    } else if (html.charCodeAt(range) == keys.greaterThan && insideElement) {
        return findFirstTextPositionAfterElement(html, range + 1, false, false);
    } else {
        if (!insideElement) {
            if (html.charCodeAt(range) == keys.ampersand && !insideSpecialCharacter) {
                return findFirstTextPositionAfterElement(html, range + 1, false, true);
            } else if (html.charCodeAt(range) != keys.semicolon && insideSpecialCharacter) {
                return findFirstTextPositionAfterElement(html, range + 1, false, true);
            }
            return range;
        } else {
            return findFirstTextPositionAfterElement(html, range + 1, true, false);
        }
    }
}

function findInitialHtmlRangeForSpecialCharacters(html, range) {
    if (html.charCodeAt(range - 1) == keys.ampersand) {
        return range;
    } else {
        return findInitialHtmlRangeForSpecialCharacters(html, range - 1);
    }
}

function getBrokenElementsAfterSubstring(element, range, senderId) {
    var html = $(element).html();
    var htmlArray = html.substring(range).split(/(<[^>]*>)/);
    htmlArray = $.grep(htmlArray, function (n) {
        return (n);
    });
    var firstSubstringText = true;
    var brokenFirstElements = "";
    var markedHtml = "";
    for (var i = 0; i < htmlArray.length; i++) {
        if (!htmlArray[i].match(/(<[^>]*>)/) && firstSubstringText) {
            htmlArray[i] = "<span contenteditable='false' class='broken-marker-" + senderId + "'>" + htmlArray[i] + "</span>";
            firstSubstringText = false;
        }
        if (!firstSubstringText) {
            markedHtml += htmlArray[i];
        } else {
            brokenFirstElements += htmlArray[i];
        }
    }
    if (markedHtml == "" && htmlArray.length != 0) {
        markedHtml = "<span contenteditable='false' class='broken-marker-" + senderId + "'></span>";
        for (var i = 0; i < htmlArray.length; i++) {
            markedHtml += htmlArray[i];
        }
        $(element).html(html.substring(0, range) + markedHtml);
    } else {
        $(element).html(html.substring(0, range) + brokenFirstElements + markedHtml);
    }
    $(element).find(".broken-marker-" + senderId).parentsUntil(".editor").each(function () {
        if (!$(this).parent().hasClass("editor")) {
            $(this)[0].innerHTML = "";
            markedHtml = $(this)[0].outerHTML.substring(0, $(this)[0].outerHTML.length - (3 + $(this)[0].nodeName.length)) + markedHtml;
        }
    });
    $(element).find(".broken-marker-" + senderId).contents().unwrap();
    $(element).find(".broken-marker-" + senderId).remove();
    return markedHtml;
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