package com.frynet.theatre.spectacles

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@Tag(name = "spectacles")
@RequestMapping(path = ["/spectacles"])
class SpectacleController {

    @Autowired
    private lateinit var spectacleService: SpectacleService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "get all spectacles")
    @ResponseBody
    fun getAllSpectacles(): List<SpectacleInfo> {
        return spectacleService.getAll()
    }

    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "get spectacle by ID")
    @ResponseBody
    fun getSpectacleById(@PathVariable id: Long): SpectacleInfo {
        return spectacleService.getById(id)
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "add new spectacle")
    @ResponseBody
    fun addSpectacle(@RequestBody spectacle: SpectacleCreate): SpectacleInfo {
        return spectacleService.add(spectacle)
    }

    @PutMapping("/{id}")
    @Operation(summary = "update spectacle by ID")
    @ResponseBody
    fun updateSpectacleById(
        @PathVariable id: Long,
        @RequestBody spectacle: SpectacleCreate
    ): SpectacleInfo {
        return spectacleService.updateById(id, spectacle)
    }

    @DeleteMapping
    @Operation(summary = "delete all spectacles")
    @ResponseBody
    fun deleteAll() {
        spectacleService.deleteAll()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete spectacle by ID")
    @ResponseBody
    fun deleteSpectacleById(@PathVariable id: Long) {
        spectacleService.deleteById(id)
    }
}