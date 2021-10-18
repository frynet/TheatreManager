package com.frynet.theatre.repertoires

import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
data class RepertoireId(

    @Column(name = "id_spec")
    var spectacle: Long,

    @Column(name = "_date")
    var date: LocalDate,
) : Serializable
