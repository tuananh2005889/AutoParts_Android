package com.example.frontend.ui.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
    val user by profileViewModel.userDataState.collectAsState()
    val avatarError by profileViewModel.updateAvatarError.collectAsState()
    val updateError by profileViewModel.updateUserError.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(userName) {
        profileViewModel.loadUser(userName)
    }
    LaunchedEffect(avatarError) {
        avatarError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            profileViewModel.clearAvatarError()
        }
    }
    LaunchedEffect(updateError) {
        updateError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            profileViewModel.clearUpdateUserError()
        }
    }

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        ProfileContent(
            user = user!!,
            onUpload = { file ->
                scope.launch {
                    val url = withContext(Dispatchers.IO) {
                        uploadAvatarToCloudinary(file)
                    }
                    if (url != null) {
                        profileViewModel.updateAvatarUrl(user!!.userName, url)
                    } else {
                        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onLogout = onLogout,
            profileViewModel = profileViewModel
        )
    }
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
    val scrollState = rememberScrollState()

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

    var showDialog by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf(user.fullName) }
    var email    by remember { mutableStateOf(user.gmail) }
    var phone    by remember { mutableStateOf(user.phone.orEmpty()) }
    var address  by remember { mutableStateOf(user.address.orEmpty()) }

    LaunchedEffect(user) {
        fullName = user.fullName
        email    = user.gmail
        phone    = user.phone.orEmpty()
        address  = user.address.orEmpty()
        avatarUri = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Profile",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF15D43),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color(0xFFF4F4F4))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar + camera overlay
            Box(
                Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .pointerInput(Unit) {
                        detectTapGestures { launcher.launch("image/*") }
                    },
                contentAlignment = Alignment.BottomEnd
            ) {
                when {
                    avatarUri != null ->
                        Image(
                            painter = rememberAsyncImagePainter(avatarUri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    user.avatarUrl != null ->
                        Image(
                            painter = rememberAsyncImagePainter(user.avatarUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                }
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clickable { launcher.launch("image/*") }
                ) {
                    Icon(
                        Icons.Filled.CameraAlt,
                        contentDescription = "Change avatar",
                        tint = Color.Black,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                fullName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // User Information Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "User Information",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit info")
                        }
                    }
                    Divider(Modifier.padding(vertical = 8.dp))

                    // Email
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Email:")
                            }
                            append(" ${user.gmail}")
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(4.dp))

                    // Phone
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Phone:")
                            }
                            append(" ${user.phone.orEmpty()}")
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(4.dp))

                    // Address
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Address:")
                            }
                            append(" ${user.address.orEmpty()}")
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Purchase Orders Card with Delivered
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Purchase Orders", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        item { OrderStatusItem(R.drawable.ic_hourglass, "Awaiting\nConfirmation") }
                        item { OrderStatusItem(R.drawable.ic_inventory, "Awaiting\nShipment") }
                        item { OrderStatusItem(R.drawable.ic_local_shipping, "In\nTransit") }
                        item { OrderStatusItem(R.drawable.check, "Delivered") }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Support & Policies Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column {
                    Text(
                        "Support & Policies",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(Modifier.height(12.dp))

                    ListItem(
                        modifier = Modifier.clickable { /* TODO */ },
                        leadingContent = {
                            Icon(
                                painter = painterResource(R.drawable.insurance),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        headlineContent = { Text("Privacy Policy") }
                    )
                    Spacer(Modifier.height(12.dp))

                    ListItem(
                        modifier = Modifier.clickable { /* TODO */ },
                        leadingContent = {
                            Icon(
                                painter = painterResource(R.drawable.document),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        headlineContent = { Text("Terms of Service") }
                    )
                    Spacer(Modifier.height(12.dp))

                    ListItem(
                        modifier = Modifier.clickable { /* TODO */ },
                        leadingContent = {
                            Icon(
                                Icons.Filled.ReportProblem,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        headlineContent = { Text("Report a Problem") },
                        trailingContent = {
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    )
                }
            }
        }

        // Edit / View Dialog
        if (showDialog) {
            Dialog(onDismissRequest = {
                showDialog = false
                isEditing = false
            }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = if (isEditing) "Edit Information" else "Your Information",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(Modifier.height(12.dp))

                        if (!isEditing) {
                            InfoRow("Full Name", fullName)
                            InfoRow("Email", email)
                            InfoRow("Phone", phone)
                            InfoRow("Address", address)
                        } else {
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Full Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Phone") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("Address") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            if (!isEditing) {
                                TextButton(onClick = { isEditing = true }) {
                                    Text("Edit")
                                }
                            } else {
                                TextButton(onClick = {
                                    profileViewModel.updateUserInfo(
                                        UpdateUserInfoRequest(
                                            userName = user.userName,
                                            fullName = fullName,
                                            gmail    = email,
                                            address  = address,
                                            phone    = phone
                                        )
                                    )
                                    isEditing = false
                                    showDialog = false
                                }) {
                                    Text("Save")
                                }
                                Spacer(Modifier.width(8.dp))
                                TextButton(onClick = {
                                    isEditing = false
                                    showDialog = false
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
private fun OrderStatusItem(iconRes: Int, label: String) {
    Column(
        Modifier
            .width(72.dp)
            .clickable { /* TODO */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
