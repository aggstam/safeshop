let form = '#userForm';

$(document).ready(function(){

    $('.addButton').click(function(){
        $('#modalTitle').text('Add User');
        $('#submitButton').text('Add');
        $('#userForm').attr('action', '/users');
        $('#email').val('');
        $('#name').val('');
        $('#address').val('');
        $('#phone').val('');
        $('#seller').prop('checked', false);
        $('#admin').prop('checked', false);
    });

    $('.editButton').click(function(){
        $('#modalTitle').text('Edit User');
        $('#submitButton').text('Edit');
        $('#userForm').attr('action', '/users/' + $(this).parents('tr').find('td:eq(0)').html());
        $('#email').val($(this).parents('tr').find('td:eq(1)').html());
        $('#name').val($(this).parents('tr').find('td:eq(2)').html());
        $('#address').val($(this).parents('tr').find('td:eq(3)').html());
        $('#phone').val($(this).parents('tr').find('td:eq(4)').html());
        if ($(this).parents('tr').find('td:eq(5)').html().includes('check')) {
            $('#seller').prop('checked', true);
        } else {
            $('#seller').prop('checked', false);
        }
        if ($(this).parents('tr').find('td:eq(6)').html().includes('check')) {
            $('#admin').prop('checked', true);
        } else {
            $('#admin').prop('checked', false);
        }
    });

    $('.deleteButton').click(function(){
        $('#deleteForm').attr('action', '/users/delete/' + $(this).parents('tr').find('td:eq(0)').html());
    });

    $('.searchInput').keyup(function(){
        let value = $(this).val().toLowerCase();
        $('#usersTable tr').filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $('#userForm').submit(function(event) {
        if ($('#userFormCaptchaResponse').val() === '') {
            event.preventDefault();
            form = '#userForm';
            grecaptcha.execute();
        }
    });

    $('#deleteForm').submit(function(event) {
        if ($('#deleteUserFormCaptchaResponse').val() === '') {
            event.preventDefault();
            form = '#deleteForm';
            grecaptcha.execute();
        }
    });

});

onCompleted = function() {
    $('#userFormCaptchaResponse').val(grecaptcha.getResponse());
    $('#deleteUserFormCaptchaResponse').val(grecaptcha.getResponse());
    $(form).submit();
}