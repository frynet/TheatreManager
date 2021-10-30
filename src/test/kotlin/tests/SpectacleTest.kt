package tests

import clients.SpectacleClient
import com.frynet.theatre.errors.Spectacle
import com.frynet.theatre.spectacles.SpectacleConverter
import com.frynet.theatre.spectacles.SpectacleCreate
import com.frynet.theatre.spectacles.SpectacleInfo
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
        SpectacleConverter::class
    ]
)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner::class)
class SpectacleTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var spectacleClient: SpectacleClient

    @Autowired
    private lateinit var spectacleConverter: SpectacleConverter

    private lateinit var spectacle: SpectacleInfo

    init {
        "Try to get non-exist spectacle" {
            val id = 0L

            val ex = shouldThrow<BadRequest> {
                spectacleClient.getSpectacleById(id)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message?.let { it shouldContain Spectacle.notFound(id) }
        }

        "Add some spectacles" {
            val spectacles = Generate.spectacles(Random.nextInt(4, 10))

            spectacles.forEach {
                shouldNotThrowAny {
                    spectacleClient.addSpectacle(it)
                }
            }

            val response = spectacleClient.getAllSpectacles()

            response.size shouldBe spectacles.size
            response.map { spectacleConverter.toCreate(it) } shouldContainAll spectacles
        }

        "Get spectacle by ID" {
            spectacle = spectacleClient.getAllSpectacles().random()

            val response = shouldNotThrowAny {
                spectacleClient.getSpectacleById(spectacle.id)
            }

            response.id shouldBe spectacle.id
            response.title shouldBe spectacle.title
        }

        "Update spectacle by ID" {
            shouldNotThrowAny {
                spectacleClient.updateSpectacleById(spectacle.id, SpectacleCreate("kek"))
            }

            val response = shouldNotThrowAny {
                spectacleClient.getSpectacleById(spectacle.id)
            }

            response.id shouldBe spectacle.id
            response.title shouldNotBe spectacle.title
            response.title shouldBe "kek"
        }

        "Delete spectacle by ID" {
            spectacle = spectacleClient.getAllSpectacles().random()

            shouldNotThrowAny {
                spectacleClient.deleteById(spectacle.id)
            }

            spectacleClient.getAllSpectacles() shouldNotContain spectacle
        }
    }

    override fun afterSpec(spec: Spec) {
        spectacleClient.deleteAll()
    }
}