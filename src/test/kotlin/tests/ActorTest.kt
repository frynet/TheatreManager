package tests

import clients.ActorClient
import com.frynet.theatre.actors.ActorConverter
import com.frynet.theatre.actors.ActorCreate
import com.frynet.theatre.actors.ActorInfo
import com.frynet.theatre.errors.Actor
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
        ActorConverter::class
    ]
)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner::class)
class ActorTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var actorClient: ActorClient

    @Autowired
    private lateinit var actorConverter: ActorConverter

    private lateinit var actor: ActorInfo

    init {
        "Try to get non-exist actor" {
            val id = 0L

            val ex = shouldThrow<FeignException.BadRequest> {
                actorClient.getActorById(id)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message?.let { it shouldContain Actor.notFound(id) }
        }

        "Add some actors" {
            val actors = Generate.actors(Random.nextInt(4, 10))

            actors.forEach {
                shouldNotThrowAny {
                    actorClient.addActor(it)
                }
            }

            val response = actorClient.getAllActors()

            response.size shouldBe actors.size
            response.map { actorConverter.toCreate(it) } shouldContainAll actors
        }

        "Get actor by ID" {
            actor = actorClient.getAllActors().random()

            val response = shouldNotThrowAny {
                actorClient.getActorById(actor.id)
            }

            response.id shouldBe actor.id
            response.name shouldBe actor.name
        }

        "Update actor by ID" {
            shouldNotThrowAny {
                actorClient.updateActor(actor.id, ActorCreate("Olga"))
            }

            val response = shouldNotThrowAny {
                actorClient.getActorById(actor.id)
            }

            response.id shouldBe actor.id
            response.name shouldNotBe actor.name
            response.name shouldBe "Olga"
        }

        "Delete actor by ID" {
            actor = actorClient.getAllActors().random()

            shouldNotThrowAny {
                actorClient.deleteActor(actor.id)
            }

            actorClient.getAllActors() shouldNotContain actor
        }
    }

    override fun afterSpec(spec: Spec) {
        actorClient.deleteAllActors()
    }
}