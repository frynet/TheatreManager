package com.frynet.theatre.errors

import com.frynet.theatre.spectacles_roles_actors.SpecRoleActorId

object SpectacleRoleActor {

    fun notFound(id: SpecRoleActorId) = "The record with id=$id doesn't exist"

    fun alreadyExist(id: SpecRoleActorId) = "Actor with id=${id.actor} already have a role with id=${id.role}"
}