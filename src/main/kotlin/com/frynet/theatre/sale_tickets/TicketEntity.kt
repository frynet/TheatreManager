package com.frynet.theatre.sale_tickets

import java.time.LocalDate
import javax.persistence.*


@Entity
@Table(
    name = "sale_tickets",
    uniqueConstraints = [
        UniqueConstraint(name = "k10", columnNames = ["id_spec", "_date", "_row", "_col"])
    ]
)
data class TicketEntity(

    @Id
    @Column(name = "id_ticket")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "id_spec")
    var specId: Long,

    @Column(name = "_date")
    var date: LocalDate,

    var price: Int,

    @Column(name = "_row")
    var row: Int,

    @Column(name = "_col")
    var column: Int,
)
