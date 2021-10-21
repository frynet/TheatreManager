package com.frynet.theatre.sale_tickets

import com.frynet.theatre.hall.HallPlace
import com.frynet.theatre.repertoires.RepertoireInfo


data class TicketInfo(

    var id: Long,

    var repertoireInfo: RepertoireInfo,

    var place: HallPlace,
)
