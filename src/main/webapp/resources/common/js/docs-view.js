$(document).ready(function () {
    $(".menu-icon-four").parent().addClass("active");
    
    $('.doc-container').on('DOMMouseScroll mousewheel', preventScrolling);
});