package com.frynet.theatre.hall

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
data class HallId(

    @Column(name = "_row")
    var row: Int,

    @Column(name = "_column")
    var column: Int,
) : Serializable
