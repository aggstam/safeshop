let form = '#completeOrderForm';

$(document).ready(function(){

    $('.completeOrderButton').click(function(){
        $('#completeOrderForm').attr('action', '/orders/complete/' + $(this).parents('tr').find('td:eq(0)').html());
        $('#completeOrderCaptchaCode').val('');
    });

    $('.cancelOrderButton').click(function(){
        $('#cancelOrderForm').attr('action', '/orders/cancel/' + $(this).parents('tr').find('td:eq(0)').html());
        $('#cancelOrderCaptchaCode').val('');
    });

    $('.searchInput').keyup(function(){
        let value = $(this).val().toLowerCase();
        $('#customerOrdersTable tr').filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $('#completeOrderForm').submit(function(event) {
        if ($('#completeOrderCaptchaResponse').val() === '') {
            event.preventDefault();
            form = '#captchaResponse';
            grecaptcha.execute();
        }
    });

    $('#cancelOrderForm').submit(function(event) {
        if ($('#cancelOrderCaptchaResponse').val() === '') {
            event.preventDefault();
            form = '#cancelOrderForm';
            grecaptcha.execute();
        }
    });

});

onCompleted = function() {
    $('#completeOrderCaptchaResponse').val(grecaptcha.getResponse());
    $('#cancelOrderCaptchaResponse').val(grecaptcha.getResponse());
    $(form).submit();
}