package app.pixroost.core.result

import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame

class AppResultTest {
    @Test
    fun appCatchingWrapsReturnValue() {
        assertEquals(AppResult.Success(42), appCatching { 42 })
    }

    @Test
    fun appCatchingMapsExceptionToUnknownByDefault() {
        val boom = IllegalStateException("boom")

        val result = appCatching { throw boom }

        val error = assertIs<AppResult.Failure>(result).error
        assertSame(boom, assertIs<UnexpectedError>(error).cause)
    }

    @Test
    fun appCatchingUsesCustomMapper() {
        val result = appCatching(mapError = { NetworkError(it) }) { error("offline") }

        assertIs<NetworkError>(assertIs<AppResult.Failure>(result).error)
    }

    @Test
    fun appCatchingRethrowsCancellation() {
        assertFailsWith<CancellationException> {
            appCatching { throw CancellationException("cancelled") }
        }
    }

    @Test
    fun mapTransformsSuccessAndKeepsFailure() {
        assertEquals(AppResult.Success(2), AppResult.Success(1).map { it + 1 })

        val failure: AppResult<Int> = AppResult.Failure(NotFoundError())
        assertSame(failure, failure.map { it + 1 })
    }

    @Test
    fun flatMapStopsAtFirstFailure() {
        val failure = AppResult.Failure(StorageError())

        val result = AppResult.Success(1).flatMap { failure }.flatMap { AppResult.Success(it) }

        assertSame(failure, result)
    }

    @Test
    fun foldPicksMatchingBranch() {
        assertEquals("ok 1", AppResult.Success(1).fold({ "ok $it" }, { "err" }))
        assertEquals("err", AppResult.Failure(UnauthorizedError()).fold({ "ok" }, { "err" }))
    }

    @Test
    fun getOrNullAndErrorOrNull() {
        val success: AppResult<Int> = AppResult.Success(1)
        val failure: AppResult<Int> = AppResult.Failure(PermissionDeniedError())

        assertEquals(1, success.getOrNull())
        assertNull(success.errorOrNull())
        assertNull(failure.getOrNull())
        assertIs<PermissionDeniedError>(failure.errorOrNull())
    }
}
