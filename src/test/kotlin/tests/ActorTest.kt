package tests

import clients.ActorClient
import com.frynet.theatre.actors.ActorConverter
import com.frynet.theatre.actors.ActorCreate
import com.frynet.theatre.actors.ActorInfo
import config.FeignConfiguration
import feign.FeignException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner


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
                it.contains("The actor with id=$id not found") shouldBe true
            }
        }

        "Add new actor" {
            var response: ActorInfo? = null
            val actor = ActorCreate("Ivan")

            shouldNotThrowAny {
                response = actorClient.addActor(actor)
            }

            response?.let {
                it.name shouldBe actor.name
            }
        }

        "Add some actors" {
            val actors = listOf(
                ActorCreate("Andrey"),
                ActorCreate("Michael"),
                ActorCreate("Olga")
            )

            actors.forEach {
                shouldNotThrowAny {
                    actorClient.addActor(it)
                }
            }

            val response = actorClient.getAllActors()

            response.size shouldBe 4
            response.map { actorConverter.toCreate(it) } shouldContainAll actors
        }
    }

    override fun afterSpec(spec: Spec) {
        actorClient.deleteAllActors()
    }
}