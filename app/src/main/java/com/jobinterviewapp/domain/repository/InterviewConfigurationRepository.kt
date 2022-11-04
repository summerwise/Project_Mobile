package com.jobinterviewapp.domain.repository

import com.jobinterviewapp.data.remote.dto.FieldOfActivityDto
import com.weatherapp.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface InterviewConfigurationRepository {

    fun getFieldOfActivity(): Flow<Resource<FieldOfActivityDto>>
}