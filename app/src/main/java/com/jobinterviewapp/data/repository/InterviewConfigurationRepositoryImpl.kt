package com.jobinterviewapp.data.repository

import com.google.gson.Gson
import com.jobinterviewapp.R
import com.jobinterviewapp.data.remote.InterviewServiceApi
import com.jobinterviewapp.data.remote.dto.ErrorDto
import com.jobinterviewapp.data.remote.dto.FieldOfActivityDto
import com.jobinterviewapp.data.remote.dto.InterviewPreviewDto
import com.jobinterviewapp.domain.repository.InterviewConfigurationRepository
import com.jobinterviewapp.core.util.Resource
import com.jobinterviewapp.core.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class InterviewConfigurationRepositoryImpl @Inject constructor(
    private val api: InterviewServiceApi,
): InterviewConfigurationRepository {

    override fun getFieldsOfActivity() = flow {
        emit(safeApiCall { api.getFieldOfActivity() })
    }.flowOn(Dispatchers.IO)

    override fun getDirectionsOfField(fieldId: Int) = flow {
        emit(safeApiCall { api.getDirectionsOfField(fieldId) })
    }.flowOn(Dispatchers.IO)

    override fun getTechnologiesOfDirection(directionId: Int) = flow {
        emit(safeApiCall { api.getTechnologiesOfDirection(directionId) })
    }.flowOn(Dispatchers.IO)

    override fun getProfessionsOfTechnology(technologyId: Int): Flow<Resource<List<FieldOfActivityDto>>> = flow {
        emit(safeApiCall { api.getProfessionsOfTechnology(technologyId) })
    }.flowOn(Dispatchers.IO)

    override fun getInterviewPreview(professionId: Int): Flow<Resource<InterviewPreviewDto>> = flow {
        emit(safeApiCall { api.getInterviewPreview(professionId) })
    }.flowOn(Dispatchers.IO)

    private inline fun <T> safeApiCall(apiCall: () -> T): Resource<T> {
        try {
            val data = apiCall()
            return Resource.Success(data = data)
        }
        catch(e: HttpException) {
            val errorMessage = if(e.localizedMessage.isNullOrEmpty()) {
                UiText.StringResource(R.string.unknown_exception)
            }
            else {
                val errorBody = e.response()?.errorBody()
                if (errorBody == null) {
                    UiText.DynamicString(e.localizedMessage!!)
                }
                else {
                    val errorMessage = Gson().fromJson(errorBody.charStream(), ErrorDto::class.java).message
                    UiText.DynamicString(errorMessage)
                }
            }
            return Resource.Error(errorMessage)
        }
        catch(e: IOException) {
            return Resource.Error(UiText.StringResource(R.string.io_exception))
        }
    }
}