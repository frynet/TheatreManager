package tests

import clients.ActorClient
import clients.SpecActorClient
import clients.SpectacleClient
import com.frynet.theatre.data.actors.ActorInfo
import com.frynet.theatre.data.spectacles.SpectacleInfo
import com.frynet.theatre.errors.Spectacle
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
class SpecActorTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var specActorClient: SpecActorClient

    @Autowired
    private lateinit var spectacleClient: SpectacleClient

    @Autowired
    private lateinit var actorClient: ActorClient

    private var specId = 0L
    private lateinit var actors: List<ActorInfo>
    private lateinit var spectacles: List<SpectacleInfo>

    override fun beforeSpec(spec: Spec) {
        val count = Random.nextInt(4, 10)

        Generate.spectacles(count).forEach(spectacleClient::addSpectacle)
        spectacles = spectacleClient.getAllSpectacles()

        Generate.actors(count).forEach(actorClient::addActor)
        actors = actorClient.getAllActors()
    }

    init {
        "Try to add actor when spectacle doesn't exists" {
            val specId = Generate.notContained(spectacles.map { it.id })

            val ex = shouldThrow<BadRequest> {
                specActorClient.addActorsToSpec(specId, listOf(actors.random().id))
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Spectacle.notFound(specId)
        }

        "Add some actors" {
            val count = Random.nextInt(4, 10)
            val actorsIds = actors.map { it.id }
            val nonExists = Generate.notContained(actorsIds, count)

            specId = spectacles.random().id

            val response = shouldNotThrowAny {
                specActorClient.addActorsToSpec(specId, (nonExists + actorsIds))
            }

            response.size shouldBe nonExists.size
            specActorClient.getActorsInSpec(specId) shouldContainAll actorsIds
        }

        "Get all actors in spectacle" {
            val response = shouldNotThrowAny {
                specActorClient.getActorsInSpec(specId)
            }

            response.size shouldBe actors.size
            response shouldContainAll actors.map { it.id }
        }

        "Delete actor from spectacle" {
            val id = actors.random().id

            shouldNotThrowAny {
                specActorClient.deleteActorFromSpec(specId, id)
            }

            val response = specActorClient.getActorsInSpec(specId)

            response.size shouldBe actors.size - 1
            response shouldNotContain id
        }
    }

    override fun afterSpec(spec: Spec) {
        actorClient.deleteAllActors()
        spectacleClient.deleteAll()
        specActorClient.deleteAll()
    }
}