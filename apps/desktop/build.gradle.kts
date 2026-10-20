import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.pixroost.desktop.application)
}

dependencies {
    implementation(project(":shared:core"))
    implementation(project(":shared:designsystem"))
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.material3)
    implementation(libs.metadata.extractor)
}

compose.desktop {
    application {
        mainClass = "app.pixroost.desktop.MainKt"
        // Spike S-06 decodes full-size photos: cap the heap so it cannot eat the whole PC.
        jvmArgs += listOf("-Xmx2g")
        nativeDistributions {
            // Installers are worked out in spike S-07 (step 1.10).
            targetFormats(TargetFormat.Msi, TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "Pixroost"
        }
    }
}
