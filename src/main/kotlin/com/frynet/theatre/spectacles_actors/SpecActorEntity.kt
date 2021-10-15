package com.frynet.theatre.spectacles_actors

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table(name = "spectacles_actors")
data class SpecActorEntity(

    @EmbeddedId
    var id: SpecActorId,
)
