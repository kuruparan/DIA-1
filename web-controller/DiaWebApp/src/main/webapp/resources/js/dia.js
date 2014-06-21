function validatePass() {
    if (document.signUpForm.password.value == document.signUpForm.passwordRe.value) {
        return true;
    }
    return false;
}