package com.frynet.theatre.data.actors

import javax.persistence.*


@Entity
@Table(name = "actors")
data class ActorEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var name: String,
)
