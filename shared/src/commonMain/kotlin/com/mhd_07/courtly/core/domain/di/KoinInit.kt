package com.mhd_07.courtly.core.domain.di

//import com.mhd_07.courtly.feature_auth.domain.di.authModule
import com.mhd_07.courtly.feature_sign.domain.di.signModule
import com.mhd_07.courtly.feature_match_record.domain.di.matchRecordModule
import com.mhd_07.courtly.feature_nav.domain.di.navModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    includes(config)
    modules(coreModule)
    modules(matchRecordModule)
//    modules(authModule)
    modules(navModule)
    modules(signModule)
}