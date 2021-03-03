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

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.transform.CircleCropTransformation
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.data.Pet
import com.example.androiddevchallenge.ui.util.MaskTransformation
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun HomeScreen(petViewModel: PetViewModel, petSelected: (pet: Pet) -> Unit) {

    val petState = petViewModel.getAllPets().collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adopt Me!", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
            )
        },
        content = { innerPadding ->
            LazyColumn(contentPadding = innerPadding, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(
                    items = petState.value
                ) { pet ->
                    PetCard(pet, petSelected)
                }
            }
        }

    )
}

@Composable
fun PetSex(modifier: Modifier = Modifier, pet: Pet) {
    Icon(
        when (pet.sex) {
            "M" -> Icons.Filled.Male
            "F" -> Icons.Filled.Female
            else -> Icons.Filled.Error
        },
        "Male",
        modifier = modifier.scale(2f)
    )
}

@Composable
fun PetAge(modifier: Modifier = Modifier, pet: Pet) {
    Text(
        text = "${pet.age} Years",
        modifier = modifier,
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun PetName(modifier: Modifier = Modifier, pet: Pet) {
    Text(
        text = pet.name,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h2,
        modifier = modifier
            .padding(top = 60.dp, bottom = 40.dp)
            .fillMaxWidth()
    )
}

@Composable
fun PetCard(pet: Pet, petSelected: (pet: Pet) -> Unit) {
    val modifier = Modifier
    val imageSize = 192
    ConstraintLayout(
        modifier
            .padding(top = (imageSize / 2).dp)
            .clickable {
                petSelected(pet)
            }
    ) {
        val (surface, petImage, infoCard) = createRefs()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(surface) {}
        )
        Card(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .wrapContentHeight(Alignment.CenterVertically)
                .constrainAs(infoCard) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 2.dp
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                ) {
                    PetAge(pet = pet, modifier = Modifier.weight(0.3f))
                    Spacer(modifier = Modifier.weight(0.5f))
                    PetSex(pet = pet, modifier = Modifier.weight(0.3f))
                }
                PetName(modifier = modifier, pet = pet)
            }
        }
        MaskedImage(
            modifier
                .offset(y = -(imageSize / 2).dp)
                .size(imageSize.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .constrainAs(petImage) {
                    top.linkTo(surface.top)
                    centerHorizontallyTo(surface)
                },
            pet.image
        )
    }
}

private enum class RotationStates {
    Original,
    Rotated
}

@Composable
fun MaskedImage(modifier: Modifier = Modifier, img: String) {
    val context = LocalContext.current

    CoilImage(
        modifier = modifier
            .background(MaterialTheme.colors.background),
        data = img,
        contentDescription = "Pet",
        contentScale = ContentScale.Inside,
        requestBuilder = {
            this.transformations(CircleCropTransformation(), MaskTransformation(context, R.drawable.pawprint))
        },
        error = {
            Image(painterResource(id = R.drawable.pawprint), "Error")
        },
        loading = {
            val state = remember { mutableStateOf(RotationStates.Original) }
            val transition = rememberInfiniteTransition()
            val rotation by transition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        0f at 0 with FastOutSlowInEasing
                        360f at 1000 with FastOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
            Image(
                painterResource(id = R.drawable.pawprint), "Paw",
                modifier = Modifier
                    .rotate(rotation)
            )
        },
    )
}
