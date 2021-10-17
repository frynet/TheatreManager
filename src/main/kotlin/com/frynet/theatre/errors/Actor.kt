package com.frynet.theatre.errors

object Actor {

    fun notFound(id: Long) = "The actor with id=$id not found"

    fun alreadyExist(name: String) = "The actor $name already exist"
}