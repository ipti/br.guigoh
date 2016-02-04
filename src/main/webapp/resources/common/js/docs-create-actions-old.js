/*
 * known todo's
 * 1: fix collaborator cursor marker disappearing when undo/redo
 * 2: fix user caret position when undo/redo (including highlight if word changed is larger than 1 letter)
 * 3: Remove unique redo/undo stack to every collaborators and create multiple stacks (each stack for every collaborator)
 * 4: change only editor diffs in undo/redo, and not all $(".editor").html()
 */

/**
 * For each one of these action methods, the general strategy adopted was:
 *      - For every action, we have 5 posibilities:
 *          - The initial element is before final element;
 *          - The initial element and final element are the same, but the user highlighted  more than one letter, from left to right;
 *          - The initial element and final element are the same, and the range is the same (i.e: a simple mouse click);
 *          - The initial element and final element are the same, but the user highlighted  more than one letter, from right to left;
 *          - The initial element is after final element;
 * Mostly of these methods are 'if' conditioned in that structure.
 * @param {String} code                 : Code sent by a collaborator. Can be a simple keycode number or a text/html.
 * @param {String} senderId             : Sender unique id.
 * @param {String} senderName           : Sender name. Passed via Json to avoid a sql search in websocket's callback.
 * @param {String} initialElement       : DOM element of the initial index (where the sender action started).
 * @param {String} finalElement         : DOM element of the final index (where the sender action finished).
 * @param {String} initialHtmlRange     : Collaborator's initial caret offset (counting html structure, not only text).
 * @param {String} finalHtmlRange       : Collaborator's final caret offset (counting html structure, not only text).
 * @param {String} initialTextRange     : Collaborator's initial caret offset (counting only text).
 * @param {String} finalTextRange       : Collaborator's final caret offset (counting only text).
 * @returns void
 */

function keyPressAction(code, senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    code = replaceCode(code);
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).append($(finalElement).html());
        var html = $(initialElement).html();
        var htmlArray = html.substring(initialHtmlRange, initialHtml.length + finalHtmlRange).split(/(<[^>]*>)/);
        var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
        $(initialElement).html(html.substring(0, initialHtmlRange) + code + addMarker(senderId, senderName, "") + markedHtml + html.substring(initialHtml.length + finalHtmlRange));
        deleteMarkerAndEmptyParents(initialElement, senderId);
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        setLocalCaretPosition(initialElement, initialTextRange + 1, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange || initialHtmlRange == finalHtmlRange) {
            var htmlArray = initialHtml.substring(initialHtmlRange, finalHtmlRange).split(/(<[^>]*>)/);
            var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + code + addMarker(senderId, senderName, "") + markedHtml + initialHtml.substring(finalHtmlRange));
            deleteMarkerAndEmptyParents(initialElement, senderId);
            setLocalCaretPosition(initialElement, initialTextRange + 1, senderId);
        } else {
            var htmlArray = initialHtml.substring(finalHtmlRange, initialHtmlRange).split(/(<[^>]*>)/);
            var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + code + addMarker(senderId, senderName, "") + markedHtml + initialHtml.substring(initialHtmlRange));
            deleteMarkerAndEmptyParents(initialElement, senderId);
            setLocalCaretPosition(initialElement, finalTextRange + 1, senderId);
        }
    } else {
        $(finalElement).append($(initialElement).html());
        var html = $(finalElement).html();
        var htmlArray = html.substring(finalHtmlRange, finalHtml.length + initialHtmlRange).split(/(<[^>]*>)/);
        var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
        $(finalElement).html(html.substring(0, finalHtmlRange) + code + addMarker(senderId, senderName, "") + markedHtml + html.substring(finalHtml.length + initialHtmlRange));
        deleteMarkerAndEmptyParents(finalElement, senderId);
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
        setLocalCaretPosition(finalElement, finalTextRange + 1, senderId);
    }
}

function mouseClickAction(code, senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
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
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
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
    var initialText = $(initialElement).text();
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).append($(finalElement).html());
        var html = $(initialElement).html();
        var htmlArray = html.substring(initialHtmlRange, initialHtml.length + finalHtmlRange).split(/(<[^>]*>)/);
        var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
        $(initialElement).html(html.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + html.substring(initialHtml.length + finalHtmlRange));
        deleteMarkerAndEmptyParents(initialElement, senderId);
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        setLocalCaretPosition(initialElement, initialTextRange, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange) {
            var htmlArray = initialHtml.substring(initialHtmlRange, finalHtmlRange).split(/(<[^>]*>)/);
            var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + initialHtml.substring(finalHtmlRange));
            deleteMarkerAndEmptyParents(initialElement, senderId);
            setLocalCaretPosition(initialElement, initialTextRange, senderId);
        } else if (initialHtmlRange == finalHtmlRange) {
            if (initialTextRange == 0 && $(initialElement).index() !== 0) {
                var prevRange = $(initialElement).prev().text().length;
                if ($(initialElement).prev().html() == "") {
                    $(initialElement).prev().html(addMarker(senderId, senderName, "") + initialHtml);
                } else {
                    $(initialElement).prev().html($(initialElement).prev().html() + addMarker(senderId, senderName, "") + initialHtml);
                }
                var prevElement = initialElement;
                do {
                    prevElement = prevElement.previousSibling;
                } while (prevElement && prevElement.nodeType !== 1);
                setLocalCaretPosition(prevElement, prevRange, senderId);
                $(initialElement).remove();
            } else {
                if (initialHtml.charCodeAt(initialHtmlRange - 1) == keys.semicolon && initialHtml.substring(initialHtmlRange - 1, finalHtmlRange) != initialText.substring(initialTextRange - 1, finalTextRange)) {
                    initialHtmlRange = findInitialHtmlRangeForSpecialCharacters(initialHtml, initialHtmlRange);
                }
                var htmlArray = initialHtml.substring(initialHtmlRange - 1, finalHtmlRange).split(/(<[^>]*>)/);
                var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
                $(initialElement).html(initialHtml.substring(0, initialHtmlRange - 1) + addMarker(senderId, senderName, "") + markedHtml + initialHtml.substring(finalHtmlRange));
                deleteMarkerAndEmptyParents(initialElement, senderId);
                setLocalCaretPosition(initialElement, initialTextRange - 1, senderId);
            }
        } else {
            var htmlArray = initialHtml.substring(finalHtmlRange, initialHtmlRange).split(/(<[^>]*>)/);
            var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + initialHtml.substring(initialHtmlRange));
            deleteMarkerAndEmptyParents(initialElement, senderId);
            setLocalCaretPosition(initialElement, finalTextRange, senderId);
        }
    } else {
        $(finalElement).append($(initialElement).html());
        var html = $(finalElement).html();
        var htmlArray = html.substring(finalHtmlRange, finalHtml.length + initialHtmlRange).split(/(<[^>]*>)/);
        var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
        $(finalElement).html(html.substring(0, finalHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + html.substring(finalHtml.length + initialHtmlRange));
        deleteMarkerAndEmptyParents(finalElement, senderId);
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
        $(initialElement).append($(finalElement).html());
        var html = $(initialElement).html();
        var htmlArray = html.substring(initialHtmlRange, initialHtml.length + finalHtmlRange).split(/(<[^>]*>)/);
        var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
        $(initialElement).html(html.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + html.substring(initialHtml.length + finalHtmlRange));
        deleteMarkerAndEmptyParents(initialElement, senderId);
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        setLocalCaretPosition(initialElement, initialTextRange, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange) {
            var htmlArray = initialHtml.substring(initialHtmlRange, finalHtmlRange).split(/(<[^>]*>)/);
            var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + initialHtml.substring(finalHtmlRange));
            deleteMarkerAndEmptyParents(initialElement, senderId);
            setLocalCaretPosition(initialElement, initialTextRange, senderId);
        } else if (initialHtmlRange == finalHtmlRange) {
            if (initialTextRange == initialText.length && !$(initialElement).is(':last-child')) {
                $(initialElement).html(initialHtml + addMarker(senderId, senderName, "") + $(initialElement).next().html());
                $(initialElement).next().remove();
                setLocalCaretPosition(initialElement, initialText.length, senderId);
            } else {
                finalHtmlRange = findFirstTextPositionAfterElement(initialHtml, finalHtmlRange, false, false);
                var htmlArray = initialHtml.substring(initialHtmlRange, finalHtmlRange + 1).split(/(<[^>]*>)/);
                var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
                $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + initialHtml.substring(finalHtmlRange + 1));
                deleteMarkerAndEmptyParents(initialElement, senderId);
                setLocalCaretPosition(initialElement, initialTextRange, senderId);
            }
        } else {
            var htmlArray = initialHtml.substring(finalHtmlRange, initialHtmlRange).split(/(<[^>]*>)/);
            var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + initialHtml.substring(initialHtmlRange));
            deleteMarkerAndEmptyParents(initialElement, senderId);
            setLocalCaretPosition(initialElement, finalTextRange, senderId);
        }
    } else {
        $(finalElement).append($(initialElement).html());
        var html = $(finalElement).html();
        var htmlArray = html.substring(finalHtmlRange, finalHtml.length + initialHtmlRange).split(/(<[^>]*>)/);
        var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
        $(finalElement).html(html.substring(0, finalHtmlRange) + addMarker(senderId, senderName, "") + markedHtml + html.substring(finalHtml.length + initialHtmlRange));
        deleteMarkerAndEmptyParents(finalElement, senderId);
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
    var html;
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).html(initialHtml.substring(0, initialHtmlRange));
        $(initialElement).find('*').each(function () {
            if ($(this).text() == "") {
                $(this).remove();
            }
        });
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        html = getBrokenElementsAfterSubstring(finalElement, finalHtmlRange, senderId);
        $(finalElement).html(addMarker(senderId, senderName, "") + html);
        $(finalElement).find(".broken-marker-" + senderId).contents().unwrap();
        $(finalElement).find(".broken-marker-" + senderId).remove();
        setLocalCaretPosition(finalElement, 0, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange || initialHtmlRange == finalHtmlRange) {
            html = getBrokenElementsAfterSubstring(initialElement, finalHtmlRange, senderId);
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange));
        } else {
            html = getBrokenElementsAfterSubstring(initialElement, initialHtmlRange, senderId);
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange));
        }
        $(initialElement).find('*').each(function () {
            if ($(this).text() == "") {
                $(this).remove();
            }
        });
        $(initialElement).clone().html(addMarker(senderId, senderName, "") + html).insertAfter(initialElement);
        $(initialElement).next().find(".broken-marker-" + senderId).contents().unwrap();
        $(initialElement).next().find(".broken-marker-" + senderId).remove();
        setLocalCaretPosition(initialElement.nextSibling, 0, senderId);
    } else {
        $(finalElement).html(finalHtml.substring(0, finalHtmlRange));
        $(finalElement).find('*').each(function () {
            if ($(this).text() == "") {
                $(this).remove();
            }
        });
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        html = getBrokenElementsAfterSubstring(initialElement, initialHtmlRange, senderId);
        $(initialElement).html(addMarker(senderId, senderName, "") + html);
        $(initialElement).find(".broken-marker-" + senderId).contents().unwrap();
        $(initialElement).find(".broken-marker-" + senderId).remove();
        setLocalCaretPosition(initialElement, 0, senderId);
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
    if (code == keys.ctrlHome || code == "page" + keys.pageUp) {
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

function pasteAction(code, senderId, senderName, initialElement, finalElement, initialHtmlRange, finalHtmlRange, initialTextRange, finalTextRange) {
    code = code.replace(/'/g, '"').replace(/\\n/g, "\n");
    var initialHtml = $(initialElement).html();
    var finalHtml = $(finalElement).html();
    if ($(initialElement).index() < $(finalElement).index()) {
        $(initialElement).append($(finalElement).html());
        var html = $(initialElement).html();
        var htmlArray = html.substring(initialHtmlRange, initialHtml.length + finalHtmlRange).split(/(<[^>]*>)/);
        var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
        $(initialElement).html(html.substring(0, initialHtmlRange) + code + addMarker(senderId, senderName, "") + markedHtml + html.substring(initialHtml.length + finalHtmlRange));
        deleteMarkerAndEmptyParents(initialElement, senderId);
        $(initialElement).nextUntil($(finalElement)).each(function () {
            $(this).remove();
        });
        $(finalElement).remove();
        setLocalCaretPosition(initialElement, initialTextRange + code.length, senderId);
    } else if ($(initialElement).index() == $(finalElement).index()) {
        if (initialHtmlRange < finalHtmlRange || initialHtmlRange == finalHtmlRange) {
            $(initialElement).html(initialHtml.substring(0, initialHtmlRange) + code + addMarker(senderId, senderName, "") + initialHtml.substring(finalHtmlRange));
            setLocalCaretPosition(initialElement, initialTextRange + code.length, senderId);
        } else {
            $(initialElement).html(initialHtml.substring(0, finalHtmlRange) + code + addMarker(senderId, senderName, "") + initialHtml.substring(initialHtmlRange));
            setLocalCaretPosition(initialElement, finalTextRange + code.length, senderId);
        }
    } else {
        $(finalElement).append($(initialElement).html());
        var html = $(finalElement).html();
        var htmlArray = html.substring(finalHtmlRange, finalHtml.length + initialHtmlRange).split(/(<[^>]*>)/);
        var markedHtml = insertDeleteMarkerOnTextNodes(htmlArray, senderId);
        $(finalElement).html(html.substring(0, finalHtmlRange) + code + addMarker(senderId, senderName, "") + markedHtml + html.substring(finalHtml.length + initialHtmlRange));
        deleteMarkerAndEmptyParents(finalElement, senderId);
        $(finalElement).nextUntil($(initialElement)).each(function () {
            $(this).remove();
        });
        $(initialElement).remove();
        setLocalCaretPosition(finalElement, finalTextRange + code.length, senderId);
    }
}

function undoAction() {
    var action = undoStack.read();
    if (action != undefined) {
        $(".editor").html(action.previousEditor);
        redoStack.push(action);
        undoStack.pop();
        setLocalCaretPosition($(".editor").children().get(action.elementIndex), action.range, null);
    }
}

function redoAction() {
    var action = redoStack.read();
    if (action != undefined) {
        $(".editor").html(action.currentEditor);
        undoStack.push(action);
        redoStack.pop();
        setLocalCaretPosition($(".editor").children().get(action.elementIndex), action.range, null);
    }
}