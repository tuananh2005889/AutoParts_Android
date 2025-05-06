package com.example.frontend.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.AddReviewRequest
import com.example.frontend.data.model.ReviewData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repo: ReviewRepository
) : ViewModel() {

    private val _reviews   = MutableStateFlow<List<ReviewData>>(emptyList())
    val reviews: StateFlow<List<ReviewData>> = _reviews

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun load(productId: Long) = viewModelScope.launch {
        _error.value = null               // clear previous error
        _loading.value = true             // show loading spinner
        try {
            when (val r = repo.getReviews(productId)) {
                is ApiResponse.Success -> _reviews.value = r.data
                is ApiResponse.Error   -> _error.value   = r.message
                else                   -> { /* no-op */ }
            }
        } catch (e: Exception) {
            Log.e("ReviewViewModel", "load reviews failed", e)
            _error.value = e.localizedMessage ?: "Unknown error"
        } finally {
            _loading.value = false        // hide loading spinner
        }
    }

    fun post(req: AddReviewRequest, onDone: () -> Unit) = viewModelScope.launch {
        _error.value   = null               // clear previous error
        _loading.value = true               // show loading spinner
        try {
            when (val r = repo.postReview(req)) {
                is ApiResponse.Success -> {
                    load(req.productId)     // reload list on success
                    onDone()                // callback để đóng dialog
                }
                is ApiResponse.Error -> {
                    _error.value = r.message
                    Log.e("ReviewViewModel", "postReview failed: ${r.message}")
                }
                else -> { /* no-op */ }
            }
        } catch (e: Exception) {
            Log.e("ReviewViewModel", "exception during postReview", e)
            _error.value = e.localizedMessage ?: "Unknown error"
        } finally {
            _loading.value = false          // hide loading spinner
        }
    }
}
