package com.frynet.theatre.data.sale_tickets

import com.frynet.theatre.data.hall.HallPlace
import java.time.LocalDate


data class TicketCreate(

    var specId: Long,

    var date: LocalDate,

    var place: HallPlace,
)
