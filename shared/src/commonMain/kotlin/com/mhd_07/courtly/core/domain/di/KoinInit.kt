package com.mhd_07.courtly.core.domain.di

//import com.mhd_07.courtly.feature_auth.domain.di.authModule
import com.mhd_07.courtly.feature_match.domain.di.matchModule
import com.mhd_07.courtly.feature_sign.domain.di.signModule
import com.mhd_07.courtly.feature_match_setup.domain.di.matchSetupModule
import com.mhd_07.courtly.feature_nav.domain.di.navModule
import com.mhd_07.courtly.feature_profile.domain.di.profilePreviewModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    includes(config)
    modules(coreModule)
    modules(matchModule)
//    modules(authModule)
    modules(navModule)
    modules(signModule)
    modules(matchSetupModule)
    modules(profilePreviewModule)
}