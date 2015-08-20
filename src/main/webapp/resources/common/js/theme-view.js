var index = 2;

$(document).ready(function() {
    if ($(".discussion_topics_box").height() >= $(".publication_objects_box").height()) {
        $(".discussion_topics_box").css("border-right", "1px solid #B2B6B1");
    } else {
        $(".publication_objects_box").css("border-left", "1px solid #B2B6B1");
    }

    $(".educational_object").last().css({"margin-bottom": "0", "border-bottom": "0"});
    if ($("#pagination-container").length == 0) {
        $(".discussion_topic").last().css({"margin-bottom": "0", "border-bottom": "0"});
    }

    var items = $(".discussion_topic");
    var perPage = 5;

    items.slice(perPage).hide();
    if ($(items).length) {
        $('#pagination').twbsPagination({
            totalPages: Math.ceil(items.length / perPage),
            visiblePages: 5,
            first: "«",
            prev: "<",
            next: ">",
            last: "»",
            onPageClick: function(event, page) {
                var showFrom = perPage * (page - 1);
                var showTo = showFrom + perPage;

                items.hide().slice(showFrom, showTo).show();
                $(page).attr('class', 'active');

            }
        });
    }
});


