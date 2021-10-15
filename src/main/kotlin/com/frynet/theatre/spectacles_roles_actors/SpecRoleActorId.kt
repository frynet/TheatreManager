package com.frynet.theatre.spectacles_roles_actors

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
data class SpecRoleActorId(

    @Column(name = "id_spec")
    var spectacle: Long,

    @Column(name = "id_role")
    var role: Long,

    @Column(name = "id_actor")
    var actor: Long,
) : Serializable
