package me.jschutz.kbff.http

/**
 * An interface for HTTP clients.
 * Provides the essential methods for HTTP communication.
 */
interface HttpClientInterface {
    /**
     * Executes an HTTP [method] aimed at [url].
     * An optional request payload can be provided in [body],
     * with optional custom HTTP headers [headers].
     * The response will be returned as an [HttpResponse].
     */
    fun request(
        method: HttpMethod,
        url: String,
        body: Json? = null,
        headers: Iterable<HttpHeader> = emptyList(),
        responseHandler: HttpResponse.() -> Unit
    ): HttpResponse
}
