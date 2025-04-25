package com.example.frontend.ui.screen.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.data.model.LoginData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ui.screen.login.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val loginState by loginViewModel.loginState
    val textState by loginViewModel.loginTextFieldState

    val context = LocalContext.current
    val serverClientId = stringResource(R.string.google_server_client_id)
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(serverClientId)
        .build()
    val googleClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { loginViewModel.loginWithGoogle(it) }
        } catch (_: Exception) {
            // Google Sign-In error handled silently
        }
    }

    LaunchedEffect(loginState.loginSuccess) {
        if (loginState.loginSuccess) onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF30393E))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painterResource(id = R.drawable.login2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight()
                    .offset(x = 30.dp, y = 20.dp)
                    .graphicsLayer(scaleY = 1.3f)
            )
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = 40.dp)
                    .shadow(8.dp, RoundedCornerShape(10.dp))
            )
            Text(
                text = "LOGIN",
                fontSize = 72.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 55.dp, x = (-20).dp)
                    .shadow(8.dp, RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.height(120.dp))

        LoginField(
            placeholder = "Enter your user name",
            value = textState.userName.orEmpty(),
            onValueChange = loginViewModel::onUserNameChange
        )

        Spacer(modifier = Modifier.height(10.dp))

        LoginField(
            placeholder = "Enter your password",
            value = textState.password.orEmpty(),
            onValueChange = loginViewModel::onPasswordChange,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        loginState.errorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = {
                val user = LoginData(textState.userName.orEmpty(), textState.password.orEmpty())
                loginViewModel.login(user)
            },
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43)),
        ) {
            Text(text = "LogIn", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onSignupClick) {
            Text("You don’t have an account? Register now", fontSize = 12.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Or sign in with Google", color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.logogoogle),
            contentDescription = "Google Login",
            modifier = Modifier
                .size(48.dp)
                .clickable { launcher.launch(googleClient.signInIntent) }
        )
    }
}

@Composable
fun LoginField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 14.sp) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(300.dp)
            .height(55.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}
