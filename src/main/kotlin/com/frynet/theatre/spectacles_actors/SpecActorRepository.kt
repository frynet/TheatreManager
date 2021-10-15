package com.frynet.theatre.spectacles_actors

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface SpecActorRepository : CrudRepository<SpecActorEntity, SpecActorId> {

    @Query(
        value = "SELECT * FROM spectacles_actors WHERE id_spec = :id",
        nativeQuery = true
    )
    fun findBySpecId(@Param("id") specId: Long): List<SpecActorEntity>
}