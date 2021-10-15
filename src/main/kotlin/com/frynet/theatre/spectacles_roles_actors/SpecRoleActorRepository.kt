package com.frynet.theatre.spectacles_roles_actors

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface SpecRoleActorRepository : CrudRepository<SpecRoleActorEntity, SpecRoleActorId> {

    @Query(
        value = "SELECT * FROM spectacles_roles_actors WHERE id_spec = :spec AND id_role = :role",
        nativeQuery = true
    )
    fun findActorsByRole(
        @Param("spec") specId: Long,
        @Param("role") roleId: Long
    ): List<SpecRoleActorEntity>

    @Query(
        value = "SELECT * FROM spectacles_roles_actors WHERE id_spec = :spec AND id_actor = :actor",
        nativeQuery = true
    )
    fun findRolesByActors(
        @Param("spec") specId: Long,
        @Param("actor") actorId: Long
    ): List<SpecRoleActorEntity>
}