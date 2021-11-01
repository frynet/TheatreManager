package com.frynet.theatre.data.spectacles_roles

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "spectacles_roles")
data class SpecRoleEntity(

    @Id
    var id: SpecRoleId,

    var main: Boolean,
)
