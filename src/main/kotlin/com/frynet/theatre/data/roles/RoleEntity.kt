package com.frynet.theatre.data.roles

import javax.persistence.*


@Entity
@Table(name = "roless")
data class RoleEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var title: String,
)
