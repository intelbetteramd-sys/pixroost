package app.pixroost.database

/** Constants of the local database. */
object DatabaseConstants {
    /** Schema version. Raise it only together with a migration in [DatabaseMigrations] and a migration test. */
    const val VERSION = 1

    /** File name of the database inside the app's private storage. */
    const val FILE_NAME = "pixroost.db"
}
