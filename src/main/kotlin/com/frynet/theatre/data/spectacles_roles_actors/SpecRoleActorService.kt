package com.frynet.theatre.data.spectacles_roles_actors

import com.frynet.theatre.data.actors.ActorService
import com.frynet.theatre.data.roles.RoleInfo
import com.frynet.theatre.data.roles.RoleService
import com.frynet.theatre.data.spectacles.SpectacleService
import com.frynet.theatre.data.spectacles_actors.SpecActorService
import com.frynet.theatre.data.spectacles_roles.SpecRoleService
import com.frynet.theatre.errors.SpectacleRoleActor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class SpecRoleActorService {

    @Autowired
    private lateinit var specRoleActorRepo: SpecRoleActorRepository

    @Autowired
    private lateinit var actorService: ActorService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var spectacleService: SpectacleService

    @Autowired
    private lateinit var specRoleService: SpecRoleService

    @Autowired
    private lateinit var specActorService: SpecActorService

    fun getRolesByActor(specId: Long, actorId: Long): List<Long> {
        return specRoleActorRepo.findRolesByActors(specId, actorId).map { it.id.role }
    }

    fun getActorsByRole(specId: Long, roleId: Long): List<Long> {
        return specRoleActorRepo.findActorsByRole(specId, roleId).map { it.id.actor }
    }

    fun changeRoleForActor(specId: Long, roleId: Long, actorId: Long, role: RoleInfo) {
        val id = SpecRoleActorId(specId, roleId, actorId)

        if (!specRoleActorRepo.existsById(id)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, SpectacleRoleActor.notFound(id))
        }

        id.role = role.id

        if (specRoleActorRepo.existsById(id)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, SpectacleRoleActor.alreadyExist(id))
        }

        specRoleActorRepo.save(SpecRoleActorEntity(id))
    }

    fun setRolesForActor(specId: Long, actorId: Long, roles: List<RoleInfo>): List<Long> {
        spectacleService.getById(specId)
        actorService.getActorById(actorId)

        val invalid = mutableListOf<Long>()
        val id = SpecRoleActorId(specId, 0L, actorId)

        roles.forEach {
            try {
                roleService.getRoleById(it.id)

                id.role = it.id

                if (specRoleActorRepo.existsById(id)) {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, SpectacleRoleActor.alreadyExist(id))
                }

                specRoleActorRepo.save(SpecRoleActorEntity(id))
            } catch (ex: ResponseStatusException) {
                invalid.add(it.id)
            }
        }

        return invalid
    }

    fun removeRolesFromActor(specId: Long, actorId: Long, roles: List<RoleInfo>) {
        val id = SpecRoleActorId(specId, 0L, actorId)

        roles.forEach {
            id.role = it.id
            specRoleActorRepo.deleteById(id)
        }
    }

    fun deleteAll() {
        specRoleActorRepo.deleteAll()
    }
}