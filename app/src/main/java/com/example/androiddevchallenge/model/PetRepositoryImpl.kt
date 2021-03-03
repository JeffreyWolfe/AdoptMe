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
package com.example.androiddevchallenge.model

import com.example.androiddevchallenge.model.data.DatabaseHelper
import com.example.androiddevchallenge.model.data.Pet
import com.example.androiddevchallenge.model.data.pets
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PetRepositoryImpl(val databaseHelper: DatabaseHelper) : PetRepository {

    private val petQueries = databaseHelper.petQueries
    private val coroutineScope: CoroutineScope = MainScope()

    init {
        coroutineScope.launch {
            addPets(pets)
        }
    }

    override fun getAllPets(): Flow<List<Pet>> {
        return petQueries.selectAll().asFlow().mapToList()
    }

    override suspend fun getPet(petID: Int): Pet {
        TODO("Not yet implemented")
    }

    override suspend fun addPet(pet: Pet) {
        databaseHelper.insertPet(pet)
    }

    override suspend fun addPets(pets: List<Pet>) {
        databaseHelper.insertPets(pets)
    }

    override suspend fun removePet(id: Int) {
        TODO("Not yet implemented")
    }
}
