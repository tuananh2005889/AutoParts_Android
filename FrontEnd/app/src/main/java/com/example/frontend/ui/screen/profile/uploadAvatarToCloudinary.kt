package com.example.frontend.ui.screen.profile

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
suspend fun uploadAvatarToCloudinary(file: File): String? {
    val cloudName = "dj72n3iih"
    val uploadPreset = "ml_default"

    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "file", file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        .addFormDataPart("upload_preset", uploadPreset)
        .build()

    val request = Request.Builder()
        .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
        .post(requestBody)
        .build()

    val client = OkHttpClient()
    val response = client.newCall(request).execute()

    if (!response.isSuccessful) {
        Log.e("Cloudinary", "Upload failed: ${response.code}")
        return null
    }

    val bodyString = response.body?.string()
    if (bodyString.isNullOrEmpty()) {
        Log.e("Cloudinary", "Empty body on upload")
        return null
    }

    val json = JSONObject(bodyString)
    val url = json.getString("secure_url")
    Log.d("Cloudinary", "Uploaded URL = $url")
    return url
}


fun getRealPathFromUri(context: Context, uri: Uri): String {
    val returnCursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor?.moveToFirst()
    val name = nameIndex?.let { returnCursor.getString(it) } ?: "temp_file"
    returnCursor?.close()

    val file = File(context.cacheDir, name)

    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        val buffers = ByteArray(1024)
        var read: Int

        while (inputStream?.read(buffers).also { read = it ?: -1 } != -1) {
            outputStream.write(buffers, 0, read)
        }

        inputStream?.close()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return file.path
}
