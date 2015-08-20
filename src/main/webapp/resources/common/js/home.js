var index = 0;

$(window).scroll(function (){
        if ($(window).scrollTop() + $(window).height() === $(document).height()) {
            $('.load-more-objects').click();
            $('.load-more-activities').click();
        }
});

//function isElementVisible(elementToBeChecked)
//{
//    var TopView = $(window).scrollTop();
//    var BotView = TopView + $(window).height();
//    var TopElement = $(elementToBeChecked).offset().top;
//    var BotElement = TopElement + $(elementToBeChecked).height();
//    return ((BotElement <= BotView) && (TopElement >= TopView));
//}
