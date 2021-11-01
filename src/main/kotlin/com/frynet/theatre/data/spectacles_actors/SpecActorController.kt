package com.frynet.theatre.data.spectacles_actors

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@Tag(name = "spectacles and actors")
@RequestMapping(path = ["/spectacles/{id}/actors"])
class SpecActorController {

    @Autowired
    private lateinit var specActorService: SpecActorService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "все актёры, участвующие в спектакле")
    @ResponseBody
    fun getActorsInSpec(@PathVariable("id") specId: Long): List<Long> {
        return specActorService.getActorsInSpec(specId)
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "добавить актёров в спектакль")
    @ResponseBody
    fun addActorsToSpec(
        @PathVariable("id") specId: Long,
        @RequestBody actors: List<Long>
    ): List<Long> {
        return specActorService.addActorsToSpec(specId, actors)
    }

    @DeleteMapping(path = ["/{actor_id}"])
    @Operation(summary = "удалить актёра из спектакля")
    @ResponseBody
    fun deleteActorFromSpec(
        @PathVariable("id") specId: Long,
        @PathVariable("actor_id") actorId: Long
    ) {
        specActorService.deleteActorFromSpec(specId, actorId)
    }

    @DeleteMapping
    @ResponseBody
    @Hidden
    fun deleteAll(@PathVariable("id") specId: Long = 0) {
        specActorService.deleteAll()
    }
}