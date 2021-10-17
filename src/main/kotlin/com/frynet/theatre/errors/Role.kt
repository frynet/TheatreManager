package com.frynet.theatre.errors

object Role {

    fun notFound(id: Long) = "The role with id=$id not found"

    fun alreadyExist(title: String) = "The role with title $title already exist"
}