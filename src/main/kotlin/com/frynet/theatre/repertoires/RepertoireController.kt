package com.frynet.theatre.repertoires

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@Controller
@Tag(name = "repertoires")
@RequestMapping(value = ["/repertoires"])
class RepertoireController {

    @Autowired
    private lateinit var repertoireService: RepertoireService

    @PostMapping(
        path = ["/date"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "спектакли в определённый день")
    @ResponseBody
    fun getSpectaclesByDate(@RequestBody date: RepertoireDate): List<RepertoireInfo> {
        return repertoireService.getSpectaclesByDate(date)
    }

    @PostMapping(
        path = ["/interval"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "расписание на несколько дней")
    @ResponseBody
    fun getSpectaclesByInterval(@RequestBody interval: RepertoireDateInterval): Map<LocalDate, List<RepertoireInfo>> {
        return repertoireService.getSpectaclesByInterval(interval)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "запланировать спектакль")
    @ResponseBody
    fun scheduleSpectacle(@RequestBody info: RepertoireInfo) {
        repertoireService.scheduleSpectacle(info)
    }

    @DeleteMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "отменить спектакль")
    @ResponseBody
    fun cancelSpectacle(@RequestBody info: RepertoireInfo) {
        repertoireService.cancelSpectacle(info)
    }

    @DeleteMapping(path = ["/all"])
    @Hidden
    @ResponseBody
    fun deleteAll() {
        repertoireService.deleteAll()
    }
}