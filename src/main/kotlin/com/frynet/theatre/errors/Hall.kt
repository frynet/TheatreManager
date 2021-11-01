package com.frynet.theatre.errors

import com.frynet.theatre.data.hall.HallPlace

object Hall {

    fun notContained(place: HallPlace) = "Hall doesn't contain a place(${place.row}, ${place.column})."
}