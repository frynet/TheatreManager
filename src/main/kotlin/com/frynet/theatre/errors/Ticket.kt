package com.frynet.theatre.errors

import com.frynet.theatre.hall.HallPlace

object Ticket {

    fun notFound(id: Long) = "The ticket with id=$id not found"

    fun alreadyExist(place: HallPlace) = "The place(${place.row}, ${place.column}) already taken"
}