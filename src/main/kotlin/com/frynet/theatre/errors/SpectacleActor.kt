package com.frynet.theatre.errors

import com.frynet.theatre.spectacles_actors.SpecActorId

object SpectacleActor {

    fun notFound(id: SpecActorId) = "The record with id=$id not found"
}