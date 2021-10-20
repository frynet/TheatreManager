package tests

import clients.RepertoireClient
import clients.SpectacleClient
import com.frynet.theatre.errors.Repertoire
import com.frynet.theatre.errors.Spectacle
import com.frynet.theatre.repertoires.RepertoireDate
import com.frynet.theatre.repertoires.RepertoireDateInterval
import com.frynet.theatre.repertoires.RepertoireInfo
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
import io.kotest.matchers.maps.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import utils.Generate
import java.time.LocalDate
import kotlin.random.Random


@SpringBootTest(
    classes = [
        FeignConfiguration::class,
    ]
)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner::class)
class RepertoireTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var spectacleClient: SpectacleClient

    @Autowired
    private lateinit var repertoireClient: RepertoireClient

    private var specId = 0L
    private lateinit var spectacles: List<SpectacleInfo>
    private lateinit var repertoires: List<RepertoireInfo>
    private lateinit var groupedRepertoires: Map<LocalDate, List<RepertoireInfo>>

    override fun beforeSpec(spec: Spec) {
        val count = Random.nextInt(4, 10)

        Generate.spectacles(count).forEach(spectacleClient::addSpectacle)
        spectacles = spectacleClient.getAllSpectacles()

        repertoires = Generate.repertoires(spectacles, count)

        groupedRepertoires = repertoires
            .groupBy { it.date }
            .toSortedMap()
    }

    init {
        "Try to schedule non-exist spectacle" {
            specId = Generate.notContained(spectacles.map { it.id })

            val ex = shouldThrow<BadRequest> {
                repertoireClient.scheduleSpectacle(RepertoireInfo(specId, LocalDate.now(), Random.nextInt(300)))
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Spectacle.notFound(specId)
        }

        "Try to schedule some spectacles" {
            repertoires.forEach {
                shouldNotThrowAny {
                    repertoireClient.scheduleSpectacle(it)
                }
            }
        }

        "Try to schedule already scheduled spectacles" {
            var ex: BadRequest

            repertoires.forEach {
                ex = shouldThrow {
                    repertoireClient.scheduleSpectacle(it)
                }

                ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
                ex.message shouldContain Repertoire.alreadyScheduled(it)
            }
        }

        "Is it schedule? (For scheduled spectacle)" {
            repertoireClient.isScheduled(repertoires.random()) shouldBe true
        }

        "Try to get spectacles by date" {
            val date = groupedRepertoires.keys.random()

            val response = repertoireClient.getSpectaclesByDate(RepertoireDate(date))

            response.size shouldBe groupedRepertoires[date]!!.size
            response shouldContainAll groupedRepertoires[date]!!
        }

        "Try to get spectacles by invalid interval" {
            val interval = RepertoireDateInterval(
                begin = groupedRepertoires.keys.last(), end = groupedRepertoires.keys.first()
            )

            val ex = shouldThrow<BadRequest> {
                repertoireClient.getSpectaclesByInterval(interval)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Repertoire.invalidDateInterval(interval)
        }

        "Try to get spectacles by valid interval" {
            val interval = RepertoireDateInterval(
                begin = groupedRepertoires.keys.first(), end = groupedRepertoires.keys.last()
            )
            val response = repertoireClient.getSpectaclesByInterval(interval)

            response.size shouldBe groupedRepertoires.size
            response shouldContainAll groupedRepertoires
        }

        "Try to cancel spectacle" {
            val rep = repertoires.random()

            shouldNotThrowAny {
                repertoireClient.cancelSpectacle(rep)
            }

            val response = repertoireClient.getSpectaclesByDate(RepertoireDate(rep.date))

            response.size shouldBe groupedRepertoires[rep.date]!!.size - 1
            response shouldNotContain rep
        }
    }

    override fun afterSpec(spec: Spec) {
        spectacleClient.deleteAll()
    }
}