package com.example.frontend.ui.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.R
import com.example.frontend.ViewModel.ProfileViewModel
import com.example.frontend.data.dto.UpdateUserInfoRequest
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
    val avatarError by profileViewModel.updateAvatarError.collectAsState()
    val updateError by profileViewModel.updateUserError.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(userName) {
        profileViewModel.loadUser(userName)
    }

    avatarError?.let { msg ->
        LaunchedEffect(msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            profileViewModel.clearAvatarError()
        }
    }
    updateError?.let { msg ->
        LaunchedEffect(msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            profileViewModel.clearUpdateUserError()
        }
    }

    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
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
                if (url != null) profileViewModel.updateAvatarUrl(user.userName, url)
                else Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
            }
        },
        onLogout = onLogout,
        profileViewModel = profileViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    user: UserData,
    onUpload: (File) -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Uri chọn avatar cục bộ
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            avatarUri = it
            val file = File(getRealPathFromUri(context, it))
            onUpload(file)
        }
    }

    // Dialog state
    var showDialog by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    // Các ô nhập để chỉnh sửa, khởi tạo từ user
    var editableFullName by remember { mutableStateOf(user.fullName) }
    var editableEmail    by remember { mutableStateOf(user.gmail) }
    var editablePhone    by remember { mutableStateOf(user.phone ?: "") }
    var editableAddress  by remember { mutableStateOf(user.address ?: "") }

    // Mỗi khi user mới được load (sau cập nhật), tự động cập nhật lại các ô nhập
    LaunchedEffect(user) {
        editableFullName = user.fullName
        editableEmail    = user.gmail
        editablePhone    = user.phone ?: ""
        editableAddress  = user.address ?: ""
        avatarUri = null // nếu muốn reset preview avatar cục bộ sau cập nhật
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontSize = 20.sp) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_right),
                            contentDescription = "Logout",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
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
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                when {
                    avatarUri != null ->
                        Image(
                            painter = rememberAsyncImagePainter(avatarUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    user.avatarUrl != null ->
                        Image(
                            painter = rememberAsyncImagePainter(user.avatarUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    else ->
                        Text("Choose avatar", color = Color.White)
                }
            }

            Spacer(Modifier.height(16.dp))
            // Hiển thị tên từ user (sẽ tự cập nhật khi StateFlow thay đổi)
            Text(
                text = user.fullName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE5D2D))
            ) {
                Text("View / Edit Information", color = Color.White)
            }

            Spacer(Modifier.height(24.dp))
            OrderSection(onHistoryClick = { /* TODO */ })
        }

        // Dialog chỉnh sửa
        if (showDialog) {
            Dialog(onDismissRequest = {
                showDialog = false
                isEditing = false
            }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (!isEditing) {
                            // Chế độ xem thông tin
                            Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Email: ${user.gmail}")
                            Spacer(Modifier.height(4.dp))
                            Text("Phone: ${user.phone ?: "Not set"}")
                            Spacer(Modifier.height(4.dp))
                            Text("Address: ${user.address ?: "Not set"}")
                            Spacer(Modifier.height(16.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { isEditing = true }) { Text("Edit") }
                                Spacer(Modifier.width(8.dp))
                                TextButton(onClick = { showDialog = false }) { Text("Close") }
                            }
                        } else {
                            // Chế độ edit: dùng các biến editable*
                            OutlinedTextField(
                                value = editableFullName,
                                onValueChange = { editableFullName = it },
                                label = { Text("Full Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = editableEmail,
                                onValueChange = { editableEmail = it },
                                label = { Text("Email") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = editablePhone,
                                onValueChange = { editablePhone = it },
                                label = { Text("Phone") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = editableAddress,
                                onValueChange = { editableAddress = it },
                                label = { Text("Address") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = {
                                    // Gọi update lên ViewModel
                                    profileViewModel.updateUserInfo(
                                        UpdateUserInfoRequest(
                                            userName = user.userName,
                                            fullName = editableFullName,
                                            gmail    = editableEmail,
                                            address  = editableAddress,
                                            phone    = editablePhone
                                        )
                                    )
                                    showDialog = false
                                    isEditing = false
                                }) {
                                    Text("Save")
                                }
                                Spacer(Modifier.width(8.dp))
                                TextButton(onClick = {
                                    // Hủy chỉnh sửa, vô hiệu hoá edit mode
                                    isEditing = false
                                }) {
                                    Text("Cancel")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun OrderSection(onHistoryClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Purchase order", fontWeight = FontWeight.Bold)
                TextButton(onClick = onHistoryClick) {
                    Text("View purchase history >")
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                item { OrderItem(R.drawable.ic_hourglass, "Waiting for confirmation") }
                item { OrderItem(R.drawable.ic_inventory, "Waiting for goods") }
                item { OrderItem(R.drawable.ic_local_shipping, "Waiting for delivery") }
                item { OrderItem(R.drawable.ic_star, "Evaluate") }
            }
        }
    }
}

@Composable
private fun OrderItem(iconRes: Int, label: String) {
    Column(
        Modifier
            .width(64.dp)
            .clickable { /* TODO */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(iconRes), contentDescription = null, modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
    }
}