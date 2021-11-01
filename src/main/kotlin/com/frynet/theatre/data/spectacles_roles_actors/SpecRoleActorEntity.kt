package com.frynet.theatre.data.spectacles_roles_actors

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table


@Entity
@Table(name = "spectacles_roles_actors")
data class SpecRoleActorEntity(

    @EmbeddedId
    var id: SpecRoleActorId,
)
