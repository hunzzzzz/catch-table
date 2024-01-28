package org.team.b6.catchtable.global.home

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping
    fun mainPage(model: Model): String {
        return "index"
    }
}