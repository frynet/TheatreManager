package com.frynet.theatre.data.repertoires

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table(name = "repertoires")
data class RepertoireEntity(

    @EmbeddedId
    var id: RepertoireId,

    var price: Int,
)