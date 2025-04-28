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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.data.model.LoginData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    /* -------- Observe state từ ViewModel -------- */
    val uiState   by loginViewModel.ui
    val textState by loginViewModel.text

    /* -------- Google Sign-In client -------- */
    val context = LocalContext.current
    val serverClientId = stringResource(R.string.google_server_client_id)
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(serverClientId)
        .build()
    val googleClient = GoogleSignIn.getClient(context, gso)

    val googleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { token ->
                val avatarUrl = account.photoUrl?.toString()
                loginViewModel.loginWithGoogle(token, avatarUrl)
            }
        } catch (_: Exception) { /* ignore */ }
    }

    /* -------- Navigate on success -------- */
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) onLoginSuccess()
    }

    /* ----------------- UI ----------------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF30393E))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))

        /* ---- Hero images + title ---- */
        Box(Modifier.height(200.dp)) {
            Image(
                painter = painterResource(R.drawable.login2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight()
                    .offset(x = 30.dp, y = 20.dp)
                    .graphicsLayer(scaleY = 1.3f)
            )
            Image(
                painter = painterResource(R.drawable.login),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = 40.dp)
                    .shadow(8.dp, RoundedCornerShape(10.dp))
            )
            Text(
                "LOGIN",
                fontSize = 72.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 55.dp, x = (-20).dp)
                    .shadow(8.dp, RoundedCornerShape(10.dp))
            )
        }

        Spacer(Modifier.height(120.dp))

        /* ---- Username ---- */
        LoginField(
            placeholder   = "Enter your user name",
            value         = textState.userName,
            onValueChange = loginViewModel::onUserNameChange
        )

        Spacer(Modifier.height(10.dp))

        /* ---- Password ---- */
        LoginField(
            placeholder   = "Enter your password",
            value         = textState.password,
            onValueChange = loginViewModel::onPasswordChange,
            isPassword    = true
        )

        Spacer(Modifier.height(20.dp))

        /* ---- Error ---- */
        uiState.errorMessage?.let {
            Text(it, color = Color.Red, fontSize = 14.sp)
            Spacer(Modifier.height(10.dp))
        }

        /* ---- LogIn button ---- */
        Button(
            onClick = {
                val user = LoginData(textState.userName, textState.password)
                loginViewModel.login(user)
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43))
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Text("LogIn", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(Modifier.height(10.dp))

        /* ---- Sign-up ---- */
        TextButton(onClick = onSignupClick) {
            Text(
                "You don’t have an account? Register now",
                fontSize = 12.sp,
                color = Color.White
            )
        }

        Spacer(Modifier.height(20.dp))
        Text("Or sign in with Google", color = Color.Gray)
        Spacer(Modifier.height(10.dp))

        /* ---- Google button ---- */
        Image(
            painter = painterResource(R.drawable.logogoogle),
            contentDescription = "Google Login",
            modifier = Modifier
                .size(48.dp)
                .clickable { googleLauncher.launch(googleClient.signInIntent) }
        )
    }
}

/* ---------- Reusable TextField ---------- */
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
        placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
            imeAction    = ImeAction.Done
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(300.dp)
            .height(55.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}
