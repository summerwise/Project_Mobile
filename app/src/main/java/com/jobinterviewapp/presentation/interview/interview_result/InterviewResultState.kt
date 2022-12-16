package com.jobinterviewapp.presentation.interview.interview_result

import com.jobinterviewapp.core.util.UiText
import com.jobinterviewapp.data.remote.dto.TaskDto

data class InterviewResultState(
    val error: UiText? = null,
    val wrongAnswers: List<TaskDto> = emptyList(),
    val rightAnswersCount: Int? = null,
    val wrongAnswersCount: Int? = null,
    val rightAnswersPercentage: Float? = null,
    val answersCount: Int? = null,
    val professionId: Int? = null,
)