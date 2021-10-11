package clients

import com.frynet.theatre.actors.ActorCreate
import com.frynet.theatre.actors.ActorInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@FeignClient(
    name = "actors",
    url = "\${endpoint.url}"
)
@RequestMapping("/actors")
interface ActorClient {

    @GetMapping
    fun getAllActors(): List<ActorInfo>

    @GetMapping("/{id}")
    fun getActorById(@PathVariable id: Long): ActorInfo

    @PostMapping
    fun addActor(@RequestBody actor: ActorCreate): ActorInfo

    @DeleteMapping
    fun deleteAllActors()

    @DeleteMapping("/{id}")
    fun deleteActor(@PathVariable id: Long)
}