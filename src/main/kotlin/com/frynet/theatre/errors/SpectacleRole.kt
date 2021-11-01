package com.frynet.theatre.errors

import com.frynet.theatre.data.spectacles_roles.SpecRoleId

object SpectacleRole {

    fun notFound(id: SpecRoleId) = "The record with id=$id not found"
}