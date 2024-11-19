/*
 * Copyright (c) 2023 Stoyan Vuchev / Copyright (c) 2024 Apolo Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package squircleshape

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 *
 *  Creates a [SquircleBasedShape].
 *
 *  @param topStartCorner The top start corner radius defined as [CornerSize].
 *  @param topEndCorner The top end corner radius defined as [CornerSize].
 *  @param bottomStartCorner The bottom start corner radius defined as [CornerSize].
 *  @param bottomEndCorner The bottom end corner radius defined as [CornerSize].
 *  @param cornerSmoothing (0.55f - rounded corner shape, 1f - fully pronounced squircle).
 *
 **/
class SquircleShape(
    val topStartCorner: CornerSize,
    val topEndCorner: CornerSize,
    val bottomStartCorner: CornerSize,
    val bottomEndCorner: CornerSize,
    val cornerSmoothing: Float
) : Shape {



    override fun toString(): String {
        return "RoundedCornerShape(topStart = $topStartCorner, topEnd = $topEndCorner, bottomStart = " +
                "$bottomStartCorner, bottomEnd = $bottomEndCorner, cornerSmoothing = $cornerSmoothing)"
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        var topStart = topStartCorner.toPx(size, density)
        var topEnd = topEndCorner.toPx(size, density)
        var bottomEnd = bottomStartCorner.toPx(size, density)
        var bottomStart = bottomEndCorner.toPx(size, density)
        val minDimension = size.minDimension

        if (topStart + bottomStart > minDimension) {
            val scale = minDimension / (topStart + bottomStart)
            topStart *= scale
            bottomStart *= scale
        }

        if (topEnd + bottomEnd > minDimension) {
            val scale = minDimension / (topEnd + bottomEnd)
            topEnd *= scale
            bottomEnd *= scale
        }

        require(topStart >= 0.0f && topEnd >= 0.0f && bottomEnd >= 0.0f && bottomStart >= 0.0f) {
            "Corner size in Px can't be negative(topStart = $topStart, topEnd = $topEnd, " +
                    "bottomEnd = $bottomEnd, bottomStart = $bottomStart)!"
        }

        return if (topStart + topEnd + bottomEnd + bottomStart == 0.0f) {
            Outline.Rectangle(size.toRect())
        } else {
            val isLtr = layoutDirection == LayoutDirection.Ltr
            Outline.Generic(
                path = squircleShapePath(
                    size = size,
                    topLeftCorner = clampedCornerRadius(if (isLtr) topStart else topEnd, size),
                    topRightCorner = clampedCornerRadius(if (isLtr) topEnd else topStart, size),
                    bottomLeftCorner = clampedCornerRadius(if (isLtr) bottomStart else bottomEnd, size),
                    bottomRightCorner = clampedCornerRadius(
                        if (isLtr) bottomEnd else bottomStart,
                        size
                    ),
                    cornerSmoothing = clampedCornerSmoothing(cornerSmoothing)
                )
            )
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SquircleShape) return false
        if (topStartCorner != other.topStartCorner) return false
        if (topEndCorner != other.topEndCorner) return false
        if (bottomStartCorner != other.bottomStartCorner) return false
        if (bottomEndCorner != other.bottomEndCorner) return false
        if (cornerSmoothing != other.cornerSmoothing) return false
        return true
    }

    override fun hashCode(): Int {
        var result = topStartCorner.hashCode()
        result = 31 * result + topEndCorner.hashCode()
        result = 31 * result + bottomStartCorner.hashCode()
        result = 31 * result + bottomEndCorner.hashCode()
        result = 31 * result + cornerSmoothing.hashCode()
        return result
    }

}

/**
 *
 *  Creates a [SquircleShape] with corner radius percent defined as [Int] value.
 *
 *  @param percent The corner radius percent from 0 to 100.
 *  @param cornerSmoothing (0.55f - rounded corner shape, 1f - fully pronounced squircle).
 *
 **/
fun SquircleShape(
    percent: Int = 100,
    cornerSmoothing: Float = CornerSmoothing.Medium
) = SquircleShape(
    topStartCorner = CornerSize(percent),
    topEndCorner = CornerSize(percent),
    bottomStartCorner = CornerSize(percent),
    bottomEndCorner = CornerSize(percent),
    cornerSmoothing = cornerSmoothing
)

/**
 *
 *  Creates a [SquircleShape] with corner radius defined as [Dp] value.
 *
 *  @param radius The corner radius.
 *  @param cornerSmoothing (0.55f - rounded corner shape, 1f - fully pronounced squircle).
 *
 **/
fun SquircleShape(
    radius: Dp,
    cornerSmoothing: Float = CornerSmoothing.Medium
) = SquircleShape(
    topStartCorner = CornerSize(radius),
    topEndCorner = CornerSize(radius),
    bottomStartCorner = CornerSize(radius),
    bottomEndCorner = CornerSize(radius),
    cornerSmoothing = cornerSmoothing
)

/**
 *
 *  Creates a [SquircleShape] with corner radius in pixels defined as [Float] value.
 *
 *  @param radius The corner radius in pixels.
 *  @param cornerSmoothing (0.55f - rounded corner shape, 1f - fully pronounced squircle).
 *
 **/
fun SquircleShape(
    radius: Float,
    cornerSmoothing: Float = CornerSmoothing.Medium
) = SquircleShape(
    topStartCorner = CornerSize(radius),
    topEndCorner = CornerSize(radius),
    bottomStartCorner = CornerSize(radius),
    bottomEndCorner = CornerSize(radius),
    cornerSmoothing = cornerSmoothing
)

/**
 *
 *  Creates a [SquircleShape] with corners percent defined as [Int] values.
 *
 *  @param topStart The top start corner radius percent from 0 to 100.
 *  @param topEnd The top end corner radius percent from 0 to 100.
 *  @param bottomStart The bottom start corner radius percent from 0 to 100.
 *  @param bottomEnd The bottom end corner radius percent from 0 to 100.
 *  @param cornerSmoothing (0.55f - rounded corner shape, 1f - fully pronounced squircle).
 *
 **/
fun SquircleShape(
    topStart: Int = 0,
    topEnd: Int = 0,
    bottomStart: Int = 0,
    bottomEnd: Int = 0,
    cornerSmoothing: Float = CornerSmoothing.Medium
) = SquircleShape(
    topStartCorner = CornerSize(topStart),
    topEndCorner = CornerSize(topEnd),
    bottomStartCorner = CornerSize(bottomStart),
    bottomEndCorner = CornerSize(bottomEnd),
    cornerSmoothing = cornerSmoothing
)

/**
 *
 *  Creates a [SquircleShape] with corners defined as [Dp] values.
 *
 *  @param topStart The top start corner radius.
 *  @param topEnd The top end corner radius.
 *  @param bottomStart The bottom start corner radius.
 *  @param bottomEnd The bottom end corner radius.
 *  @param cornerSmoothing (0.55f - rounded corner shape, 1f - fully pronounced squircle).
 *
 **/
fun SquircleShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    cornerSmoothing: Float = CornerSmoothing.Medium
) = SquircleShape(
    topStartCorner = CornerSize(topStart),
    topEndCorner = CornerSize(topEnd),
    bottomStartCorner = CornerSize(bottomStart),
    bottomEndCorner = CornerSize(bottomEnd),
    cornerSmoothing = cornerSmoothing
)

/**
 *
 *  Creates a [SquircleShape] with pixel corners defined as [Float] values.
 *
 *  @param topStart The top start corner radius in pixels.
 *  @param topEnd The top end corner radius.
 *  @param bottomStart The bottom start corner radius.
 *  @param bottomEnd The bottom end corner radius.
 *  @param cornerSmoothing (0.55f - rounded corner shape, 1f - fully pronounced squircle).
 *
 **/
fun SquircleShape(
    topStart: Float = 0f,
    topEnd: Float = 0f,
    bottomStart: Float = 0f,
    bottomEnd: Float = 0f,
    cornerSmoothing: Float = CornerSmoothing.Medium
) = SquircleShape(
    topStartCorner = CornerSize(topStart),
    topEndCorner = CornerSize(topEnd),
    bottomStartCorner = CornerSize(bottomStart),
    bottomEndCorner = CornerSize(bottomEnd),
    cornerSmoothing = cornerSmoothing
)