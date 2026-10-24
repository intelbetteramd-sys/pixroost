package app.pixroost.database

import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test

class TmpSchemaDumpTest {
    @Test
    fun dump() {
        val text = Files.readString(Path.of("schemas/app.pixroost.database.PixroostDatabase/1.json"))
        val target = Path.of("../../build/reports/schema1.json")
        Files.createDirectories(target.parent)
        Files.writeString(target, text)
        kotlin.test.fail("dumped")
    }
}
