package com.example.retrofitupload

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadRepository(private val api: UploadApi = RetrofitHelper.uploadApi) {
    suspend fun uploadFile(file: File) {
        api.uploadFile(
            MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
        )
    }
}