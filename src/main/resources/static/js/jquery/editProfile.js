function updateInterests(list) {
    for (var i = 0; i < list.length; i++) {
        var id = "#" + list[i];
        $(id).prop('checked', true);
        $("input[type='submit']").removeAttr("disabled");
    }
}