package com.frynet.theatre.roles

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

    fun getAllRoles(): List<RoleInfo> {
        return roleRepository.findAll().map { roleConverter.toInfo(it) }
    }

    fun getRoleById(id: Long): RoleInfo {
        val role = roleRepository.findById(id)

        if (role.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The role with id=$id not found")
        }

        return roleConverter.toInfo(role.get())
    }

    fun addRole(role: RoleCreate): RoleInfo {
        if (roleRepository.existsByTitle(role.title)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The role with title ${role.title} already exist")
        }

        return roleConverter.toInfo(
            roleRepository.save(roleConverter.toEntity(role))
        )
    }

    fun deleteRoleById(id: Long) {
        val role = roleRepository.findById(id)

        if (role.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The role with id=$id not found")
        }

        roleRepository.deleteById(id)
    }

    fun deleteAll() {
        roleRepository.deleteAll()
    }

    fun updateRole(id: Long, role: RoleCreate): RoleInfo {
        val r = roleRepository.findById(id)

        if (r.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The role with id=$id not found")
        }

        r.get().title = role.title

        return roleConverter.toInfo(roleRepository.save(r.get()))
    }
}