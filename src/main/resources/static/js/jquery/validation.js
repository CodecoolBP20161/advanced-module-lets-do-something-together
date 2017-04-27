$(document).ready(function () {
    buttonActive(false);
    $('.email-error-message').hide();
    $('.success-message').hide();
    $("#email").focus();
});


var passwordMatch = function () {
    var pword = $('.password').val();
    var pwordAgain = $('.password-again').val();
    if (pword != pwordAgain) {
        buttonActive(false);
        $('.password-error-message').text("Password does not match").addClass("alert alert-danger alert-dismissable").fadeIn();
        $("#password_again").focus();
        // addError($("#pwd2Div"));
    } else {
        buttonActive(true);
        $(".password-error-message").fadeOut();
        // removeError($("#pwd2Div"));
    }
};


function buttonActive(status) {
    if (status == false) {
        $(".registration-button").attr("disabled", "true");
    } else {
        $(".registration-button").removeAttr("disabled");
    }
}
//
//
// function hideError() {
//     $('.email-error-message').hide();
// }
//
//
// function validateEmailFormat() {
//     var $email = $("#email");
//     var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@(|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
//     if ($email.val().length > 0 && !re.test($email.val())) {
//         $(".email-format-message").text("Incorrect email format").addClass("alert alert-danger alert-dismissable").fadeIn();
//         $("#email").focus();
//         // addError($("#emailDiv"));
//     } else {
//         $(".email-format-message").fadeOut();
//         // removeError($("#emailDiv"));
//     }
// }


function onSuccess() {
    $('.success-message').text("You have successfully registered").addClass("alert alert-success alert-dismissable").fadeIn();
    removeError($("#emailDiv"));
    if ($(".success-message").css("display") === "block") {
        $(".success-message").append('<br/><span>We are redirecting you to the main page.</span>')
        setTimeout(function () {
            window.location.href = "/";
        }, 4000);
    }




// function addError($element) {
//     $element.addClass("has-error");
//     $element.addClass("has-feedback");
// }
//
//
// function removeError($element) {
//     $element.removeClass("has-error");
//     $element.removeClass("has-feedback");
// }


// function validatePasswordFormat() {
//     var $password = $(".password");
//     var re = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}/g;
//     if ($password.val().length > 0 && !re.test($password.val())) {
//         $(".password-validate-message").text("Minimum length is 6 characters.\nPassword must contain at least one " +
//             "uppercase, lowercase and number").addClass("alert alert-danger alert-dismissable").css("white-space", "pre-line").fadeIn();
//         buttonActive(false);
//         $("#password").focus();
//         // addError($("#pwdDiv"));
//     } else {
//         $(".password-validate-message").fadeOut();
//         buttonActive(true);
//         // removeError($("#pwdDiv"));
//     }
}


$("#email").blur();

$("#password_again").blur();

$("#password").blur();