package com.frynet.theatre.data.sale_tickets

import com.frynet.theatre.data.repertoires.RepertoireDate
import com.frynet.theatre.data.repertoires.RepertoireDateInterval
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@Controller
@Tag(name = "tickets (sale history)")
@RequestMapping(value = ["/tickets"])
class TicketController {

    @Autowired
    private lateinit var ticketService: TicketService

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "добавить билет (продать)")
    @ResponseBody
    fun addTicket(@RequestBody ticket: TicketCreate): TicketInfo {
        return ticketService.addTicket(ticket)
    }

    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "информация по билету")
    @ResponseBody
    fun getTicketInfo(@PathVariable("id") ticketId: Long): TicketInfo {
        return ticketService.getTicketInfo(ticketId)
    }

    @GetMapping(
        path = ["/spectacle/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "все проданные билеты (по спектаклю)")
    @ResponseBody
    fun getTicketsBySpectacle(@PathVariable("id") specId: Long): List<TicketInfo> {
        return ticketService.getTicketsBySpectacle(specId)
    }

    @PostMapping(
        path = ["/date"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "все проданные билеты (по дате)")
    @ResponseBody
    fun getTicketsByDate(@RequestBody date: RepertoireDate): List<TicketInfo> {
        return ticketService.getTicketsByDate(date)
    }

    @PostMapping(
        path = ["/date/interval"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "все проданные билеты (по временному промежутку)")
    @ResponseBody
    fun getTicketsByInterval(@RequestBody interval: RepertoireDateInterval): Map<LocalDate, List<TicketInfo>> {
        return ticketService.getTicketsByDateInterval(interval)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "вся история продаж")
    @ResponseBody
    fun getAll(): List<TicketInfo> {
        return ticketService.getAll()
    }

    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "поменять билет")
    @ResponseBody
    fun updateTicket(
        @PathVariable("id") ticketId: Long,
        @RequestBody ticket: TicketCreate
    ): TicketInfo {
        return ticketService.updateTicket(ticketId, ticket)
    }

    @DeleteMapping(path = ["/{id}"])
    @Operation(summary = "вернуть билет обратно")
    @ResponseBody
    fun deleteTicket(@PathVariable("id") ticketId: Long) {
        ticketService.deleteTicket(ticketId)
    }

    @DeleteMapping
    @Hidden
    @ResponseBody
    fun deleteAll() {
        ticketService.deleteAll()
    }
}