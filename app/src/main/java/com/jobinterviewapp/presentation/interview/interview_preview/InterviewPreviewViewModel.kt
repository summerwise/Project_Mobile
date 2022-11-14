package com.jobinterviewapp.presentation.interview.interview_preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobinterviewapp.core.util.Resource
import com.jobinterviewapp.domain.use_case.interviewConfiguration.GetInterviewPreviewUseCase
import com.jobinterviewapp.presentation.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewPreviewViewModel @Inject constructor(
    private val getInterviewPreviewUseCase: GetInterviewPreviewUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(InterviewPreviewState())
    val state = _state.asStateFlow()
    private val professionId: Int? = savedStateHandle.get<Int>(Constants.PARAM_PROFESSIONS_OF_TECHNOLOGY)

    init {
        loadInterviewPreview()
    }

    fun loadInterviewPreview() {
        viewModelScope.launch {
            professionId?.let {
                getInterviewPreviewUseCase(professionId).collectLatest { result ->
                    when(result) {
                        is Resource.Success -> {
                            _state.update { it.copy(
                                previewName = result.data.name,
                                questionsCount = result.data.count
                            ) }
                        }
                        is Resource.Error -> {
                        }
                    }
                }
            }
        }
    }
}