package com.frynet.theatre.data.sale_tickets

import java.time.LocalDate

data class TicketUniqueK10(

    var specId: Long,

    var date: LocalDate,

    var row: Int,

    var column: Int,
)
