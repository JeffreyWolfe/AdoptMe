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
package com.example.androiddevchallenge.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.data.Pet
import com.example.androiddevchallenge.model.data.loremIpsum
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun PetDetailScreen(petViewModel: PetViewModel, petId: Long, popBack: () -> Unit) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val pet = petViewModel.getPet(petId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { "${pet?.id}" },
                navigationIcon = {
                    IconButton(onClick = { popBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            pet?.let {
                PetDetail(pet = it)
            }
        }

    )
}

@Composable
fun PetImage(modifier: Modifier = Modifier, img: String, name: String) {
    CoilImage(
        data = img,
        contentDescription = name,
        fadeIn = true,
        contentScale = ContentScale.Fit,
        modifier = modifier,
        error = {
            Image(painterResource(id = R.drawable.pawprint), "Paw")
        },
        loading = {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        },
    )
}

@Composable
fun PetDetail(pet: Pet) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PetImage(
                Modifier
                    .fillMaxWidth(),
                pet.image,
                pet.name
            )
            Text(
                text = pet.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1,
            )
            Text(
                text = "${pet.age} Year old ${when (pet.sex) {
                    "M" -> "Male"
                    "F" -> "Female"
                    else -> ""
                }
                }"
            )
            Text(text = loremIpsum)
        }
    }
}
