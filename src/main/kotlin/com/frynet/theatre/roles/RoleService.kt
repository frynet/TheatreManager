package com.frynet.theatre.roles

import com.frynet.theatre.errors.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class RoleService {

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var roleConverter: RoleConverter

    private fun getIfExists(id: Long): RoleEntity {
        val role = roleRepository.findById(id)

        if (role.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Role.notFound(id))
        }

        return role.get()
    }

    fun getAllRoles(): List<RoleInfo> {
        return roleRepository.findAll().map { roleConverter.toInfo(it) }
    }

    fun getRoleById(id: Long): RoleInfo {
        return roleConverter.toInfo(getIfExists(id))
    }

    fun addRole(role: RoleCreate): RoleInfo {
        if (roleRepository.existsByTitle(role.title)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Role.alreadyExist(role.title))
        }

        return roleConverter.toInfo(
            roleRepository.save(roleConverter.toEntity(role))
        )
    }

    fun deleteRoleById(id: Long) {
        roleRepository.delete(getIfExists(id))
    }

    fun deleteAll() {
        roleRepository.deleteAll()
    }

    fun updateRole(id: Long, role: RoleCreate): RoleInfo {
        val r = getIfExists(id)

        r.title = role.title

        return roleConverter.toInfo(roleRepository.save(r))
    }
}