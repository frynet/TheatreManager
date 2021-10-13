package com.frynet.theatre.roles

import org.springframework.stereotype.Service


@Service
class RoleConverter {

    fun toInfo(o: RoleEntity) = RoleInfo(
        o.id,
        o.title
    )

    fun toEntity(o: RoleCreate) = RoleEntity(
        0L,
        o.title
    )

    fun toCreate(o: RoleInfo) = RoleCreate(
        o.title
    )
}