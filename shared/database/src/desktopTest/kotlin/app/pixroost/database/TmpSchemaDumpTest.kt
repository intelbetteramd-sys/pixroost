package app.pixroost.database

import java.nio.file.Files
import java.nio.file.Path
import java.util.Base64
import kotlin.test.Test
import kotlin.test.fail

class TmpSchemaDumpTest {
    @Test
    fun dump() {
        val text = Files.readString(Path.of("schemas/app.pixroost.database.PixroostDatabase/1.json"))
        fail("BEGIN_SCHEMA\n" + Base64.getEncoder().encodeToString(text.toByteArray()) + "\nEND_SCHEMA")
    }
}
