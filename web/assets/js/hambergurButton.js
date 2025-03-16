
$(document).ready(function () {
    $('#sidebarToggle').on('click', function () {
        $('#sidebar').toggleClass('active');
        if ($('#sidebar').hasClass('active')) {
            $('.sidebar').css('display', 'block');
        } else {
            $('.sidebar').css('display', 'none');
        }
    });
});
