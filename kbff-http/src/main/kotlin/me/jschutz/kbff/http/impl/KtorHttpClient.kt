package me.jschutz.kbff.http.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.runBlocking
import me.jschutz.kbff.http.*
import me.jschutz.kbff.http.HttpMethod
import me.jschutz.kbff.http.HttpResponse
import java.lang.annotation.Inherited
import io.ktor.http.HttpMethod.Companion as KtorHttpMethod

/**
 * An HTTP client implementation using Jetbrains' Ktor client.
 */
internal class KtorHttpClient(
    private val client: HttpClient,
    private val mapper: ObjectMapper
) : HttpClientInterface {

    override fun request(
        method: HttpMethod,
        url: String,
        body: Map<String, Any?>?,
        headers: Iterable<HttpHeader>
    ): HttpResponse = runBlocking {
        client.request(url) {
            this.method = KtorHttpMethod.parse(method.verb)
            headers {
                headers.forEach { append(it.name, it.formattedValues) }
            }
            timeout {
                requestTimeoutMillis = 60000L
            }
            body?.let {
                contentType(ContentType.Application.Json)
                setBody<String>(mapper.writeValueAsString(it))
            }
        }.run {
            val responseContent = bodyAsText()
            @Suppress("UNCHECKED_CAST")
            HttpResponse(
                status = status.value,
                content = mapper.readValue(responseContent, Map::class.java) as Map<String, Any?>?
            )
        }
    }
}
