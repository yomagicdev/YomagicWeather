package com.yomagic.weather.di

import com.yomagic.weather.data.location.LocationTrackerImpl
import com.yomagic.weather.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

        @Binds
        abstract fun bindLocationTracker(locationTrackerImpl: LocationTrackerImpl): LocationTracker
}