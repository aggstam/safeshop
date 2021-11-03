$(document).ready(function(){

    $('.searchInput').keyup(function(){
        let value = $(this).val().toLowerCase();
        $('.col-md-4').each(function() {
            if ($(this).find('p.card-text').text().toLowerCase().includes(value)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });

});