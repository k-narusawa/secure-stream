package com.knarusawa.api.application.service.changeProfile

import com.knarusawa.common.domain.profile.FamilyName
import com.knarusawa.common.domain.profile.GivenName
import com.knarusawa.common.domain.profile.Nickname
import com.knarusawa.common.domain.profile.ProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChangeProfileService(
    private val profileRepository: ProfileRepository
) {
    @Transactional
    fun exec(inputData: ChangeProfileInputData) {
        val profile = profileRepository.findByUserId(inputData.userId)

        profile.change(
            familyName = FamilyName.of(inputData.familyName),
            givenName = GivenName.of(inputData.givenName),
            nickname = inputData.nickname?.let { Nickname.of(it) },
            picture = inputData.picture
        )

        profileRepository.save(profile)
    }
}