package com.itacademy.personal_ads.domain.usecase

import com.itacademy.common.Resource
import com.itacademy.common.model.Ad
import com.itacademy.personal_ads.PersonalAdsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonalAds @Inject constructor(
    private val personalAdsRepository: PersonalAdsRepository
) {
    suspend fun getAds(): Flow<Resource<List<Ad>?>> = personalAdsRepository.getAds()
}