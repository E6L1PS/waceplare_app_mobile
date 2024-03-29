package com.itacademy.data.repository

import com.itacademy.common.Resource
import com.itacademy.common.model.Ad
import com.itacademy.data.AdsDataRepository
import com.itacademy.data.api.SearchApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AdsDataRepositoryImpl @Inject constructor(
    private val api: SearchApi
) : AdsDataRepository {

    override suspend fun getAds(title: String?): Flow<Resource<List<Ad>?>> = flow {
        emit(Resource.loading(null))

        val response = if (title == null) {
            api.getAllAds()
        } else {
            api.getAllAds(title)
        }

        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(response.message(), null))
        }

    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Error occurred", null))
    }.flowOn(Dispatchers.IO)

    override suspend fun getAd(id: Long): Resource<Ad?> {
        val response = api.getAd(id)

        return if (response.isSuccessful) {
            Resource.success(response.body())
        } else {
            Resource.error(response.message())
        }
    }


}