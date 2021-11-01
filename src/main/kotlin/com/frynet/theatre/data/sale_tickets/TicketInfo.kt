package com.frynet.theatre.data.sale_tickets

import com.frynet.theatre.data.hall.HallPlace
import com.frynet.theatre.data.repertoires.RepertoireInfo


data class TicketInfo(

    var id: Long,

    var repertoireInfo: RepertoireInfo,

    var place: HallPlace,
)
