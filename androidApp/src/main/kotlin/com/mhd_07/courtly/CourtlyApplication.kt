package com.mhd_07.courtly

import android.app.Application
import com.mhd_07.courtly.core.domain.di.initKoin
import org.koin.android.ext.koin.androidContext


class CourtlyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@CourtlyApplication) }
    }
}