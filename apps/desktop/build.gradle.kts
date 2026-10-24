import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.pixroost.desktop.application)
}

dependencies {
    implementation(project(":shared:core"))
    implementation(project(":shared:designsystem"))
    implementation(libs.compose.material3)
    implementation(compose.desktop.currentOs)
}

val appVersion = "1.0.0"

compose.desktop {
    application {
        mainClass = "app.pixroost.desktop.MainKt"
        nativeDistributions {
            // Spike S-07 (step 1.10): the Windows .msi comes from packageSpikeMsi (not Compose) to add a firewall rule.
            targetFormats(TargetFormat.Msi, TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "Pixroost"
            packageVersion = appVersion
        }
    }
}

// --- Spike S-07: Windows installers built from the Compose app image (createDistributable). ---
// Compose has no MSIX target and rewrites its jpackage resource dir, so both installers are made by scripts
// in spike-installer/ that run on Windows only.
val spikeDir = layout.projectDirectory.dir("spike-installer")
val appImageDir = layout.buildDirectory.dir("compose/binaries/main/app/Pixroost")
val installersDir = layout.buildDirectory.dir("spike-installers")
val isWindowsHost = System.getProperty("os.name").startsWith("Windows")
val gradleJavaHome: String = System.getProperty("java.home")

fun powershellScript(script: String, vararg args: String): List<String> =
    listOf(
        "powershell.exe", "-NoProfile", "-ExecutionPolicy", "Bypass", "-File",
        spikeDir.file(script).asFile.absolutePath,
    ) + args

tasks.register<Exec>("packageSpikeMsi") {
    group = "distribution"
    description = "Builds Pixroost.msi with a firewall rule (custom main.wxs) from the app image."
    dependsOn("createDistributable")
    onlyIf { isWindowsHost }
    inputs.dir(appImageDir)
    inputs.dir(spikeDir)
    outputs.dir(installersDir)
    commandLine(
        powershellScript(
            "build-msi.ps1",
            "-AppImage", appImageDir.get().asFile.absolutePath,
            "-ResourceDir", spikeDir.asFile.absolutePath,
            "-Output", installersDir.get().asFile.absolutePath,
            "-JavaHome", gradleJavaHome,
            "-Version", appVersion,
        ),
    )
}

tasks.register<Exec>("packageSpikeMsix") {
    group = "distribution"
    description = "Builds an unsigned Pixroost.msix (firewall rule, startup task) from the app image."
    dependsOn("createDistributable")
    onlyIf { isWindowsHost }
    inputs.dir(appImageDir)
    inputs.dir(spikeDir)
    outputs.dir(installersDir)
    commandLine(
        powershellScript(
            "build-msix.ps1",
            "-AppImage", appImageDir.get().asFile.absolutePath,
            "-SpikeDir", spikeDir.asFile.absolutePath,
            "-Output", installersDir.get().asFile.absolutePath,
            "-Version", "$appVersion.0",
        ),
    )
}
