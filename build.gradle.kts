plugins {
    kotlin("jvm") version "2.0.0" apply false
    kotlin("multiplatform") version "2.0.0" apply false
    kotlin("android") version "2.0.0" apply false
    kotlin("plugin.serialization") version "2.0.0" apply false
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.compose") version "1.6.11" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}