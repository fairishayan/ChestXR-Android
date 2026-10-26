package com.example.chestxr.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chestxr.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

enum class Diagnosis {
    NORMAL,
    PNEUMONIA
}

data class PredictionResult(
    val diagnosis: Diagnosis,
    val confidencePercentage: Int
)

sealed interface UiState {
    data object Idle : UiState
    data class ImageSelected(val imageUri: Uri) : UiState
    data class Loading(val imageUri: Uri) : UiState
    data class Success(val imageUri: Uri, val result: PredictionResult) : UiState
    data class Error(val imageUri: Uri?, val message: String) : UiState
}

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onImageSelected(uri: Uri?) {
        if (uri != null) {
            _uiState.value = UiState.ImageSelected(uri)
        }
    }

    fun analyzeXray(context: Context) {
        val currentUri = when (val state = _uiState.value) {
            is UiState.ImageSelected -> state.imageUri
            is UiState.Success -> state.imageUri
            is UiState.Error -> state.imageUri
            else -> null
        } ?: return

        viewModelScope.launch {
            _uiState.value = UiState.Loading(currentUri)

            try {
                val imagePart = prepareImagePart(context, currentUri)
                if (imagePart == null) {
                    _uiState.value = UiState.Error(currentUri, "Failed to process selected image file.")
                    return@launch
                }

                // Real network call to FastAPI backend
                val response = RetrofitClient.api.predict(imagePart)

                val diagnosis = if (response.label.equals("PNEUMONIA", ignoreCase = true)) {
                    Diagnosis.PNEUMONIA
                } else {
                    Diagnosis.NORMAL
                }

                val confidencePct = if (response.confidence <= 1.0) {
                    (response.confidence * 100).toInt()
                } else {
                    response.confidence.toInt()
                }

                _uiState.value = UiState.Success(
                    imageUri = currentUri,
                    result = PredictionResult(
                        diagnosis = diagnosis,
                        confidencePercentage = confidencePct
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
                val errorMessage = e.localizedMessage ?: "Unable to connect to server at 192.168.1.42:8000"
                _uiState.value = UiState.Error(currentUri, errorMessage)
            }
        }
    }

    private fun prepareImagePart(context: Context, uri: Uri): MultipartBody.Part? {
        return try {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val bytes = inputStream.use { it.readBytes() }
            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", "xray_image.jpg", requestBody)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun reset() {
        _uiState.value = UiState.Idle
    }
}