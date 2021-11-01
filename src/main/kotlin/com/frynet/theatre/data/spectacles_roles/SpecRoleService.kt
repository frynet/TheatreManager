package com.frynet.theatre.data.spectacles_roles

import com.frynet.theatre.data.roles.RoleService
import com.frynet.theatre.data.spectacles.SpectacleService
import com.frynet.theatre.errors.SpectacleRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class SpecRoleService {

    @Autowired
    private lateinit var specRoleRepo: SpecRoleRepository

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var spectacleService: SpectacleService

    @Autowired
    private lateinit var specRoleConverter: SpecRoleConverter

    private fun getIfExists(id: SpecRoleId): SpecRoleEntity {
        val entity = specRoleRepo.findById(id)

        if (entity.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, SpectacleRole.notFound(id))
        }

        return entity.get()
    }

    fun getAllRolesBySpec(specId: Long): List<SpecRoleInfo> {
        return specRoleRepo.findBySpecId(specId).map { specRoleConverter.toInfo(it) }
    }

    fun addRoleToSpectacle(specId: Long, info: SpecRoleInfo) {
        roleService.getRoleById(info.roleId)
        spectacleService.getById(specId)

        val record = SpecRoleEntity(
            SpecRoleId(specId, info.roleId),
            info.main
        )

        specRoleRepo.save(record)
    }

    fun getRoleInfo(specId: Long, roleId: Long): SpecRoleInfo {
        return specRoleConverter.toInfo(getIfExists(SpecRoleId(specId, roleId)))
    }

    fun updateRoleInfo(specId: Long, info: SpecRoleInfo) {
        val entity = getIfExists(SpecRoleId(specId, info.roleId))

        entity.main = info.main

        specRoleRepo.save(entity)
    }

    fun deleteRoleFromSpec(specId: Long, roleId: Long) {
        specRoleRepo.delete(getIfExists(SpecRoleId(specId, roleId)))
    }

    fun deleteAll() {
        specRoleRepo.deleteAll()
    }
}