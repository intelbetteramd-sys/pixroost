plugins {
    alias(libs.plugins.pixroost.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Both types appear in the public API of this module, so they are api, not implementation.
            api(libs.kotlinx.coroutines.core)
            api(libs.kermit)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
