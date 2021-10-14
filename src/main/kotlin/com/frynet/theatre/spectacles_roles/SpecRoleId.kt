package com.frynet.theatre.spectacles_roles

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
data class SpecRoleId(

    @Column(name = "id_spec")
    var spectacle: Long,

    @Column(name = "id_role")
    var role: Long,
) : Serializable
