package me.jschutz.kbff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KbffApplication

fun main(args: Array<String>) {
    runApplication<KbffApplication>(*args)
}
