pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // Downloads the JDK 25 toolchain when it is not installed.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// Build tools bring libraries with known vulnerabilities: the Android Gradle plugin (BouncyCastle, jdom2,
// jose4j, httpclient, commons-lang3), Kover (FreeMarker), the Android test platform (Netty) and the Kotlin
// Swift export (OpenTelemetry). They run only during the build and never reach the apps, but are raised to
// patched versions anyway. A version is only raised, never lowered; drop an entry once the tool that brings
// the library ships a newer one.
val patchedBuildLibraries = mapOf(
    "io.netty" to "4.1.137.Final",
    "org.bouncycastle" to "1.85",
    "org.freemarker:freemarker" to "2.3.35",
    "org.jdom:jdom2" to "2.0.6.1",
    "org.bitbucket.b_c:jose4j" to "0.9.6",
    "org.apache.httpcomponents:httpclient" to "4.5.14",
    "org.apache.commons:commons-lang3" to "3.18.0",
    "io.opentelemetry:opentelemetry-api" to "1.62.0",
    "io.opentelemetry:opentelemetry-context" to "1.62.0",
)

fun versionParts(version: String): List<Int> = version.split('.', '-').mapNotNull { it.toIntOrNull() }

fun isOlder(version: String, than: String): Boolean {
    val (a, b) = versionParts(version) to versionParts(than)
    val difference = a.zip(b).map { (x, y) -> x.compareTo(y) }.firstOrNull { it != 0 }
    return (difference ?: a.size.compareTo(b.size)) < 0
}

fun Configuration.raiseToPatched() = resolutionStrategy.eachDependency {
    // netty-tcnative has its own version line, BouncyCastle artifacts for older JDKs too.
    val module = "${requested.group}:${requested.name}"
    val patched = patchedBuildLibraries[module]
        ?: patchedBuildLibraries[requested.group]?.takeIf {
            !requested.name.startsWith("netty-tcnative") && !requested.name.endsWith("jdk15on")
        }
    val current = requested.version
    if (patched != null && current != null && isOlder(current, patched)) {
        useVersion(patched)
        because("patched version of a build-time library, see settings.gradle.kts")
    }
}

gradle.beforeProject {
    buildscript.configurations.configureEach { raiseToPatched() }
    configurations.configureEach { raiseToPatched() }
}

rootProject.name = "pixroost"

include(
    ":apps:android",
    ":apps:desktop",
    ":apps:ios-framework",
    ":shared:core",
    ":shared:designsystem",
)
