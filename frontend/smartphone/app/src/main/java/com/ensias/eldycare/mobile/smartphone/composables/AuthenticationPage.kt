package com.ensias.eldycare.mobile.smartphone.composables

enum class AuthenticationType {
    LOGIN, SIGNUP
}
/*
@Composable
fun AuthenticationPage() {
    var isLoading = false
    var authType: AuthenticationType = AuthenticationType.SIGNUP
    var signupStep by remember { mutableStateOf(1) }
    var signupData by remember { mutableStateOf(
        SignupData(
            name = "",
            email = "",
            password = "",
            userType = UserType.CLOSE_RELATIVE
        )
    )
    }
    var loginData by remember { mutableStateOf(
        LoginData(
            email = "",
            password = ""
        )
    )
    }
    when(authType) {
        AuthenticationType.LOGIN -> LoginPage(loginData, { loginData = it })
        AuthenticationType.SIGNUP ->
            when(signupStep) {
                1 -> SignUpPage(signupData, signupStep, { signupData = it }, { signupStep = it })
                2 -> UserTypeChoicePage(signupData, signupStep, { signupData = it }, { signupStep = it })
                3 -> LoadingAccountCreationPage(signupData)
            }
    }
}
*/