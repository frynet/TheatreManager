package clients

import com.frynet.theatre.repertoires.RepertoireDate
import com.frynet.theatre.repertoires.RepertoireDateInterval
import com.frynet.theatre.sale_tickets.TicketCreate
import com.frynet.theatre.sale_tickets.TicketInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@FeignClient(
    name = "tickets",
    url = "\${endpoint.url}"
)
@RequestMapping("/tickets")
interface TicketClient {

    @PostMapping
    fun addTicket(@RequestBody ticket: TicketCreate): TicketInfo

    @GetMapping
    fun getAll(): List<TicketInfo>

    @GetMapping("/{id}")
    fun getTicketInfo(@PathVariable("id") ticketId: Long): TicketInfo

    @PutMapping("/{id}")
    fun updateTicket(
        @PathVariable("id") ticketId: Long,
        @RequestBody ticket: TicketCreate
    ): TicketInfo

    @DeleteMapping("/{id}")
    fun deleteTicket(@PathVariable("id") ticketId: Long)

    @GetMapping("/spectacle/{id}")
    fun getTicketsBySpectacle(@PathVariable("id") specId: Long): List<TicketInfo>

    @PostMapping("/date")
    fun getTicketsByDate(@RequestBody date: RepertoireDate): List<TicketInfo>

    @PostMapping("/date/interval")
    fun getTicketsByInterval(@RequestBody interval: RepertoireDateInterval): Map<LocalDate, List<TicketInfo>>

    @DeleteMapping
    fun deleteAll()
}