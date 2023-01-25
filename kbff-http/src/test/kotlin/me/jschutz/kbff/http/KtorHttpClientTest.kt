package me.jschutz.kbff.http

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import me.jschutz.kbff.http.impl.KtorHttpClient
import me.jschutz.kbff.http.utils.mapsAreEquals
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class KtorHttpClientTest {
    private val mapper by lazy { jacksonObjectMapper() }

    @Test
    fun `GET successful with response body`() {
        val expectedResponse = mapOf(
            "name" to "my name",
            "age" to 30,
            "status" to true
        )
        val url = mountUrl("resource/get")
        val ktorClient = newKtorClient(
            url = url,
            responseBody = mapper.writeValueAsString(expectedResponse),
            httpStatus = HttpStatusCode.OK
        )

        val response = ktorClient.request(
            method = HttpMethod.Get,
            url = url
        )

        assertTrue(response.isSuccessful)
        assertEquals(HttpStatusCode.OK.value, response.status)
        response.content.let {
            assertNotNull(it)
            assertTrue(mapsAreEquals(expectedResponse, it!!))
        }
    }

    @Test
    fun `POST successful with request and response bodies`() {
        val expectedResponse = mapOf(
            "id" to UUID.randomUUID().toString(),
            "created_at" to "2023-01-01 00:00:00"
        )
        val url = mountUrl("resource/post")
        val ktorClient = newKtorClient(
            url = url,
            responseBody = mapper.writeValueAsString(expectedResponse),
            httpStatus = HttpStatusCode.Created
        )

        val response = ktorClient.request(
            method = HttpMethod.Post,
            url = url,
            body = mapOf(
                "name" to "some name",
                "value" to 3.14,
                "new_born" to true,
                "fingers" to 20
            )
        )

        assertTrue(response.isSuccessful)
        assertEquals(HttpStatusCode.Created.value, response.status)
        response.content.let {
            assertNotNull(it)
            assertTrue(mapsAreEquals(expectedResponse, it!!))
        }
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

    /**
     * Adjusts [target] to match [KtorHttpClient] normalized URL on requests.
     */
    private fun mountUrl(target: String) = "http://localhost/$target"
}
