package com.frynet.theatre.data.repertoires

import java.time.LocalDate

data class RepertoireInfo(

    var specId: Long,

    var date: LocalDate,

    var price: Int = 0,
)
