$(document).ready(function () {
    $("#loginEmail").focus();
});

if ($("#formError").text().length > 0) {
    $("#formError").slideToggle("slow").removeAttr("hidden");
    setTimeout(function () {
        $("#formError:visible").fadeOut();
    }, 3000);

}

$("#loginEmail").blur();
