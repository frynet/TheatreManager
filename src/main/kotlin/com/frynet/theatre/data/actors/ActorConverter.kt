package com.frynet.theatre.data.actors

import org.springframework.stereotype.Service


@Service
class ActorConverter {

    fun toInfo(o: ActorEntity) = ActorInfo(
        o.id,
        o.name
    )

    fun toInfo(o: ActorCreate) = ActorInfo(
        0L,
        o.name
    )

    fun toEntity(o: ActorInfo) = ActorEntity(
        o.id,
        o.name
    )

    fun toEntity(o: ActorCreate) = ActorEntity(
        0L,
        o.name
    )

    fun toCreate(o: ActorInfo) = ActorCreate(
        o.name
    )
}