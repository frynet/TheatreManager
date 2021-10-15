package clients

import com.frynet.theatre.roles.RoleInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@FeignClient(
    name = "SpecRoleActor",
    url = "\${endpoint.url}"
)
@RequestMapping("/spectacles/{id}")
interface SpecRoleActorClient {

    @GetMapping("/actors/{actor_id}/roles")
    fun getRolesByActor(
        @PathVariable("id") specId: Long,
        @PathVariable("actor_id") actorId: Long
    ): List<Long>

    @GetMapping("/roles/{role_id}/actors")
    fun getActorsByRole(
        @PathVariable("id") specId: Long,
        @PathVariable("role_id") roleId: Long
    ): List<Long>

    @PutMapping("/actors/{actor_id}/roles/{role_id}")
    fun changeRoleForActor(
        @PathVariable("id") specId: Long,
        @PathVariable("role_id") roleId: Long,
        @PathVariable("actor_id") actor_id: Long,
        @RequestBody role: RoleInfo
    )

    @PostMapping("/actors/{actor_id}/roles/")
    fun setRolesForActor(
        @PathVariable("id") specId: Long,
        @PathVariable("actor_id") actorId: Long,
        @RequestBody roles: List<RoleInfo>
    ): List<Long>

    @DeleteMapping("/actors/{actor_id}/roles")
    fun releaseActorFromRoles(
        @PathVariable("id") specId: Long,
        @PathVariable("actor_id") actorId: Long,
        @RequestBody roles: List<RoleInfo>
    )

    @DeleteMapping("/roles/actors")
    fun deleteAll(@PathVariable("id") specId: Long = 0)
}