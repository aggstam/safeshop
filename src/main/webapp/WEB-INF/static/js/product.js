$(document).ready(function(){
    $('#orderForm').submit(function(event) {
        if ($('#captchaResponse').val() === '') {
            event.preventDefault();
            grecaptcha.execute();
        }
    });
});

onCompleted = function() {
    $('#captchaResponse').val(grecaptcha.getResponse());
    $('#orderForm').submit();
}



