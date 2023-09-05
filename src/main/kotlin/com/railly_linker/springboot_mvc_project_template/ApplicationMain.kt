package com.railly_linker.springboot_mvc_project_template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApplicationMain

fun main(args: Array<String>) {
    runApplication<ApplicationMain>(*args)
}
