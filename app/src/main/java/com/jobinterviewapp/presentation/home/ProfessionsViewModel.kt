package com.jobinterviewapp.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobinterviewapp.domain.use_case.interviewConfiguration.GetProfessionsOfTechnologyUseCase
import com.jobinterviewapp.presentation.Constants
import com.weatherapp.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfessionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProfessionsOfTechnologyUseCase: GetProfessionsOfTechnologyUseCase
): ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    private val technologyId: Int? = savedStateHandle.get<Int>(Constants.PARAM_TECHNOLOGIES_OF_DIRECTION_ID)

    fun loadDirectionsOfField() {
        viewModelScope.launch {
            technologyId?.let {
                getProfessionsOfTechnologyUseCase(technologyId).collectLatest { result ->
                    when(result) {
                        is Resource.Success -> {
                            _state.update { it.copy(
                                fieldsOfActivity = result.data
                            ) }
                        }
                        is Resource.Error -> {
                        }
                    }
                }
            }
        }
    }

    init {
        loadDirectionsOfField()
    }
}