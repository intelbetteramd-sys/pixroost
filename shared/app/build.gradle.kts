// App shell: Koin setup, the navigation model and the root screen that apps/* host.
// Feature modules (shared/feature/*) do not know each other; this module will connect them.
plugins {
    alias(libs.plugins.pixroost.kmp.library)
    alias(libs.plugins.pixroost.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.navigation3.ui)
        }
    }
}
