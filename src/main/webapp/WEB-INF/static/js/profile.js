$(document).ready(function(){
    $('#profileForm').submit(function(event) {
        if ($('#captchaResponse').val() === '') {
            event.preventDefault();
            grecaptcha.execute();
        }
    });
});

onCompleted = function() {
    $('#captchaResponse').val(grecaptcha.getResponse());
    $('#profileForm').submit();
}