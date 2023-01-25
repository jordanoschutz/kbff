package me.jschutz.kbff.http.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import me.jschutz.kbff.http.HttpHeader
import me.jschutz.kbff.http.HttpMethod
import me.jschutz.kbff.http.impl.KtorHttpClient
import me.jschutz.kbff.http.utils.mapsAreEquals
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.*

class KtorHttpClientTest {
    private val mapper by lazy { jacksonObjectMapper() }

    @ParameterizedTest(name = "{1} {4} with request {2} and response {3}")
    @ArgumentsSource(SuccessfulRequestProvider::class)
    fun `HTTP request successful`(
        uri: String,
        method: HttpMethod,
        requestBody: Map<String, Any?>?,
        responseBody: Map<String, Any?>?,
        expectedResponseStatus: HttpStatusCode
    ) {
        val ktorClient = newKtorClient(
            url = uri,
            responseBody = responseBody?.let { mapper.writeValueAsString(it) } ?: "",
            httpStatus = expectedResponseStatus
        )
        val response = ktorClient.request(
            method = method,
            url = uri,
            requestBody,
            headers = listOf(HttpHeader(name = "X-Test-Id", value = UUID.randomUUID().toString()))
        )

        assertTrue(response.isSuccessful)
        assertEquals(expectedResponseStatus.value, response.status)
        response.content.let {
            if (responseBody === null) {
                assertTrue(it.isNullOrEmpty())
            } else {
                assertNotNull(it)
                assertTrue(mapsAreEquals(responseBody, it!!))
            }
        }
    }

    @ParameterizedTest(name = "{1} returns {2}")
    @ArgumentsSource(UnsuccessfulRequestProvider::class)
    fun `HTTP request unsuccessful`(uri: String, method: HttpMethod, expectedStatusError: HttpStatusCode) {
        val ktorClient = newKtorClient(
            url = uri,
            responseBody = "",
            httpStatus = expectedStatusError
        )
        val response = ktorClient.request(
            method = method,
            url = uri
        )

        assertFalse(response.isSuccessful)
        assertEquals(expectedStatusError.value, response.status)
        assertTrue(response.content.isNullOrEmpty())
    }

    /**
     * Constructs an instance of [KtorHttpClient] from trivial
     * HTTP request parameters.
     */
    private fun newKtorClient(
        url: String,
        responseBody: String,
        httpStatus: HttpStatusCode
    ) = KtorHttpClient(
        client = mockHttpClient(url, responseBody, httpStatus),
        mapper = mapper
    )

    /**
     * Mocks a Ktor HTTP Client for a request towards
     * [url], in which the expected HTTP status code will
     * be [httpStatus] with returning payload [responseBody].
     */
    private fun mockHttpClient(
        url: String,
        responseBody: String,
        httpStatus: HttpStatusCode,
        headers: Headers = headersOf()
    ) = HttpClient(MockEngine) {
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
        }
        engine {
            addHandler { request ->
                assertEquals(request.url.toString(), url)
                respond(responseBody, httpStatus, headers)
            }
        }
    }
}
