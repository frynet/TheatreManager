package tests

import clients.RoleClient
import clients.SpecRoleClient
import clients.SpectacleClient
import com.frynet.theatre.roles.RoleCreate
import com.frynet.theatre.roles.RoleInfo
import com.frynet.theatre.spectacles.SpectacleCreate
import com.frynet.theatre.spectacles.SpectacleInfo
import com.frynet.theatre.spectacles_roles.SpecRoleInfo
import config.FeignConfiguration
import feign.FeignException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import utils.Generate
import kotlin.random.Random


@SpringBootTest(
    classes = [
        FeignConfiguration::class,
    ]
)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner::class)
class SpecRoleTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var specRoleClient: SpecRoleClient

    @Autowired
    private lateinit var spectacleClient: SpectacleClient

    @Autowired
    private lateinit var roleClient: RoleClient

    private var specId = 0L
    private lateinit var roles: List<RoleInfo>
    private lateinit var spectacles: List<SpectacleInfo>

    override fun beforeSpec(spec: Spec) {
        val count = Random.nextInt(4, 10)

        generateSpectacles(count).forEach(spectacleClient::addSpectacle)
        spectacles = spectacleClient.getAllSpectacles()

        generateRoles(count).forEach(roleClient::addRole)
        roles = roleClient.getAllRoles()
    }

    init {
        "Try to add role when spectacle doesn't exists" {
            val specId = getNotContained(spectacles.map { it.id })

            val ex = shouldThrow<FeignException.BadRequest> {
                specRoleClient.addRoleToSpec(specId, SpecRoleInfo(roles.random().id, true))
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain "The spectacle with id=$specId not found"
        }

        "Try to add role when role doesn't exists" {
            val s = spectacles.random()
            val roleId = getNotContained(roles.map { it.id })
            val info = SpecRoleInfo(roleId, true)

            val ex = shouldThrow<FeignException.BadRequest> {
                specRoleClient.addRoleToSpec(s.id, info)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain "The role with id=$roleId not found"
        }

        "Try to get non-exists record" {
            val specId = 0L
            val roleId = 0L

            val ex = shouldThrow<FeignException.BadRequest> {
                specRoleClient.getRoleInfo(specId, roleId)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain "Not found"
        }

        "Add some records" {
            val size = roles.size
            specId = spectacles.random().id

            for (i in 0 until size) {
                shouldNotThrowAny {
                    specRoleClient.addRoleToSpec(specId, SpecRoleInfo(roles[i].id, Random.nextBoolean()))
                }
            }

            val response = specRoleClient.getAllRolesInSpec(specId)

            response.size shouldBe size
            response.map { it.roleId } shouldContainAll roles.map { it.id }
        }

        "Update role info" {
            val r = roles.random()

            shouldNotThrowAny {
                specRoleClient.updateRoleInfo(specId, SpecRoleInfo(r.id, false))
            }

            val response = shouldNotThrowAny {
                specRoleClient.getRoleInfo(specId, r.id)
            }

            response.roleId shouldBe r.id
            response.main shouldBe false
        }

        "Delete role from spectacle" {
            val role = specRoleClient.getAllRolesInSpec(specId).random()

            shouldNotThrowAny {
                specRoleClient.deleteRoleFromSpec(specId, role.roleId)
            }

            specRoleClient.getAllRolesInSpec(specId) shouldNotContain role
        }
    }

    private fun getNotContained(list: List<Long>): Long {
        var result = list.first()

        while (list.contains(result)) {
            result++
        }

        return result
    }

    private fun generateRoles(count: Int): List<RoleCreate> {
        val result = mutableListOf<RoleCreate>()

        for (i in 0 until count) {
            result.add(RoleCreate(Generate.randomWord(Random.nextInt(4, 10))))
        }

        return result
    }

    private fun generateSpectacles(count: Int): List<SpectacleCreate> {
        val result = mutableListOf<SpectacleCreate>()

        for (i in 0 until count) {
            result.add(SpectacleCreate(Generate.randomWord(Random.nextInt(4, 10))))
        }

        return result
    }

    override fun afterSpec(spec: Spec) {
        roleClient.deleteAllRoles()
        spectacleClient.deleteAll()
        specRoleClient.deleteAll()
    }
}