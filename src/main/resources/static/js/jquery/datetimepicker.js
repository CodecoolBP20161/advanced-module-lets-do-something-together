$(function () {
    $('#datetimepicker1').datetimepicker({
        minDate: moment().add(1, 'h')
    });
});