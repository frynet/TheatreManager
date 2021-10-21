package com.frynet.theatre.sale_tickets

import com.frynet.theatre.hall.HallPlace
import java.time.LocalDate


data class TicketCreate(

    var specId: Long,

    var date: LocalDate,

    var place: HallPlace,
)
