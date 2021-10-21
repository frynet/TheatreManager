package com.frynet.theatre.sale_tickets

import com.frynet.theatre.errors.Hall
import com.frynet.theatre.errors.Ticket
import com.frynet.theatre.hall.HallService
import com.frynet.theatre.repertoires.RepertoireDate
import com.frynet.theatre.repertoires.RepertoireDateInterval
import com.frynet.theatre.repertoires.RepertoireInfo
import com.frynet.theatre.repertoires.RepertoireService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate


@Service
class TicketService {

    @Autowired
    private lateinit var ticketRepository: TicketRepository

    @Autowired
    private lateinit var ticketConverter: TicketConverter

    @Autowired
    private lateinit var hallService: HallService

    @Autowired
    private lateinit var repertoireService: RepertoireService

    private fun getIfExists(id: Long): TicketEntity {
        val t = ticketRepository.findById(id)

        if (t.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Ticket.notFound(id))
        }

        return t.get()
    }

    private fun checkUniqueK10(ticket: TicketCreate) {
        if (ticketRepository.findByUniqueK10(ticketConverter.toUniqueK10(ticket)) != null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Ticket.alreadyExist(ticket.place))
        }
    }

    fun addTicket(ticket: TicketCreate): TicketInfo {
        if (!hallService.contains(ticket.place)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Hall.notContained(ticket.place))
        }

        repertoireService.isScheduled(RepertoireInfo(ticket.specId, ticket.date))

        checkUniqueK10(ticket)

        return ticketConverter.toInfo(ticketRepository.save(ticketConverter.toEntity(ticket)))
    }

    fun getTicketInfo(id: Long): TicketInfo {
        return ticketConverter.toInfo(getIfExists(id))
    }

    fun getTicketsBySpectacle(specId: Long): List<TicketInfo> {
        return ticketRepository.findAllBySpecId(specId).map { ticketConverter.toInfo(it) }
    }

    fun getTicketsByDate(date: RepertoireDate): List<TicketInfo> {
        return ticketRepository.findAllByDate(date.date).map { ticketConverter.toInfo(it) }
    }

    fun getTicketsByDateInterval(interval: RepertoireDateInterval): Map<LocalDate, List<TicketInfo>> {
        repertoireService.checkInterval(interval)

        return ticketRepository
            .findAllByDateInterval(interval)
            .map { ticketConverter.toInfo(it) }
            .groupBy { it.repertoireInfo.date }
            .toSortedMap()
    }

    fun getAll(): List<TicketInfo> {
        return ticketRepository.findAll().map { ticketConverter.toInfo(it) }
    }

    fun updateTicket(id: Long, ticket: TicketCreate): TicketInfo {
        val entity = getIfExists(id)
        val e = ticketConverter.toEntity(ticket)

        e.id = entity.id

        return ticketConverter.toInfo(ticketRepository.save(e))
    }

    fun deleteTicket(id: Long) {
        ticketRepository.delete(getIfExists(id))
    }

    fun deleteAll() {
        ticketRepository.deleteAll()
    }
}