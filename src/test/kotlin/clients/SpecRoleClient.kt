package clients

import com.frynet.theatre.data.spectacles_roles.SpecRoleInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@FeignClient(
    name = "spectaclesRoles",
    url = "\${endpoint.url}"
)
@RequestMapping("/spectacles/{id}/roles")
interface SpecRoleClient {

    @GetMapping
    fun getAllRolesInSpec(@PathVariable(name = "id") specId: Long): List<SpecRoleInfo>

    @PostMapping
    fun addRoleToSpec(
        @PathVariable(name = "id") specId: Long,
        @RequestBody role: SpecRoleInfo
    )

    @GetMapping("/{id_role}")
    fun getRoleInfo(
        @PathVariable(name = "id") specId: Long,
        @PathVariable(name = "id_role") roleId: Long
    ): SpecRoleInfo

    @PutMapping
    fun updateRoleInfo(
        @PathVariable(name = "id") specId: Long,
        @RequestBody role: SpecRoleInfo
    )

    @DeleteMapping("/{id_role}")
    fun deleteRoleFromSpec(
        @PathVariable(name = "id") specId: Long,
        @PathVariable(name = "id_role") roleId: Long
    )

    @DeleteMapping
    fun deleteAll(@PathVariable(name = "id") specId: Long = 0)
}