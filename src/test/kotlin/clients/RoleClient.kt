package clients

import com.frynet.theatre.roles.RoleCreate
import com.frynet.theatre.roles.RoleInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(
    name = "roles",
    url = "\${endpoint.url}"
)
@RequestMapping("/roles")
interface RoleClient {

    @GetMapping
    fun getAllRoles(): List<RoleInfo>

    @GetMapping("/{id}")
    fun getRoleById(@PathVariable id: Long): RoleInfo

    @PostMapping
    fun addRole(@RequestBody role: RoleCreate): RoleInfo

    @PutMapping("/{id}")
    fun updateRole(@PathVariable id: Long, role: RoleCreate): RoleInfo

    @DeleteMapping
    fun deleteAllRoles()

    @DeleteMapping("/{id}")
    fun deleteRoleById(@PathVariable id: Long)
}