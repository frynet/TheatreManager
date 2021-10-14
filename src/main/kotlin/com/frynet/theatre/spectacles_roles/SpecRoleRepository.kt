package com.frynet.theatre.spectacles_roles

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface SpecRoleRepository : CrudRepository<SpecRoleEntity, SpecRoleId> {

    @Query(
        value = "SELECT * FROM spectacles_roles WHERE id_spec = :id",
        nativeQuery = true
    )
    fun findBySpecId(@Param("id") specId: Long): List<SpecRoleEntity>
}