package app.pixroost.core.result

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AppResultTest {
    @Test
    fun `appCatching wraps return value`() {
        assertEquals(AppResult.Success(42), appCatching { 42 })
    }

    @Test
    fun `appCatching maps exception to unexpected error by default`() {
        val boom = IllegalStateException("boom")

        val result = appCatching { throw boom }

        val error = assertIs<AppResult.Failure>(result).error
        assertSame(boom, assertIs<UnexpectedError>(error).cause)
    }

    @Test
    fun `appCatching uses custom mapper`() {
        val result = appCatching(mapError = { NetworkError(it) }) { error("offline") }

        assertIs<NetworkError>(assertIs<AppResult.Failure>(result).error)
    }

    @Test
    fun `appCatching rethrows cancellation`() {
        assertFailsWith<CancellationException> {
            appCatching { throw CancellationException("cancelled") }
        }
    }

    @Test
    fun `appCatching does not catch errors`() {
        class TestError : Error()

        assertFailsWith<TestError> { appCatching { throw TestError() } }
    }

    @Test
    fun `appCatching turns timeout into failure`() = runTest {
        val result = appCatching { withTimeout(1) { awaitCancellation() } }

        assertIs<UnexpectedError>(assertIs<AppResult.Failure>(result).error)
    }

    @Test
    fun `appCatching keeps cancellation of the caller`() = runTest {
        var reachedAfterCatching = false
        val job = launch(start = CoroutineStart.UNDISPATCHED) {
            appCatching { awaitCancellation() }
            reachedAfterCatching = true
        }

        job.cancel()
        job.join()

        assertTrue(job.isCancelled)
        assertFalse(reachedAfterCatching)
    }

    @Test
    fun `error to string omits cause text`() {
        val error = StorageError(causeWithPath())

        assertEquals("StorageError", error.toString())
    }

    private fun causeWithPath() = IllegalStateException("/storage/emulated/0/DCIM/secret.jpg")

    @Test
    fun `map transforms success and keeps failure`() {
        assertEquals(AppResult.Success(2), AppResult.Success(1).map { it + 1 })

        val failure: AppResult<Int> = AppResult.Failure(NotFoundError())
        assertSame(failure, failure.map { it + 1 })
    }

    @Test
    fun `flatMap stops at first failure`() {
        val failure = AppResult.Failure(StorageError())

        val result = AppResult.Success(1).flatMap { failure }.flatMap { AppResult.Success(it) }

        assertSame(failure, result)
    }

    @Test
    fun `fold picks matching branch`() {
        assertEquals("ok 1", AppResult.Success(1).fold({ "ok $it" }, { "err" }))
        assertEquals("err", AppResult.Failure(UnauthorizedError()).fold({ "ok" }, { "err" }))
    }

    @Test
    fun `getOrNull and errorOrNull`() {
        val success: AppResult<Int> = AppResult.Success(1)
        val failure: AppResult<Int> = AppResult.Failure(PermissionDeniedError())

        assertEquals(1, success.getOrNull())
        assertNull(success.errorOrNull())
        assertNull(failure.getOrNull())
        assertIs<PermissionDeniedError>(failure.errorOrNull())
    }
}
