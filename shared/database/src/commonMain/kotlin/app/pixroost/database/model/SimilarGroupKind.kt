package app.pixroost.database.model

/**
 * Kind of a group of similar shots.
 *
 * Stored as the constant name; never rename a constant without a migration.
 */
enum class SimilarGroupKind {
    SAME_SHOT,
    SIMILAR,
}
