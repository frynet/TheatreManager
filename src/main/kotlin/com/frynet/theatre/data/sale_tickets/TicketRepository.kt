package com.frynet.theatre.data.sale_tickets

import com.frynet.theatre.data.repertoires.RepertoireDateInterval
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate


@Repository
interface TicketRepository : CrudRepository<TicketEntity, Long> {

    fun findAllBySpecId(specId: Long): List<TicketEntity>

    fun findAllByDate(date: LocalDate): List<TicketEntity>

    @Query(
        value = "SELECT * FROM sale_tickets WHERE _date >= :#{#interval.begin} AND _date <= :#{#interval.end}",
        nativeQuery = true
    )
    fun findAllByDateInterval(interval: RepertoireDateInterval): List<TicketEntity>

    @Query(
        value = """
            SELECT *
            FROM sale_tickets
            WHERE id_spec = :#{#constraint.specId}
              AND _date = :#{#constraint.date}
              AND _row = :#{#constraint.row}
              AND _col = :#{#constraint.column}
        """,
        nativeQuery = true
    )
    fun findByUniqueK10(constraint: TicketUniqueK10): TicketEntity?
}