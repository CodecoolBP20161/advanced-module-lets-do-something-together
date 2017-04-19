function updateInterests(list) {
    if (list.length > 0) {
        $("input[type='submit']").removeAttr("disabled");
        for (var i = 0; i < list.length; i++) {
            var id = createSelector(list[i]);
            $(id).prop('checked', true);
        }
    }
}

function checkInterests() {
    var interests = [];

    $('#profileForm *').filter(':checkbox').each(function () {
        var id = this.id;
        if ($(createSelector(id)).prop("checked")) {
            interests.push(id)
        }
    });
    return interests;
}

var createSelector = function (id) {
    return "#" + id;
};