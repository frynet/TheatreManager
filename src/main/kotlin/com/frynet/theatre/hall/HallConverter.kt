package com.frynet.theatre.hall

import org.springframework.stereotype.Service


@Service
class HallConverter {

    fun toEntity(o: HallPlace) = HallEntity(
        HallId(o.row, o.column)
    )
}