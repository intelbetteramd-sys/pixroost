plugins {
    alias(libs.plugins.pixroost.kmp.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room3)
}

// Every version of the schema is exported here and kept in the repository: migration tests read it.
room3 {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Room, SQLite and coroutines types appear in the public API (DAOs, builder, migrations).
            api(libs.room3.runtime)
            api(libs.androidx.sqlite)
            // DispatcherProvider is in the public API of applyPixroostDefaults.
            api(project(":shared:core"))
            api(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.sqlite.bundled)
        }
        // Query and migration tests run on the JVM against a real SQLite file (the bundled driver).
        getByName("desktopTest").dependencies {
            implementation(libs.room3.testing)
            implementation(libs.junit)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

// Room generates the database constructor and the DAO implementations once per target.
dependencies {
    listOf("kspAndroid", "kspDesktop", "kspIosArm64", "kspIosSimulatorArm64").forEach { configuration ->
        add(configuration, libs.room3.compiler)
    }
}
