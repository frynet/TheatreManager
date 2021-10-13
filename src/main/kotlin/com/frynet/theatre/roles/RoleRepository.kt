package com.frynet.theatre.roles

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface RoleRepository : CrudRepository<RoleEntity, Long> {

    fun existsByTitle(title: String): Boolean
}