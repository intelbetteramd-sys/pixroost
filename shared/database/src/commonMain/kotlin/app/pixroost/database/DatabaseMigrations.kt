package app.pixroost.database

import androidx.room3.migration.Migration

/**
 * Every migration between schema versions, in the order they were written.
 *
 * Version 1 is the first schema, so there is nothing to migrate yet. A new version adds a [Migration] here,
 * exports `schemas/app.pixroost.database.PixroostDatabase/<version>.json` and extends `DatabaseMigrationTest`.
 */
object DatabaseMigrations {
    val all: List<Migration> = emptyList()
}
