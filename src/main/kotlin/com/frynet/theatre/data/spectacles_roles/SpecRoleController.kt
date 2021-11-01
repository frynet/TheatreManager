package com.frynet.theatre.data.spectacles_roles

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@Tag(name = "Spectacles and roles")
@RequestMapping(path = ["/spectacles/{id}/roles"])
class SpecRoleController {

    @Autowired
    private lateinit var specRoleService: SpecRoleService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "все роли в спектакле")
    @ResponseBody
    fun getAllRolesInSpec(
        @PathVariable(name = "id") specId: Long
    ): List<SpecRoleInfo> {
        return specRoleService.getAllRolesBySpec(specId)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "добавить роль в спектакль")
    @ResponseBody
    fun addRoleToSpec(
        @PathVariable(name = "id") specId: Long,
        @RequestBody role: SpecRoleInfo
    ) {
        specRoleService.addRoleToSpectacle(specId, role)
    }

    @GetMapping(
        path = ["/{id_role}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "информация о роли в спектакле")
    @ResponseBody
    fun getRoleInfo(
        @PathVariable(name = "id") specId: Long,
        @PathVariable(name = "id_role") roleId: Long
    ): SpecRoleInfo {
        return specRoleService.getRoleInfo(specId, roleId)
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "обновить информацию о роли в спектакле")
    @ResponseBody
    fun updateRoleInfo(
        @PathVariable(name = "id") specId: Long,
        @RequestBody role: SpecRoleInfo
    ) {
        specRoleService.updateRoleInfo(specId, role)
    }

    @DeleteMapping("/{id_role}")
    @Operation(summary = "убрать роль из спектакля")
    @ResponseBody
    fun deleteRoleFromSpec(
        @PathVariable(name = "id") specId: Long,
        @PathVariable(name = "id_role") roleId: Long
    ) {
        specRoleService.deleteRoleFromSpec(specId, roleId)
    }

    @DeleteMapping
    @Operation(summary = "удалить все записи в таблице")
    @ResponseBody
    fun deleteAll(@PathVariable id: Long = 0) {
        specRoleService.deleteAll()
    }
}