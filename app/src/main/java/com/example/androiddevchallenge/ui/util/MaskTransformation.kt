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
package com.example.androiddevchallenge.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation

class MaskTransformation(
    private val context: Context,
    @DrawableRes val maskDrawableRes: Int
) : Transformation {

    companion object {
        private val paint = Paint()
            .apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            }
    }

    override fun key(): String = "${MaskTransformation::class.java.name}-$maskDrawableRes"

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val width = input.width
        val height = input.height

        val output = pool.get(width, height, input.config)
        output.setHasAlpha(true)

        val mask: BitmapDrawable = getMaskDrawable(context.applicationContext, maskDrawableRes)

        output.applyCanvas {
            mask.setBounds(0, 0, width, height)
            mask.draw(this)
            drawBitmap(input, 0f, 0f, paint)
        }
        pool.put(input)
        return output
    }

    private fun getMaskDrawable(context: Context, maskId: Int): BitmapDrawable {
        return ResourcesCompat.getDrawable(context.resources, maskId, null) as BitmapDrawable?
            ?: throw IllegalArgumentException("maskId is invalid")
    }
}
