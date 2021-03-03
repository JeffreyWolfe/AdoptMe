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
package com.example.androiddevchallenge.model.data

import android.content.Context
import com.example.androiddevchallenge.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver

class DatabaseHelper(context: Context) {

    private val sqlDriver = AndroidSqliteDriver(Database.Schema, context, "pet.db")
    private val db: Database = Database(sqlDriver)
    val petQueries = db.petQueries

    internal fun clear() {
        sqlDriver.close()
    }

    suspend fun insertPets(pets: List<Pet>) {
        db.transaction {
            pets.forEach { pet ->
                db.petQueries.insertFullPetObject(pet)
            }
        }
    }

    suspend fun insertPet(pet: Pet) {
        db.transaction {
            db.petQueries.insertFullPetObject(pet)
        }
    }
}
