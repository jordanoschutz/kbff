package me.jschutz.kbff.log.aop

import me.jschutz.kbff.http.HttpResponse
import java.util.logging.Level
import java.util.logging.Logger

fun requestLoggedBy(
    message: String? = null,
    request: () -> HttpResponse
): HttpResponse = request().apply {
    // todo: inject Logger.
    Logger
        .getLogger(null)
        .log(
            if (isSuccessful) Level.INFO else Level.SEVERE,
            "HTTP $status with $content"
        )
}
