package me.jschutz.kbff.http

/**
 * Represents an HTTP response.
 */
data class HttpResponse(
    /**
     * HTTP status code.
     * todo: enum.
     */
    val status: Int,
    /**
     * Response content.
     * It is null when content is not provided.
     */
    val content: Json?
) {
    /**
     * True iff. [status] is in the interval [200, 300).
     */
    val isSuccessful = status in 200 until 300
}
