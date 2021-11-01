package com.frynet.theatre.data.sale_tickets

import com.frynet.theatre.data.hall.HallPlace
import com.frynet.theatre.data.repertoires.RepertoireInfo
import org.springframework.stereotype.Service


@Service
class TicketConverter {

    fun toInfo(o: TicketEntity) = TicketInfo(
        o.id,
        RepertoireInfo(o.specId, o.date, o.price),
        HallPlace(o.row, o.column)
    )

    fun toInfo(o: TicketCreate) = TicketInfo(
        0L,
        RepertoireInfo(o.specId, o.date, 0),
        o.place
    )

    fun toEntity(o: TicketCreate) = TicketEntity(
        0L,
        o.specId,
        o.date,
        0,
        o.place.row,
        o.place.column
    )

    fun toEntity(o: TicketInfo) = TicketEntity(
        0L,
        o.repertoireInfo.specId,
        o.repertoireInfo.date,
        o.repertoireInfo.price,
        o.place.row,
        o.place.column
    )

    fun toUniqueK10(o: TicketCreate) = TicketUniqueK10(
        o.specId,
        o.date,
        o.place.row,
        o.place.column
    )

    fun toCreate(o: TicketInfo) = TicketCreate(
        o.repertoireInfo.specId,
        o.repertoireInfo.date,
        o.place
    )
}