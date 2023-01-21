package me.jschutz.kbff.http

/**
 * Enumerate of HTTP methods with a given [verb].
 */
enum class HttpMethod() {
    Connect,
    Delete,
    Get,
    Head,
    Options,
    Post,
    Put,
    Trace;

    /**
     * Verb as in HTTP RFC.
     */
    val verb = this.name.uppercase()
}
