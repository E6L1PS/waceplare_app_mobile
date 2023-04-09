package com.itacademy.search.domain.usecase

import com.itacademy.common.Resource
import com.itacademy.common.model.Ad
import com.itacademy.search.domain.repository.AdsRepository
import javax.inject.Inject

class GetInfoAboutAdUseCase @Inject constructor(
    private val adsRepository: AdsRepository
) {
    suspend fun getAd(id: Long): Resource<Ad?> {
        return adsRepository.getAd(id)
    }
}
