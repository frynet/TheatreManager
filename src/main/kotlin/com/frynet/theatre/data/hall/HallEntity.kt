package com.frynet.theatre.data.hall

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table(name = "hall")
data class HallEntity(

    @EmbeddedId
    var id: HallId,
)
