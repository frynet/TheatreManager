package tests

import clients.ActorClient
import com.frynet.theatre.actors.ActorConverter
import com.frynet.theatre.actors.ActorCreate
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

    init {
        "Try to get non-exist actor" {
            val id = 0L

            val ex = shouldThrow<FeignException.BadRequest> {
                actorClient.getActorById(id)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message?.let {
                it shouldContain "The actor with id=$id not found"
            }
        }

        "Add some actors" {
            val actors = generateActors(Random.nextInt(4, 10))

            actors.forEach {
                shouldNotThrowAny {
                    actorClient.addActor(it)
                }
            }

            val response = actorClient.getAllActors()

            response.size shouldBe actors.size
            response.map { actorConverter.toCreate(it) } shouldContainAll actors
        }

        "Delete actor by ID" {
            val actor = actorClient.getAllActors().random()

            shouldNotThrowAny {
                actorClient.deleteActor(actor.id)
            }

            actorClient.getAllActors() shouldNotContain actor
        }

        "Get actor by ID" {
            val actor = actorClient.getAllActors().random()

            val response = shouldNotThrowAny {
                actorClient.getActorById(actor.id)
            }

            response.id shouldBe actor.id
            response.name shouldBe actor.name
        }
    }

    private fun generateActors(count: Int): List<ActorCreate> {
        val result = mutableListOf<ActorCreate>()

        for (i in 0 until count) {
            result.add(ActorCreate(Generate.randomWord(Random.nextInt(4, 10))))
        }

        return result
    }

    override fun afterSpec(spec: Spec) {
        actorClient.deleteAllActors()
    }
}