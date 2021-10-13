package com.frynet.theatre.roles

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Tag(name = "roles")
@Controller
@RequestMapping(value = ["/roles"])
class RoleController {

    @Autowired
    private lateinit var roleService: RoleService

    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "get role by ID")
    @ResponseBody
    fun getRoleById(@PathVariable id: Long): RoleInfo {
        return roleService.getRoleById(id)
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "add new role")
    @ResponseBody
    fun addRole(@RequestBody role: RoleCreate): RoleInfo {
        return roleService.addRole(role)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "get all roles")
    @ResponseBody
    fun getAllRoles(): List<RoleInfo> {
        return roleService.getAllRoles()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete role by ID")
    @ResponseBody
    fun deleteRoleById(@PathVariable id: Long) {
        roleService.deleteRoleById(id)
    }

    @DeleteMapping
    @Operation(summary = "delete all roles")
    @ResponseBody
    fun deleteAllRoles() {
        roleService.deleteAll()
    }

    @PutMapping("/{id}")
    @Operation(summary = "update role by ID")
    @ResponseBody
    fun updateRoleById(
        @PathVariable id: Long,
        @RequestBody role: RoleCreate
    ): RoleInfo {
        return roleService.updateRole(id, role)
    }
}