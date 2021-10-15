package clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@FeignClient(
    name = "SpectaclesActors",
    url = "\${endpoint.url}"
)
@RequestMapping("/spectacles/{id}/actors")
interface SpecActorClient {

    @GetMapping
    fun getActorsInSpec(@PathVariable("id") specId: Long): List<Long>

    @PostMapping
    fun addActorsToSpec(
        @PathVariable("id") specId: Long,
        @RequestBody actors: List<Long>
    ): List<Long>

    @DeleteMapping("/{actor_id}")
    fun deleteActorFromSpec(
        @PathVariable("id") specId: Long,
        @PathVariable("actor_id") actorId: Long
    )

    @DeleteMapping
    fun deleteAll(@PathVariable("id") specId: Long = 0)
}