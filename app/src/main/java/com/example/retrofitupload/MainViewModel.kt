package com.example.retrofitupload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val repository: UploadRepository = UploadRepository()
) : ViewModel() {
    fun uploadFile(file: File) {
        viewModelScope.launch {
            repository.uploadFile(file)
        }
    }
}