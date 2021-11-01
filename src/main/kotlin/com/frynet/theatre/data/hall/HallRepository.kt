package com.frynet.theatre.data.hall

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
interface HallRepository : CrudRepository<HallEntity, HallId> {

    @Query(
        value = "SELECT max(_row) FROM hall",
        nativeQuery = true
    )
    fun findMaxRow(): Int?

    @Query(
        value = "SELECT max(_column) FROM hall",
        nativeQuery = true
    )
    fun findMaxColumn(): Int?

    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM hall WHERE _row > :rows",
        nativeQuery = true
    )
    fun cutByRows(@Param("rows") rows: Int)

    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM hall WHERE _column > :columns",
        nativeQuery = true
    )
    fun cutByColumns(@Param("columns") columns: Int)
}