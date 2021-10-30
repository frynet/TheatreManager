package tests

import clients.*
import com.frynet.theatre.actors.ActorInfo
import com.frynet.theatre.errors.Actor
import com.frynet.theatre.errors.Spectacle
import com.frynet.theatre.roles.RoleInfo
import com.frynet.theatre.spectacles.SpectacleInfo
import com.frynet.theatre.spectacles_roles.SpecRoleInfo
import config.FeignConfiguration
import feign.FeignException.BadRequest
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
class SpecRoleActorTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var roleClient: RoleClient

    @Autowired
    private lateinit var spectacleClient: SpectacleClient

    @Autowired
    private lateinit var actorClient: ActorClient

    @Autowired
    private lateinit var specRoleActorClient: SpecRoleActorClient

    @Autowired
    private lateinit var specRoleClient: SpecRoleClient

    @Autowired
    private lateinit var specActorClient: SpecActorClient

    private var specId = 0L
    private var actorId = 0L
    private lateinit var roles: List<RoleInfo>
    private lateinit var actors: List<ActorInfo>
    private lateinit var spectacles: List<SpectacleInfo>

    override fun beforeSpec(spec: Spec) {
        val count = Random.nextInt(4, 10)

        Generate.spectacles(1).forEach(spectacleClient::addSpectacle)
        spectacles = spectacleClient.getAllSpectacles()

        Generate.actors(count).forEach(actorClient::addActor)
        actors = actorClient.getAllActors()

        Generate.roles(count).forEach(roleClient::addRole)
        roles = roleClient.getAllRoles()

        specId = spectacles.random().id

        var i = 0
        repeat(count) {
            specRoleClient.addRoleToSpec(specId, SpecRoleInfo(roles[i++].id, Random.nextBoolean()))
        }

        specActorClient.addActorsToSpec(specId, actors.map { it.id })
    }

    init {
        "Try to add role for actor when spectacle doesn't exist" {
            specId = Generate.notContained(spectacles.map { it.id })
            actorId = actors.random().id

            val ex = shouldThrow<BadRequest> {
                specRoleActorClient.setRolesForActor(specId, actorId, listOf(roles.random()))
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Spectacle.notFound(specId)
        }

        "Try to add role for non-exist actor" {
            specId = spectacles.random().id
            actorId = Generate.notContained(actors.map { it.id })

            val ex = shouldThrow<BadRequest> {
                specRoleActorClient.setRolesForActor(specId, actorId, listOf(roles.random()))
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Actor.notFound(actorId)
        }

        "Add some roles for actor" {
            val count = Random.nextInt(4, 10)
            val nonExists = Generate.notContainedRoles(roles, count)

            actorId = actors.random().id

            val response = shouldNotThrowAny {
                specRoleActorClient.setRolesForActor(specId, actorId, (nonExists + roles))
            }

            response.size shouldBe nonExists.size
            specRoleActorClient.getRolesByActor(specId, actorId) shouldContainAll roles.map { it.id }
        }

        "Get all actors for role" {
            val roleId = roles.random().id

            val response = shouldNotThrowAny {
                specRoleActorClient.getActorsByRole(specId, roleId)
            }

            response.size shouldBe 1
            actors.map { it.id } shouldContainAll response
        }

        "Remove roles from actors" {
            val role = roles.random()

            shouldNotThrowAny {
                specRoleActorClient.releaseActorFromRoles(specId, actorId, listOf(role))
            }

            val response = specRoleActorClient.getRolesByActor(specId, actorId)

            response.size shouldBe roles.size - 1
            response shouldNotContain role
        }
    }

    override fun afterSpec(spec: Spec) {
        spectacleClient.deleteAll()
        roleClient.deleteAllRoles()
        actorClient.deleteAllActors()
        specRoleActorClient.deleteAll()
        specRoleClient.deleteAll()
        specActorClient.deleteAll()
    }
}