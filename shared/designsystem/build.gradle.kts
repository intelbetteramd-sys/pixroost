plugins {
    alias(libs.plugins.pixroost.kmp.library)
    alias(libs.plugins.pixroost.compose)
    alias(libs.plugins.roborazzi)
}

compose.resources {
    // Fonts live in src/commonMain/composeResources; Res stays inside the module.
    packageOfResClass = "app.pixroost.designsystem.resources"
    publicResClass = false
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core"))
            implementation(libs.compose.components.resources)
        }
        // Screenshot tests run on the desktop (JVM) target, which renders the same Skia output on Linux CI.
        getByName("desktopTest").dependencies {
            implementation(libs.compose.ui.test)
            implementation(libs.roborazzi.compose.desktop)
            implementation(compose.desktop.currentOs)
        }
    }
}
