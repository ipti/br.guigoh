/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery(function($){
    $('#checkall').bind('click',function(e){
        $(".kselItems").attr("checked", true);
        e.preventDefault();
    });
    $('#uncheckall').bind('click',function(e){
        $(".kselItems").attr("checked", false);
        e.preventDefault();
    });  
});

