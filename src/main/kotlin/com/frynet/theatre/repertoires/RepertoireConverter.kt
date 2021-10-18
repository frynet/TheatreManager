package com.frynet.theatre.repertoires

import org.springframework.stereotype.Service


@Service
class RepertoireConverter {

    fun toInfo(o: RepertoireEntity) = RepertoireInfo(
        o.id.spectacle,
        o.id.date
    )

    fun toEntity(o: RepertoireInfo) = RepertoireEntity(
        RepertoireId(o.specId, o.date)
    )
}