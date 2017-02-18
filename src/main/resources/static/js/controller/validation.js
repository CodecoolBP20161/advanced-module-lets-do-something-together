$(document).ready(function () {
    buttonActive(false);
    $('.email-error-message').hide();
    $('.success-message').hide();
});



var passwordMatch = function () {
    var pword = $('.password').val();
    var pwordAgain = $('.password-again').val();
    if (pword != pwordAgain) {
        buttonActive(false);
        $('.password-error-message').text("Password does not match").fadeIn();
    } else {
        buttonActive(true);
        $(".password-error-message").fadeOut();
    }
};


function buttonActive(status) {
    if (status == false) {
        $(".registration-button").attr("disabled", "true");
    } else {
        $(".registration-button").removeAttr("disabled");
    }
}

function hideError() {
    $('.email-error-message').hide();
}

function validateEmailFormat() {
    var $email = $("#email");
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@(|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (!re.test($email.val())) {
        $(".email-format-message").text("Incorrect email format").fadeIn();
    } else {
        $(".email-format-message").fadeOut();
    }
}