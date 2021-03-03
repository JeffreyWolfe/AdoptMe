/*
 * Copyright 2021-2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.di

import com.example.androiddevchallenge.model.PetRepository
import com.example.androiddevchallenge.model.PetRepositoryImpl
import com.example.androiddevchallenge.model.data.DatabaseHelper
import com.example.androiddevchallenge.ui.PetViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule, coreModule)
    }

val appModule = module {
    viewModel { PetViewModel(get()) }
}
val coreModule = module {
    single<PetRepository> { PetRepositoryImpl(get()) }
    single { DatabaseHelper(get()) }
}
