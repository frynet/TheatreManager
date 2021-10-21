package com.frynet.theatre.errors

import com.frynet.theatre.hall.HallPlace

object Hall {

    fun notContained(place: HallPlace) = "Hall doesn't contain a place(${place.row}, ${place.column})."
}