package com.frynet.theatre.data.spectacles

import javax.persistence.*


@Entity
@Table(name = "spectacles")
data class SpectacleEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var title: String,
)
