package clients

import com.frynet.theatre.data.spectacles.SpectacleCreate
import com.frynet.theatre.data.spectacles.SpectacleInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@FeignClient(
    name = "spectacles",
    url = "\${endpoint.url}"
)
@RequestMapping("/spectacles")
interface SpectacleClient {

    @GetMapping
    fun getAllSpectacles(): List<SpectacleInfo>

    @GetMapping("/{id}")
    fun getSpectacleById(@PathVariable id: Long): SpectacleInfo

    @PostMapping
    fun addSpectacle(spec: SpectacleCreate): SpectacleInfo

    @PutMapping("/{id}")
    fun updateSpectacleById(@PathVariable id: Long, spec: SpectacleCreate): SpectacleInfo

    @DeleteMapping
    fun deleteAll()

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long)
}