$(function () {
    $('#datetimepicker1').datetimepicker({
        format:'DD/MM/YYYY HH:mm a',
        minDate: moment().add(1, 'h')
    });
});