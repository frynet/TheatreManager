package com.frynet.theatre.repertoires

import java.time.LocalDate

data class RepertoireInfo(

    var specId: Long,

    var date: LocalDate,

    var price: Int,
)
