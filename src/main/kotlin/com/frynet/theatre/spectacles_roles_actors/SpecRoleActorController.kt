package com.frynet.theatre.spectacles_roles_actors

import com.frynet.theatre.roles.RoleInfo
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@Tag(name = "spectacles, roles and actors")
@RequestMapping(path = ["/spectacles/{id}"])
class SpecRoleActorController {

    @Autowired
    private lateinit var specRoleActorService: SpecRoleActorService

    @GetMapping(
        path = ["/actors/{actor_id}/roles"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "роли, которые исполняет актёр в спектакле")
    @ResponseBody
    fun getRolesByActors(
        @PathVariable("id") specId: Long,
        @PathVariable("actor_id") actorId: Long
    ): List<Long> {
        return specRoleActorService.getRolesByActor(specId, actorId)
    }

    @GetMapping(
        path = ["/roles/{role_id}/actors"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "актёры, которые исполняют определённую роль в спектакле")
    @ResponseBody
    fun getActorsByRole(
        @PathVariable("id") specId: Long,
        @PathVariable("role_id") roleId: Long
    ): List<Long> {
        return specRoleActorService.getActorsByRole(specId, roleId)
    }

    @PutMapping(
        path = ["/actors/{actor_id}/roles/{role_id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "поменять роль у актёра")
    @ResponseBody
    fun changeRoleForActor(
        @PathVariable("id") specId: Long,
        @PathVariable("role_id") roleId: Long,
        @PathVariable("actor_id") actor_id: Long,
        @RequestBody role: RoleInfo
    ) {
        specRoleActorService.changeRoleForActor(specId, roleId, actor_id, role)
    }

    @PostMapping(
        path = ["/actors/{actor_id}/roles/"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "назначить актёру список ролей")
    @ResponseBody
    fun setRolesForActor(
        @PathVariable("id") specId: Long,
        @PathVariable("actor_id") actorId: Long,
        @RequestBody roles: List<RoleInfo>
    ): List<Long> {
        return specRoleActorService.setRolesForActor(specId, actorId, roles)
    }

    @DeleteMapping(
        path = ["/actors/{actor_id}/roles"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "снять актёра с ролей")
    @ResponseBody
    fun releaseActorFromRoles(
        @PathVariable("id") specId: Long,
        @PathVariable("actor_id") actorId: Long,
        @RequestBody roles: List<RoleInfo>
    ) {
        specRoleActorService.removeRolesFromActor(specId, actorId, roles)
    }

    @DeleteMapping("/roles/actors")
    @Hidden
    @ResponseBody
    fun deleteAll(@PathVariable("id") specId: Long = 0) {
        specRoleActorService.deleteAll()
    }
}