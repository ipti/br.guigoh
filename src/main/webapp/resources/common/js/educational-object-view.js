$(document).ready(function () {
    $(".menu-icon-one").parent().addClass("active");
    
    displayPreview();
    var mediaName = $(".media-name");
    var mediaRow = $(".media-table-row");
    var left = $(".cnt-left").height();
    var right = $(".cnt-right").height();
    if (left > right) {
        $(".cnt-right").css("height", left);
    }
    $(document).on("click", ".recommend", function () {
        $("#recommend-box").show();
    });

    $.each(mediaRow, function () {
        var index = $(mediaRow).index(this);
        if ($(mediaName.eq(index)).text() == ".") {
            $(mediaRow.eq(index)).hide();
        }
    });
});

function displayPreview() {
    var mediaName = $(".media-name");
    var mediaUrl = $(".media-url");
    var mediaRow = $(".media-table-row");
    var right = $("#wrap-right");
    var locale = $("#localeAcronym").val();
    $.each(mediaRow, function () {
        $(mediaName).click(function () {
            var index = $(mediaName).index(this);
            $(mediaRow).css("background-color", "#DCDFD4");
            $(mediaRow.eq(index)).css("background-color", "#B1D1FF");
            right.empty();
            var type = $(this).text().split(".")[1];
            if (type.match(/^pdf$/i)) {
                right.append("<iframe height='433' width='552' src='" + mediaUrl.eq(index).text() + "'/>");
            } else if (type.match(/^jpeg$/i) || type.match(/^jpg$/i) || type.match(/^png$/i) || type.match(/^gif$/i) || type.match(/^bmp$/i)) {
                right.append("<img class='image-media' src='" + mediaUrl.eq(index).text() + "'/>");
            } else if (type.match(/^mp3$/i) || type.match(/^wav$/i) || type.match(/^wma$/i)) {
                right.append("<span class='audio-title'><p>" + $(this).text() + "</p></span>" + "<audio src='" + mediaUrl.eq(index).text() + "' controls='preload'/>");
            } else if (type.match(/^mp4$/i) || type.match(/^avi$/i) || type.match(/^mpeg$/i)) {
                right.append("<iframe height='310' width='552' src='" + mediaUrl.eq(index).text() + "' frameborder='0'/>");
            } else if (type.match(/^ogg$/i) || type.match(/^wmv$/i) || type.match(/^webM$/i) || type.match(/^swf$/i) || type.match(/^doc$/i) || type.match(/^docx$/i) || type.match(/^txt$/i)) {
                if (locale == 'ptBR') {
                    var htmlOutput = "A prévia deste arquivo não é suportada. Clique ";
                    htmlOutput += "<a href='" + mediaUrl.eq(index).text() + "' style='color:#0000FF' download>aqui</a>";
                    htmlOutput += " para baixá-lo.";
                    right.html(htmlOutput);
                } else if (locale == 'enUS'){
                    var htmlOutput = "This file's preview is not supported. Click ";
                    htmlOutput += "<a href='" + mediaUrl.eq(index).text() + "' style='color:#0000FF' download>here</a>";
                    htmlOutput += " to download it.";
                    right.html(htmlOutput);
                } else if (locale == 'frFR'){
                    var htmlOutput = "La prévisualisation de ce fichier n'est pas supporté. Cliquez ";
                    htmlOutput += "<a href='" + mediaUrl.eq(index).text() + "' style='color:#0000FF' download>ici</a>";
                    htmlOutput += "  pour télécharger.";
                    right.html(htmlOutput);
                }
            }
        });
    });
    mediaName.first().click();
}