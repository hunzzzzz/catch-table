package org.team.b6.catchtable

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class CatchTableApplication

fun main(args: Array<String>) {
	runApplication<CatchTableApplication>(*args)
}
