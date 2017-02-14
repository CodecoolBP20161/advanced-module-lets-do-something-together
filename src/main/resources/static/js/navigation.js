$(document).ready(function(){
    setBindings();
});

function setBindings() {
    $("nav a").click(function (event) {
        event.preventDefault();
        var sectionId = event.currentTarget.id + "Section";

        $('html body').animate({
            scrollTop: $("#" + sectionId).offset().top
        }, 1000)
    })
}