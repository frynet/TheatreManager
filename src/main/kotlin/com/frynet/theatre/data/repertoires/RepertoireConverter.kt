package com.frynet.theatre.data.repertoires

import org.springframework.stereotype.Service


@Service
class RepertoireConverter {

    fun toInfo(o: RepertoireEntity) = RepertoireInfo(
        o.id.spectacle,
        o.id.date,
        o.price
    )

    fun toEntity(o: RepertoireInfo) = RepertoireEntity(
        RepertoireId(o.specId, o.date), o.price
    )
}