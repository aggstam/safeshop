let form = '#productForm';

$(document).ready(function(){

    $('.addButton').click(function(){
        $('#modalTitle').text('Add Product');
        $('#submitButton').text('Add');
        $('#productForm').attr('action', '/products' + '?' + $('#_csrfToken').attr('name') + '=' + $('#_csrfToken').attr('value'));
        $('#name').val('');
        $('#description').val('');
        $('#quantity').val('');
        $('#price').val('');
        $('#file').val('');
        $('#productCaptchaCode').val('');
    });

    $('.editButton').click(function(){
        $('#modalTitle').text('Edit Product');
        $('#submitButton').text('Edit');
        $('#productForm').attr('action', '/products/' + $(this).parents('.card-body').find('.card-text:eq(0)').html().split('</b>')[1] + '?' + $('#_csrfToken').attr('name') + '=' + $('#_csrfToken').attr('value'));
        $('#name').val($(this).parents('.card-body').find('.card-text:eq(1)').html().split('</b>')[1]);
        $('#description').val($(this).parents('.card-body').find('.card-text:eq(2)').html().split('</b>')[1]);
        $('#quantity').val($(this).parents('.card-body').find('.card-text:eq(3)').html().split('</b>')[1]);
        $('#price').val($(this).parents('.card-body').find('.card-text:eq(4)').html().split('</b>')[1].replace(/\D/g,''));
        $('#file').val('');
        $('#productCaptchaCode').val('');
    });

    $('.deleteButton').click(function(){
        $('#deleteForm').attr('action', '/products/delete/' + $(this).parents('.card-body').find('.card-text:eq(0)').html().split('</b>')[1]);
        $('#deleteProductCaptchaCode').val('');
    });

    $('#productForm').submit(function(event) {
        if ($('#productFormCaptchaResponse').val() === '') {
            event.preventDefault();
            form = '#productForm';
            grecaptcha.execute();
        }
    });

    $('#deleteForm').submit(function(event) {
        if ($('#deleteProductFormCaptchaResponse').val() === '') {
            event.preventDefault();
            form = '#deleteForm';
            grecaptcha.execute();
        }
    });

});

onCompleted = function() {
    $('#productFormCaptchaResponse').val(grecaptcha.getResponse());
    $('#deleteProductFormCaptchaResponse').val(grecaptcha.getResponse());
    $(form).submit();
}