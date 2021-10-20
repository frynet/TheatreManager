package com.frynet.theatre.hall

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@Tag(name = "hall")
@RequestMapping(value = ["/hall"])
class HallController {

    @Autowired
    private lateinit var hallService: HallService

    @PostMapping(
        path = ["/contains"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "узнать, есть ли такое место в зале")
    @ResponseBody
    fun contains(@RequestBody place: HallPlace): Boolean {
        return hallService.contains(place)
    }

    @GetMapping(
        path = ["/size"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "узнать размер зала")
    @ResponseBody
    fun getHallSize(): HallSize {
        return hallService.getHallSize()
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "изменить размер зала")
    @ResponseBody
    fun changeHallSize(@RequestBody size: HallSize) {
        hallService.updateHallSize(size)
    }

    @DeleteMapping
    @Hidden
    @ResponseBody
    fun deleteAll() {
        hallService.deleteAll()
    }
}