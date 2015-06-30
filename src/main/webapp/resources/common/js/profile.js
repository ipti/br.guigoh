/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(function($){
    $('.profile_menu a').bind('click',function(e){
        $('.tab').hide();
        $('#tab_'+$(this).attr('tab')).show();
        e.preventDefault();
    });
   
});

