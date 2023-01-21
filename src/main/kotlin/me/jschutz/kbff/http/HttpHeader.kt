package me.jschutz.kbff.http

/**
 * Represents an HTTP header.
 */
class HttpHeader(val name: String, val values: Iterable<String>) {
    /**
     * Constructs a single-valued header.
     */
    constructor(name: String, value: String) : this(name, listOf(value))

    /**
     * Header [String] representation.
     * It will be given in the form of [name] followed by ":" and the list of values
     * [values] joined in a [String] separated by comma.
     */
    override fun toString(): String = "$name: ${values.joinToString()}"

    /**
     * Returns iff. [other] is a non-null [HttpHeader] and its [toString] result matches
     * self [toString].
     */
    override fun equals(other: Any?): Boolean = (other as? HttpHeader)?.toString() == toString()

    override fun hashCode(): Int = 31 * name.hashCode() + values.hashCode()
}
