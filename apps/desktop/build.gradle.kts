import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.pixroost.desktop.application)
}

dependencies {
    implementation(project(":shared:app"))
    implementation(project(":shared:core"))
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "app.pixroost.desktop.MainKt"
        // Log level for developer runs: ./gradlew :apps:desktop:run -Ppixroost.logLevel=debug
        // Installers are built without it and log at Info. Read in Main.kt.
        providers.gradleProperty("pixroost.logLevel").orNull?.let { jvmArgs += "-Dpixroost.log.level=$it" }
        nativeDistributions {
            // Installers are worked out in spike S-07 (step 1.10).
            targetFormats(TargetFormat.Msi, TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "Pixroost"
        }
    }
}
