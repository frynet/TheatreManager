package com.frynet.theatre.hall

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class HallService {

    @Autowired
    private lateinit var hallRepository: HallRepository

    @Autowired
    private lateinit var hallConverter: HallConverter

    private fun contains(place: HallPlace, size: HallSize): Boolean {
        if (size.rows == 0 || size.columns == 0) return false

        return place.row in (1..size.rows) && place.column in (1..size.columns)
    }

    private fun notContains(place: HallPlace, size: HallSize): Boolean {
        return !contains(place, size)
    }

    private fun expandHall(size: HallSize, curSize: HallSize) {
        val place = HallPlace(0, 0)

        for (i in 1..size.rows) {
            for (j in 1..size.columns) {
                place.row = i
                place.column = j

                if (notContains(place, curSize)) {
                    hallRepository.save(hallConverter.toEntity(place))
                }
            }
        }
    }

    fun getHallSize(): HallSize {
        return HallSize(
            hallRepository.findMaxRow() ?: 0,
            hallRepository.findMaxColumn() ?: 0
        )
    }

    fun updateHallSize(size: HallSize) {
        val curSize = getHallSize()

        if (size == curSize) return

        if (size.rows == 0 || size.columns == 0) {
            deleteAll()

            return
        }

        if (size.rows < curSize.rows) {
            hallRepository.cutByRows(size.rows)

            curSize.rows = size.rows
        }

        if (size.columns < curSize.rows) {
            hallRepository.cutByColumns(size.columns)

            curSize.columns = size.columns
        }

        if (size.rows > curSize.rows || size.columns > curSize.columns) {
            expandHall(size, curSize)
        }
    }

    fun deleteAll() {
        hallRepository.deleteAll()
    }
}

