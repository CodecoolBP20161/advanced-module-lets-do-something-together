$(document).ready(function () {
    // $('.search-input').attr('type','text');
});



var active = function() {
    $('.btn-search').removeAttr("disabled");
    if ($('.radio-date').is(':checked')) {
        $('#search-input').attr('type','date')
    } else {
        $('#search-input').removeAttr('type');
    }
};