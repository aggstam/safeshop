$(document).ready(function(){
    $('.redirectButton').click(function(){
        location.href = $(this).attr('link');
    });
});