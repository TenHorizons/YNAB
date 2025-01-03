package com.ynab.ui.login

import com.ynab.domain.BasicAuthUseCase
import com.ynab.domain.FakeBasicAuthUseCase
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private var basicAuthUseCase: BasicAuthUseCase = FakeBasicAuthUseCase()

    //https://stackoverflow.com/questions/63544502/hilt-viewmodel-injection-into-instrumentation-tests
    @BindValue val vm = LoginViewModel(basicAuthUseCase)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun loginViewModel_UsernameExistAndPasswordCorrect_LoginSuccess() {
        //Fails because of dispatcher used by view model. To learn how to do this properly at later date.
        //https://stackoverflow.com/questions/71807957/how-test-a-viewmodel-function-that-launch-a-viewmodelscope-coroutine-android-ko
        //https://developer.android.com/kotlin/coroutines/test
        vm.onUsernameChange("Alice")
        vm.onPasswordChange("Alice123")
        var isLoginSuccess = false
        vm.onLoginClick { isLoginSuccess = true }
        assert(isLoginSuccess)
        assertEquals(
            LoginState(
                username = "Alice",
                password = "Alice123",
                isLoginInProgress = true,
                isLoginError = false,
                errorMessage = ""
            ),
            vm.uiState.value
        )
    }
}