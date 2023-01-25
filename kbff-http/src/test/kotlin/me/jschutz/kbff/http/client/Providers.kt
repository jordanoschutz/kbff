package me.jschutz.kbff.http.client

import io.ktor.http.*
import me.jschutz.kbff.http.HttpMethod
import me.jschutz.kbff.http.impl.KtorHttpClient
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.*
import java.util.stream.Stream

/**
 * HTTP successful requests test data provider.
 */
object SuccessfulRequestProvider : ArgumentsProvider {
    /**
     * Returns the following arguments for successful requests:
     * [String] URI, [HttpMethod] method, [Map] request body (nullable),
     * [Map] response body (also nullable) and the expected response as [HttpStatusCode].
     *
     * Each of these arguments will be provided for all HTTP methods, as seen in [HttpMethod].
     */
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        val allMethods = HttpMethod.values()
        val body = mapOf(
            "id" to UUID.randomUUID().toString(),
            "natural_value" to System.currentTimeMillis(),
            "real_value" to 3.14,
            "status" to true,
            "property" to mapOf("id" to UUID.randomUUID().toString(), "date_binding" to "2023-01-01 00:00:00")
        )
        val bodyVariations = listOf(
            Pair(null, null),
            Pair(null, body),
            Pair(body, null),
            Pair(body, body),
        )
        val arguments = mutableListOf<Arguments>()

        allMethods.forEach { httpMethod ->
            bodyVariations.forEach { (requestBody, responseBody) ->
                arguments += Arguments.of(
                    mountUrl(httpMethod),
                    httpMethod,
                    requestBody,
                    responseBody,
                    if (responseBody == null) {
                        HttpStatusCode.NoContent
                    } else {
                        when (httpMethod) {
                            HttpMethod.Post -> HttpStatusCode.Created
                            else -> HttpStatusCode.OK
                        }
                    }
                )
            }
        }

        return Stream.of(*arguments.toTypedArray())
    }
}


/**
 * HTTP unsuccessful requests test data provider.
 */
object UnsuccessfulRequestProvider : ArgumentsProvider {
    /**
     * For error-prone requests, this method simply provides the arguments
     * for the [String] URI (argument #1), the HTTP method [HttpMethod] (argument #2) and
     * a commonly used HTTP error status [HttpStatusCode] (argument #3).
     */
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        val allMethods = HttpMethod.values()
        val commonErrorResponses = arrayOf(
            HttpStatusCode.NotFound,
            HttpStatusCode.Forbidden,
            HttpStatusCode.Unauthorized,
            HttpStatusCode.ServiceUnavailable,
            HttpStatusCode.BadGateway,
            HttpStatusCode.InternalServerError
        )
        val arguments = mutableListOf<Arguments>()

        allMethods.forEach { httpMethod ->
            commonErrorResponses.forEach { errorStatusCode ->
                arguments.add(
                    Arguments.of(
                        mountUrl(httpMethod),
                        httpMethod,
                        errorStatusCode
                    )
                )
            }
        }

        return Stream.of(*arguments.toTypedArray())
    }

}


/**
 * Adjusts [target] to match [KtorHttpClient] normalized URL on requests.
 */
private fun mountUrl(method: HttpMethod) = "http://localhost/resource/${method.verb.lowercase()}"
