package clients

import com.frynet.theatre.data.repertoires.RepertoireDate
import com.frynet.theatre.data.repertoires.RepertoireDateInterval
import com.frynet.theatre.data.repertoires.RepertoireInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDate


@FeignClient(
    name = "repertoires",
    url = "\${endpoint.url}"
)
@RequestMapping("/repertoires")
interface RepertoireClient {

    @PostMapping("/date")
    fun getSpectaclesByDate(@RequestBody date: RepertoireDate): List<RepertoireInfo>

    @PostMapping("/interval")
    fun getSpectaclesByInterval(@RequestBody interval: RepertoireDateInterval): Map<LocalDate, List<RepertoireInfo>>

    @PostMapping("/scheduled")
    fun isScheduled(@RequestBody info: RepertoireInfo): Boolean

    @PostMapping
    fun scheduleSpectacle(@RequestBody info: RepertoireInfo)

    @DeleteMapping
    fun cancelSpectacle(@RequestBody info: RepertoireInfo)

    @DeleteMapping("/all")
    fun deleteAll()
}