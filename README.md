# Squircle Shape


[![Download](https://img.shields.io/maven-central/v/com.composevisualeditor.apoloapps/squircle-shape)](https://central.sonatype.com/artifact/com.composevisualeditor.apoloapps/squircle-shape)
<a href="https://github.com/ApoloApps/squircle-shape/commits/master"><img src="https://img.shields.io/github/last-commit/ApoloApps/squircle-shape.svg?style=flat&logo=github&logoColor=white" alt="GitHub last commit"></a>
<!--![Kotlin](https://img.shields.io/badge/kotlin-2.1.0--RC2-blue.svg?logo=kotlin)
![Compose MP](https://img.shields.io/badge/Compose%20Multiplatform-1.8.0--dev1920-blue.svg?logo=jetpackcompose)-->

> A Compose Multiplatform library providing customizable Squircle shapes for UI components.

---

## ✨ Features

- **Customizable Squircle Shapes**: Create UI shapes that smoothly transition between squares and circles.
- **Integration with `MaterialTheme`**: Use squircle shapes directly in Jetpack Compose themes.
- **Corner Smoothing**: Fine-tune the smoothness of corners for a delightful design.
- **Multiplatform Support**: Available for Android, iOS, Desktop (JVM), and Web (WasmJS).
- **Canvas Drawing**: Easily draw squircle shapes on canvases with `drawSquircle()`.

---

## 🆕 What's New in Version 3.0.0?

- **Seamless `MaterialTheme` Integration**:  
  `SquircleBasedShape` now extends `CornerBasedShape`, allowing you to use it directly in `androidx.compose.material3.Shapes`.

- **Corner Smoothing Support**:  
  Added the `cornerSmoothing` parameter for effortless corner smoothing without extra overhead.

- **RTL Layout Support**:  
  Fully compatible with both `LayoutDirection.Ltr` and `LayoutDirection.Rtl`, ensuring proper corner mapping.

- **Performance Enhancements**:  
  Optimized corner clamping logic for improved rendering performance.

---

## 📋 Requirements

### For Multiplatform Projects:
- Kotlin: `2.0.21`
- Compose: `1.7.1`

### For Android-only Projects:
- Kotlin: `2.0.21`
- Jetpack Compose: `1.7.1`
- Minimum SDK: `23`
- Compile SDK: `35`

---

## 📦 Setup

### Gradle Kotlin DSL (Multiplatform)

1. Add the dependency in your shared module's `build.gradle.kts`:
```kotlin
sourceSets {
	commonMain.dependencies{
 	 implementation("com.composevisualeditor.apoloapps:squircle-shape:<version>")
	}
}
```

2. Sync and rebuild the project. 🔄️🔨✅

---

## Gradle Kotlin DSL Setup (For Android-only projects).

* 1. Add the Squircle Shape dependency in your module `build.gradle.kts` file.
* Latest version: ![Maven Central Version](https://img.shields.io/maven-central/v/com.composevisualeditor.apoloapps/squircle-shape)

```kotlin
dependencies {
    implementation("com.composevisualeditor.apoloapps:cve-squircle-shape-android:<version>")

}
```

* Or if you're using a version catalog (e.g. `libs.versions.toml`), declare it in the catalog instead.

```toml
[versions]
squircle-shape = "<version>"

[libraries]


squircle-shape = { group = "com.composevisualeditor.apoloapps", name = "cve-squircle-shape-android", version.ref = "squircle-shape" }
```

* Then include the dependency in your module `build.gradle.kts` file.

```kotlin
dependencies {

  // ...

  implementation(libs.squircle.shape)

}
```
2. Sync and rebuild the project. 🔄️🔨✅

---

## 🚀 Usage

### 1. **Using Squircle Shapes with `MaterialTheme`**

Define squircle shapes in your theme to use them consistently across your app:

```kotlin
val shapes = Shapes(
    small = SquircleShape(radius = 16.dp, cornerSmoothing = CornerSmoothing.Medium),
    medium = SquircleShape(radius = 32.dp, cornerSmoothing = CornerSmoothing.Medium),
    large = SquircleShape(percent = 100, cornerSmoothing = CornerSmoothing.Medium)
)

MaterialTheme(
    shapes = shapes
) {

    // ...

    Button(
        onClick = { /* Action */ },
        shape = MaterialTheme.shapes.large // Clipped to the provided `large` material theme shape.
    ) {
        Text(text = "Full Squircle")
    }

    // ...

}
```

![Button with Full Squircle shape.](./readme_images/full_squircle.png)

### 2. **Using Squircle Shapes separately**

Clip UI components separately by using a `SquircleShape()` function.

```kotlin
Image(
    modifier = Modifier
        .size(128.dp)
        .clip(
            shape = SquircleShape(
                percent = 100,
                cornerSmoothing = CornerSmoothing.Medium
            )
        ), // Clipped to a fully rounded squircle shape.
    painter = painterResource(R.drawable.mlbb_novaria),
    contentDescription = "An image of Novaria.",
    contentScale = ContentScale.Crop
)
```

![A portrait image of Novaria from MLBB clipped to a Squircle shape.](./readme_images/mlbb_novaria.png)

You can customize the radii for all corners, or for each corner independently.
Supported corner values are:

- `Int` for percent-based corner radius in range 0..100
- `Float` for pixel-based corner radius e.g. `50f`
- `Dp` for density pixel-based corner radius e.g. `16.dp`

```kotlin
// Single-corner percent-based radius implementation.
SquircleShape(
    percent = 100,
    cornerSmoothing = .6f
)

// Single-corner pixel-based radius implementation.
SquircleShape(
    radius = 32f,
    cornerSmoothing = .6f
)

// Single-corner density pixel-based radius implementation.
SquircleShape(
    radius = 32.dp,
    cornerSmoothing = .6f
)

// Multi-corner percent-based radius implementation.
SquircleShape(
    topStart = 25,
    topEnd = 5,
    bottomStart = 25,
    bottomEnd = 5,
    cornerSmoothing = .6f
)

// Multi-corner pixel-based radius implementation.
SquircleShape(
    topStart = 32f,
    topEnd = 8f,
    bottomStart = 32f,
    bottomEnd = 8f,
    cornerSmoothing = .6f
)

// Multi-corner density pixel-based radius implementation.
SquircleShape(
    topStart = 32.dp,
    topEnd = 8.dp,
    bottomStart = 32.dp,
    bottomEnd = 8.dp,
    cornerSmoothing = .6f
)
```

### 3. Draw a Squircle on Canvas

You can draw squircle shapes on a canvas for custom graphics.

Note: currently `drawSquircle` only accepts pixel-based values for each corner:

```kotlin
Canvas(
  modifier = Modifier.size(150.dp),
  onDraw = {

    drawSquircle(
      color = Color.Blue,
      topLeft = Offset.Zero,
      size = this.size,
      topLeftCorner = 32.dp.toPx(),
      topRightCorner = 8.dp.toPx(),
      bottomRightCorner = 32.dp.toPx(),
      bottomLeftCorner = 8.dp.toPx(),
      cornerSmoothing = .6f
    )

  }
)
```

## 📄 License

This project is open source and available under the [MIT License](./LICENSE).

```
MIT License

Copyright (c) 2023 Stoyan Vuchev / Copyright (c) 2024-2025 Apolo Apps

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## Contact

Created by [@stoyan-vuchev](https://github.com/stoyan-vuchev/) - feel free to contact me! <br/>
E-mail - [contact@stoyanvuchev.com](mailto://contact@stoyanvuchev.com)
<br/>
Special thanks to [@stoyan-vuchev](https://github.com/stoyan-vuchev/)

Forked by [@ApoloApps](https://github.com/ApoloApps/)
