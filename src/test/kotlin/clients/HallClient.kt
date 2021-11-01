package clients

import com.frynet.theatre.data.hall.HallPlace
import com.frynet.theatre.data.hall.HallSize
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@FeignClient(
    name = "hall",
    url = "\${endpoint.url}"
)
@RequestMapping("/hall")
interface HallClient {

    @PostMapping("/contains")
    fun contains(@RequestBody place: HallPlace): Boolean

    @GetMapping("/size")
    fun getHallSize(): HallSize

    @PutMapping
    fun changeHallSize(@RequestBody size: HallSize)

    @DeleteMapping
    fun deleteAll()
}