package com.frynet.theatre.errors

import com.frynet.theatre.repertoires.RepertoireDateInterval
import com.frynet.theatre.repertoires.RepertoireInfo

object Repertoire {

    fun invalidDateInterval(interval: RepertoireDateInterval) =
        "Interval can't begins at ${interval.begin} and ends at ${interval.end}."

    fun alreadyScheduled(info: RepertoireInfo) =
        "The spectacle with id=${info.specId} already scheduled at ${info.date}."

    fun notScheduled(info: RepertoireInfo) =
        "The spectacle with id=${info.specId} not scheduled at ${info.date}."
}