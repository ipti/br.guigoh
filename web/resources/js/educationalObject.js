$(document).ready(function() {
    displayPreview();
    var left = $(".cnt-left").height();
    var right = $(".cnt-right").height();
    if (left > right){
        $(".cnt-right").css("height", left);
    }
    $(document).on("click",".recommend",function(){
        $("#recommend-box").show();
    });
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
            if (type.match(/^doc$/i) || type.match(/^docx$/i) || type.match(/^txt$/i)) {
                right.append("Prévia não suportada.");
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
function openRecommendBox() {
    $("#recommend-box").css("display", "block");
}
function closeRecommendBox() {
    $("#email-recommend").val("");
    $("#recommend-box").css("display", "none");
}