package com.frynet.theatre.repertoires

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate


@Repository
interface RepertoireRepository : CrudRepository<RepertoireEntity, RepertoireId> {

    @Query(
        value = "SELECT * FROM repertoires WHERE _date = :date",
        nativeQuery = true
    )
    fun findByDate(@Param("date") date: LocalDate): List<RepertoireEntity>


    @Query(
        value = "SELECT * FROM repertoires WHERE _date >= :begin AND _date <= :end",
        nativeQuery = true
    )
    fun findByInterval(
        @Param("begin") begin: LocalDate,
        @Param("end") end: LocalDate
    ): List<RepertoireEntity>
}