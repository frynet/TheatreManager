package com.frynet.theatre.data.actors

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Tag(name = "actors")
@Controller
@RequestMapping(value = ["/actors"])
class ActorController {

    @Autowired
    private lateinit var actorService: ActorService

    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "get actor by ID")
    @ResponseBody
    fun getActor(@PathVariable id: Long): ActorInfo {
        return actorService.getActorById(id)
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "add new actor")
    @ResponseBody
    fun addActor(@RequestBody actor: ActorCreate): ActorInfo {
        return actorService.addActor(actor)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "get all actors")
    @ResponseBody
    fun getAllActors(): List<ActorInfo> {
        return actorService.getAllActors()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete actor by ID")
    @ResponseBody
    fun deleteActor(@PathVariable id: Long) {
        actorService.deleteActorById(id)
    }

    @DeleteMapping
    @Operation(summary = "delete all actors")
    @ResponseBody
    fun deleteAllActors() {
        actorService.deleteAll()
    }

    @PutMapping("/{id}")
    @Operation(summary = "update actor by ID")
    @ResponseBody
    fun updateActorById(
        @PathVariable id: Long,
        @RequestBody actor: ActorCreate
    ): ActorInfo {
        return actorService.updateActor(id, actor)
    }
}