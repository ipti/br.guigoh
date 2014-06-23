var commentsPag;
$(document).ready(function() {

    displayPreview();

    $("#send-comment-btn").click(function() {
        $("#send-comment").slideToggle();
    });
    $(".question .arrow").click(function() {
        if ($(this).hasClass("open")) {
            $(this).html("&gt;");
            $(this).removeClass("open");
        } else {
            $(this).html("v");
            $(this).addClass("open");
        }
        $(this).parent().next().slideToggle();
    });
    $("#send-comment button").click(function() {
        if ($("#comments-area").val() == "") {
            showServerMsg("#send-comment", "Digite um comentario");
        } else {
            $("#send-comment .msg").css("display", "none");
            showCommentLoading("#send-comment");
            sendComment();
        }
    });

    sendRequest("GET", "/webresources/primata/getReviewObject/" + stId, fillSTFieldsCallback, {});
    var commentsListURL = "/ts/comment/list?socialTechId=" + stId;

    commentsPag = new PaginationV2(10, 5, "comments-pag", commentsListURL, createComment, true);

    //sendRequest("GET",commentsListURL+"&pageNumber=1&resultsPerPage=10&searchCriterion=",getCommentsCallback);

});

function displayPreview() {
    var mediaName = $(".media-name");
    var mediaUrl = $(".media-url");
    var right = $("#wrap-right");

    $.each(mediaName, function() {
        $(mediaName).click(function() {
            var index = $(mediaName).index(this);
            right.empty();
            var type = $(this).text().split(".")[1];
            if (type.match(/^doc$/i) || type.match(/^txt$/i)) {
                right.append($(this).text() + " indisponível para visualização.");
            }else if (type.match(/^pdf$/i)){
                right.append("<iframe height='433' width='550' src='" + mediaUrl.eq(index).text()  + "'/>");
            } else if (type.match(/^jpg$/i) || type.match(/^png$/i) || type.match(/^gif$/i)) {
                right.append("<img class='image-media' src='" + mediaUrl.eq(index).text() + "'/><span class='image-title'><p>" + $(this).text() + "</p></span>");
            } else if (type.match(/^mp3$/i) || type.match(/^wav$/i) || type.match(/^wma$/i)) {
                right.append("<span class='audio-title'><p>" + $(this).text() + "</p></span>" + "<audio src='" + mediaUrl.eq(index).text() + "' controls='preload'/>");
            } else if (type.match(/^mp4$/i) || type.match(/^avi$/i) || type.match(/^mpeg$/i)) {
                right.append("<span class='video-title'><p>" + $(this).text() + "</p></span>" + "<video src='" + mediaUrl.eq(index).text() + "' width='550' height='310' controls='preload'/>");
            }
        });
    });
    mediaName.first().click();
}
function showCommentLoading(parent) {
    $(parent).find(".wrap").css("display", "none");
    $(parent).find(".loading").css("display", "block");
}
function hideCommentLoading(parent) {
    $(parent).find(".loading").css("display", "none");
    displayTimedMsg(".msg", "Comentario enviado com sucesso");
    setTimeout("hideServerMsg('" + parent + "')", 3000);
}
function showServerMsg(parent, msg) {
    $(parent).find(".msg").text(msg);
    $(parent).find(".msg").css("display", "block");
}
function hideServerMsg(parent) {
    $(parent).find(".msg").empty();
    $(parent).find(".msg").css("display", "none");
    $(parent).css("display", "none");
    $(parent).find(".wrap").css("display", "block");
}
if ($("#comments ul").html() == "") {
    $("#send-comment").css("display", "block");
}
function fillSTFieldsCallback(response, status) {
    var reviewObject = response.info[0];
    var authors = response.authors;
    var medias = response.medias;
    if (reviewObject) {
        // var socialTech=response.socialTechData.socialTechnologyVO; 
        if (reviewObject.pictureurl) {
            $("#st-pic").attr("src", reviewObject.pictureurl);
        }
        if (medias) {
            var count = 0;
            if (medias instanceof Array) {
                for (i = 0; i < medias.length; i++) {
                    count = count + medias[i].downloadCount;
                }
                $("#downloadCount").text(count);
            } else {
                $("#downloadCount").text(medias.downloadCount);
            }
            mediasList(medias);
        }
    }
    if (authors) {
        var lang = getParam('lang');
        var nome = "Nome";
        var title = "Objeto educacional";
        var telefone = "Telefone";
        if (lang == "en") {
            var nome = "Name";
            title = "Educational Object";
            var telefone = "Phone";
        } else if (lang == "fr") {
            var nome = "Nom";
            title = "Objet Éducatif";
            var telefone = "Téléphone";
        }
        var template = "";
        if (authors instanceof Array) {
            for (i = 0; i < authors.length; i++) {
                template += "<div id='author" + authors[i].id + "'><p><strong>" + nome + ":</strong> <span id='authorName'>" + authors[i].name + "</span></p>" + "<p><strong>E-mail:</strong> <span id='authorEmail'>" + authors[i].email + "</span></p>" + "<p><strong>" + telefone + ":</strong> <span id='authorTelephone'>" + authors[i].telephone + "</span></p>" + "<p><strong>Site:</strong> <span id='authorSite'>" + authors[i].site + "</span></p>" + "</div>";
            }
        } else {
            template += "<div id='author" + authors.id + "'><p><strong>" + nome + ":</strong> <span id='authorName'>" + authors.name + "</span></p>" + "<p><strong>E-mail:</strong> <span id='authorEmail'>" + authors.email + "</span></p>" + "<p><strong>" + telefone + ":</strong> <span id='authorTelephone'>" + authors.telephone + "</span></p>" + "<p><strong>Site:</strong> <span id='authorSite'>" + authors.site + "</span></p>" + "<!--<p><strong>Instituição:</strong> <span id='institution'>" + authors.institution + "</span></p>--></div>";
        }
        $("#authors-list").append(template);
    }
    $("#reviewObjectId").attr("value", reviewObject.id);
    document.title = title + " - " + reviewObject.title;
    $("#title").text(reviewObject.title);
    $("#tagsData").text(reviewObject.tags);
    $("#authorNick").text(reviewObject.submitternick);
    $("#mainTheme").text(reviewObject.maintheme);
    $("#description").text(reviewObject.description);
    /*if(response.socialTechData.currentStatus!="PUBLISHED"){
     $("#reapplication").css("visibility","hidden");
     }*/
    if (user.login != roUser.login) {
        $(".edit-link").hide();
        $(".edit-link-theme").hide();
    } else {
        $(".edit-link").show();
        $(".edit-link-theme").show();
    }
    /*$("#institution").text(socialTech.institution);
     $("#problemFaced").text(socialTech.problemFaced);
     $("#goals").text(socialTech.goals);
     $("#environment").text(socialTech.environment);
     $("#development").text(socialTech.developmentProcess);
     $("#comment").text(socialTech.comment);
     $("#downloadCount").text(socialTech.downloadCount);
     $("#commitment").text(verifyImpact(socialTech.commitment));
     $("#identify").text(verifyImpact(socialTech.identify));
     $("#potential").text(verifyImpact(socialTech.potential));
     $("#relevance").text(verifyImpact(socialTech.relevance));
     $("#promotion").text(verifyImpact(socialTech.promotion));
     $("#dialogue").text(verifyImpact(socialTech.dialogue));
     $("#concern").text(verifyImpact(socialTech.concern));
     $("#learningLevel").text(verifyImpact(socialTech.learningLevel));
     $("#methodologies").text(verifyImpact(socialTech.methodologies));
     $("#innovationLevel").text(verifyImpact(socialTech.innovationLevel));
     $("#strengthening").text(verifyImpact(socialTech.strengthening));*/

}
function verifyImpact(impact) {
    var answer = "";
    switch (impact) {
        case"HIGH":
            answer = messages.impactAnswerHigh;
            break;
        case"MEDIUM":
            answer = messages.impactAnswerMedium;
            break;
        case"LOW":
            answer = messages.impactAnswerLow;
            break;
        default:
            answer = messages.impactAnswerNotApply;
    }
    return answer;
}

function getParam(name)
{
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.href);
    if (results == null)
        return "";
    else
        return results[1];
}
function incrementDownloadsCount(mediaId) {
    sendRequest("POST", services.mandrilRts + "socialTech/download?mediaId=" + mediaId, function(response, status) {
        console.log(response);
    });
}
function getCommentsCallback(response, status) {
    commentsPag.searchCallback(response.params.numberOfPages, response.params.pageNumber, response.params.items, status);

    if (response.params.items != "") {
        if (response.params.items.comment instanceof Array) {
            var ul = $("<ul class='list-users'></ul>");
            for (var i = 0; i < response.params.items.comment.length; i++) {
                var item = response.params.items.comment[i];
                var li = "<li><p class='name-border'><span class='date-comment'>" + formatDate(item.date) + "</span><a href='#'>" + item.authorVO.nick + "</a></p> " + " <img src=' " + services.primata + "/" + item.authorVO.pictureURL + "' alt='imagem ts'/></li>" + "<p class='comments-users'>" + item.content + "</p>";
                ul.append(li);
                $("#comments-list").append(ul);
            }
        } else {
            var ul = $("<ul class='list-users'></ul>");
            var item = response.params.items.comment;
            var li = "<li><p class='name-border'><span class='date-comment'>" + formatDate(item.date) + "</span><a href='#'>" + item.authorVO.nick + "</a></p> " + " <img src=' " + services.primata + "/" + item.authorVO.pictureURL + "' alt='imagem ts'/></li>" + "<p class='comments-users'>" + item.content + "</p>";
            ul.append(li);
            $("#comments-list").append(ul);
        }
    }
}
function createComment(items) {
    $("#comments-list").empty();
    var comments = items.comment;
    if (comments) {
        if (comments instanceof Array) {
            for (var i = 0; i < comments.length; i++) {
                $("#comments-list").append(createCommentElement(comments[i]));
            }
        } else {
            $("#comments-list").append(createCommentElement(comments));
        }
    }
}
function createCommentElement(comment) {
    var template = $("#comments .template").clone();
    template.find(".commentator").text(comment.authorVO.nick);
    template.find(".comment-date").text(formatDate(comment.date));
    template.find(".comment-hour").text(getTime(comment.date));
    if (comment.authorVO.pictureURL) {
        template.find(".commentator-photo").css("background", "url(" + services.primata + "/" + comment.authorVO.pictureURL + ")");
        template.find(".commentator-photo").css("display", "block");
    }
    template.find("p").text(comment.content);
    template.removeClass("template");
    return template;
}
function sendComment() {
    var data = {
        comment: encodeURIComponent($("#send-comment").val()),
        roId: $("#roId").val()
    };

    sendRequest("POST", services.mandrilRts + "comment/", sendCommentCallback, data);
}
function sendCommentCallback() {
    var commentsListURL = services.mandrilRts + "comment/list?socialTechId=" + stId;
    sendRequest("GET", commentsListURL + "&pageNumber=1&resultsPerPage=3&searchCriterion=", getCommentsCallback);
    $("#comments-area").val("");
    $("#comment-maxsize-count").val("300");
}
function openRecommendBox() {
    $("#recommend-box").css("display", "block");
}
function closeRecommendBox() {
    $("#email-recommend").val("");
    $("#recommend-box").css("display", "none");
}
function sendRecommend() {
    var email = $("#email-recommend").val();
    var emails = email.split(/[, ;]+/);
    var isValid = true;
    var validatedEmails = new Array();
    for (var i = 0; i < emails.length; i++) {
        if (emails[i] != "" && validateEmail(emails[i])) {
            validatedEmails.push(emails[i]);
        } else {
            if (!validateEmail(emails[i])) {
                isValid = false;
            }
        }
    }
    if (isValid) {
        var serviceUrl = services.mandrilRts + "socialTech/recommend/";
        var form = $("<form action='javascript:void(0)'></form>");
        var recommender = $("<input type='text' name='recommender' value='" + user.login + "'>");
        var socialTechName = $("<input type='text' name='socialTechName' value='" + $("#title").text() + "'>");
        var socialTechURL = $("<input type='text' name='socialTechURL' value='" + services.mandrilRts + "social-tech.do?stid=" + $("#reviewObjectId").val() + "'>");
        var lang = $("<input type='text' name='lang' value='pt'>");
        for (var j = 0; j < validatedEmails.length; j++) {
            form.append($("<input type='text' name='emails' value='" + validatedEmails[j] + "'>"));
        }
        form.append(socialTechName);
        form.append(recommender);
        form.append(socialTechURL);
        form.append(lang);
        sendRequest("POST", serviceUrl, recommendCallback, form.serialize());
    } else {
        if (email == "" || email == messages.defaultRecommendMsg) {
            displayTimedMsg("recommendMsg", messages.emailRequired);
        } else {
            if (!isValid) {
                displayTimedMsg("recommendMsg", messages.emailInvalid);
            }
        }
    }
}
function recommendCallback(response, status) {
    if (response == "email-required") {
        displayTimedMsg("recommendMsg", messages.emailRequired);
    } else {
        if (response == "email-invalid") {
            displayTimedMsg("recommendMsg", messages.emailInvalid);
        } else {
            if (response == "SUCCESS") {
                displayTimedMsg("msg", messages.sendSuccess);
                closeRecommendBox();
            } else {
                displayTimedMsg("recommendMsg", messages.serverError);
            }
        }
    }
}
function registerReapplication() {
    window.location = "Reapplication.do?stid=" + $("#reviewObjectId").val();
}
function limitText(limitField, limitCount, limitNum) {
    if (limitField.value.length > limitNum) {
        limitField.value = limitField.value.substring(0, limitNum);
    } else {
        limitCount.value = limitNum - limitField.value.length;
    }
}
function showServerMsg(parent, msg) {
    $(parent).find(".msg").text(msg);
    $(parent).find(".msg").css("display", "block");
}
function showAnswer(answerId) {
    if ($("#" + answerId).css("display") == "none") {
        $("#" + answerId).css("display", "block");
        $("#" + answerId + " .close-link").click(function() {
            $("#" + answerId).css("display", "none");
        });
    } else {
        $("#" + answerId).css("display", "none");
    }
}

function newUploads(id) {
    $("#" + id).toggle();
}

var Media = {
    mediasName: new Array(),
    mediasType: new Array(),
    mediasSize: new Array(),
    mediasFilename: new Array()
};

var Picture = {
    name: "",
    type: ""
};

function showUploadBtn(uploadBtnId) {
    $("#" + uploadBtnId).css("visibility", "visible");
}
function removeUploadedField(element) {
    var parent = $(element).parent().parent();
    if ($("#uploaded-files li").length == 1) {
        Media.mediasName = null;
        Media.mediasFilename = null;
        Media.mediasType = null;
        Media.mediasSize = null;
    } else {
        var index = null;
        for (var i = 0; i < Media.mediasName.length; i++) {
            if (Media.mediasName[i] == parent.attr("id")) {
                index = i;
                break;
            }
        }
        Media.mediasName.splice(index, 1);
        Media.mediasFilename.splice(index, 1);
        Media.mediasType.splice(index, 1);
        Media.mediasSize.splice(index, 1);
    }
    parent.remove();
}
function removeMedia(element, mediaId, stId) {
    var url = services.mandrilRts + "socialTech/removeMedia?reviewObjId=" + stId + "&mediaId=" + mediaId;
    sendRequest("POST", url, removeMediaCallback, {}, element);
}
function removeMediaCallback(response, status, element) {
    if (response == "SUCCESS") {
        var parent = $(element).parent().parent();
        parent.remove();
        displayTimedMsg("upload-msg", "Removido com sucesso");
    } else {
        if (response == "FAIL") {
            displayTimedMsg("upload-msg", "Problemas ao apagar ou você não é o autor");
        }
    }
}
function downloadMedia(mediaUrl, mediaId) {
    var a = $("#media-download-link");
    var url = services.mandrilRts + "/" + mediaUrl + "?mediaId=" + mediaId;
    a.attr("href", url);
}