package com.example.frontend.ui.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.data.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ProfileScreen(
    userName: String,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userState by profileViewModel.userDataState.collectAsState()
    val user = userState
    val errorState by profileViewModel.updateAvatarError.collectAsState()
    val error = errorState
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(userName) {
        profileViewModel.loadUser(userName)
    }

    error?.let { msg ->
        LaunchedEffect(msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            profileViewModel.clearAvatarError()
        }
    }

    if (user == null) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    ProfileContent(
        user = user,
        onUpload = { file ->
            scope.launch {
                val url = withContext(Dispatchers.IO) {
                    uploadAvatarToCloudinary(file)
                }
                if (url != null) {
                    profileViewModel.updateAvatarUrl(user.userName, url)
                } else {
                    Toast.makeText(context, "Upload thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onLogout = onLogout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    user: UserData,
    onUpload: (File) -> Unit,
    onLogout: () -> Unit
) {
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            avatarUri = it
            File(getRealPathFromUri(context, it)).let(onUpload)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontSize = 20.sp) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF30393E),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF4F4F4))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar picker
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                when {
                    avatarUri != null -> Image(
                        painter = rememberAsyncImagePainter(avatarUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    user.avatarUrl != null -> Image(
                        painter = rememberAsyncImagePainter(user.avatarUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    else -> Text("Chọn avatar", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Show info dialog button
            Button(onClick = { showDialog = true }) {
                Text("Xem thông tin", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Policies always visible
            Text(text = "Chính sách bảo mật", modifier = Modifier.fillMaxWidth(), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Điều khoản dịch vụ", modifier = Modifier.fillMaxWidth(), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Cài đặt ứng dụng", modifier = Modifier.fillMaxWidth(), fontSize = 16.sp)



            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Đăng xuất", color = Color.White)
            }
        }

        // Info dialog
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = user.fullName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Email: ${user.gmail}")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Phone: ${user.phone ?: "Chưa có"}")
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
