package com.frynet.theatre.data.spectacles_actors

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
data class SpecActorId(

    @Column(name = "id_spec")
    var spectacle: Long,

    @Column(name = "id_actor")
    var actor: Long,
) : Serializable
