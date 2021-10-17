package com.frynet.theatre.errors

object Spectacle {

    fun notFound(id: Long) = "The spectacle with id=$id not found"

    fun alreadyExist(title: String) = "The spectacle with title $title already exists"
}