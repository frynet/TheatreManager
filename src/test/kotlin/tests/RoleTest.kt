package tests

import clients.RoleClient
import com.frynet.theatre.roles.RoleConverter
import com.frynet.theatre.roles.RoleCreate
import com.frynet.theatre.roles.RoleInfo
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
import io.kotest.matchers.shouldNotBe
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
        RoleConverter::class
    ]
)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner::class)
class RoleTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var roleClient: RoleClient

    @Autowired
    private lateinit var roleConverter: RoleConverter

    private lateinit var role: RoleInfo

    init {
        "Try to get non-exist role" {
            val id = 0L

            val ex = shouldThrow<FeignException.BadRequest> {
                roleClient.getRoleById(id)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message?.let {
                it shouldContain "The role with id=$id not found"
            }
        }

        "Add some roles" {
            val roles = Generate.roles(Random.nextInt(4, 10))

            roles.forEach {
                shouldNotThrowAny {
                    roleClient.addRole(it)
                }
            }

            val response = roleClient.getAllRoles()

            response.size shouldBe roles.size
            response.map { roleConverter.toCreate(it) } shouldContainAll roles
        }

        "Get role by ID" {
            role = roleClient.getAllRoles().random()

            val response = shouldNotThrowAny {
                roleClient.getRoleById(role.id)
            }

            response.id shouldBe role.id
            response.title shouldBe role.title
        }

        "Update role by ID" {
            shouldNotThrowAny {
                roleClient.updateRole(role.id, RoleCreate("kek"))
            }

            val response = shouldNotThrowAny {
                roleClient.getRoleById(role.id)
            }

            response.id shouldBe role.id
            response.title shouldNotBe role.title
            response.title shouldBe "kek"
        }

        "Delete role by ID" {
            shouldNotThrowAny {
                roleClient.deleteRoleById(role.id)
            }

            roleClient.getAllRoles() shouldNotContain role
        }
    }

    override fun afterSpec(spec: Spec) {
        roleClient.deleteAllRoles()
    }
}