package com.soictnative.ardecor.di

import com.soictnative.ardecor.data.source.local.LocalDataSource
import com.soictnative.ardecor.data.source.local.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    @ViewModelScoped
    abstract fun bindDataSource(
        dataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource
}