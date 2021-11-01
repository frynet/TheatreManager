package tests

import clients.HallClient
import clients.RepertoireClient
import clients.SpectacleClient
import clients.TicketClient
import com.frynet.theatre.data.hall.HallSize
import com.frynet.theatre.data.repertoires.RepertoireDate
import com.frynet.theatre.data.repertoires.RepertoireDateInterval
import com.frynet.theatre.data.repertoires.RepertoireInfo
import com.frynet.theatre.data.sale_tickets.TicketConverter
import com.frynet.theatre.data.sale_tickets.TicketCreate
import com.frynet.theatre.data.sale_tickets.TicketInfo
import com.frynet.theatre.data.spectacles.SpectacleInfo
import com.frynet.theatre.errors.Hall
import com.frynet.theatre.errors.Repertoire
import com.frynet.theatre.errors.Spectacle
import com.frynet.theatre.errors.Ticket
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
import java.time.LocalDate
import kotlin.random.Random


@SpringBootTest(
    classes = [
        FeignConfiguration::class,
        TicketConverter::class,
    ]
)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner::class)
class TicketTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var ticketClient: TicketClient

    @Autowired
    private lateinit var ticketConverter: TicketConverter

    @Autowired
    private lateinit var hallClient: HallClient

    @Autowired
    private lateinit var spectacleClient: SpectacleClient

    @Autowired
    private lateinit var repertoireClient: RepertoireClient

    private lateinit var hallSize: HallSize
    private lateinit var ticket: TicketCreate
    private lateinit var tickets: List<TicketCreate>
    private lateinit var spectacles: List<SpectacleInfo>
    private lateinit var repertoires: List<RepertoireInfo>
    private lateinit var groupedBySpec: Map<Long, List<TicketCreate>>
    private lateinit var groupedByDate: Map<LocalDate, List<TicketCreate>>

    private fun clearDB() {
        ticketClient.deleteAll()
        hallClient.deleteAll()
        spectacleClient.deleteAll()
        repertoireClient.deleteAll()
    }

    private fun Map<LocalDate, List<TicketInfo>>.toTicketCreate(): Map<LocalDate, List<TicketCreate>> {
        val allValues = mutableListOf<TicketCreate>()

        this.values.forEach { list ->
            list.forEach {
                allValues.add(ticketConverter.toCreate(it))
            }
        }

        return allValues.groupBy { it.date }.toSortedMap()
    }

    override fun beforeSpec(spec: Spec) {
        val count = Random.nextInt(10, 20)

        hallSize = HallSize(Random.nextInt(5, 10), Random.nextInt(4, 9))
        hallClient.changeHallSize(hallSize)

        Generate.spectacles(count).forEach(spectacleClient::addSpectacle)
        spectacles = spectacleClient.getAllSpectacles()

        repertoires = Generate.repertoires(spectacles, count)
        repertoires.forEach(repertoireClient::scheduleSpectacle)

        tickets = Generate.tickets(repertoires, hallSize, count)
        groupedBySpec = tickets.groupBy { it.specId }
        groupedByDate = tickets.groupBy { it.date }.toSortedMap()
    }

    init {
        "Try to get ticket from empty database" {
            val id = 0L

            val ex = shouldThrow<BadRequest> {
                ticketClient.getTicketInfo(id)
            }

            val all = shouldNotThrowAny {
                ticketClient.getAll()
            }

            all.size shouldBe 0
            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Ticket.notFound(id)
        }

        "Try to sell ticket for non-exist spectacle" {
            val specId = Generate.notContained(spectacles.map { it.id })

            ticket = tickets.random().copy()
            ticket.specId = specId

            val ex = shouldThrow<BadRequest> {
                ticketClient.addTicket(ticket)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Spectacle.notFound(specId)
        }

        "Try to sell ticket for not scheduled spectacle" {
            val date = Generate.notContainedDate(repertoires.map { it.date })

            ticket = tickets.random().copy()
            ticket.date = date

            val ex = shouldThrow<BadRequest> {
                ticketClient.addTicket(ticket)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Repertoire.notScheduled(ticketConverter.toInfo(ticket).repertoireInfo)
        }

        "Try to sell ticket for place not contained in hall" {
            val place = Generate.notContainedPlace(hallSize)

            ticket = tickets.random().copy()
            ticket.place = place

            val ex = shouldThrow<BadRequest> {
                ticketClient.addTicket(ticket)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Hall.notContained(place)
        }

        "Try to sell some tickets" {
            tickets.forEach {
                shouldNotThrowAny {
                    ticketClient.addTicket(it)
                }
            }
        }

        "Try to sell ticket for already taken place in hall" {
            ticket = tickets.random()

            val ex = shouldThrow<BadRequest> {
                ticketClient.addTicket(ticket)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Ticket.alreadyExist(ticket.place)
        }

        "Get ticket info" {
            val t1 = shouldNotThrowAny { ticketClient.getAll() }.random()
            val t2 = shouldNotThrowAny { ticketClient.getTicketInfo(t1.id) }

            t1 shouldBe t2
        }

        "Get tickets by non-exist spectacle" {
            val specId = Generate.notContained(spectacles.map { it.id })
            val list = shouldNotThrowAny { ticketClient.getTicketsBySpectacle(specId) }

            list.size shouldBe 0
        }

        "Get tickets by exist spectacle" {
            val specId = groupedBySpec.keys.random()
            val list = shouldNotThrowAny { ticketClient.getTicketsBySpectacle(specId) }

            list.size shouldBe groupedBySpec[specId]!!.size
            list.map { ticketConverter.toCreate(it) } shouldContainAll groupedBySpec[specId]!!
        }

        "Get tickets by not scheduled spectacles" {
            val date = Generate.notContainedDate(repertoires.map { it.date })
            val list = shouldNotThrowAny { ticketClient.getTicketsByDate(RepertoireDate(date)) }

            list.size shouldBe 0
        }

        "Get tickets by scheduled spectacles" {
            val date = groupedByDate.keys.random()
            val list = shouldNotThrowAny { ticketClient.getTicketsByDate(RepertoireDate(date)) }

            list.size shouldBe groupedByDate[date]!!.size
            list.map { ticketConverter.toCreate(it) } shouldContainAll groupedByDate[date]!!
        }

        "Get tickets by invalid date interval" {
            val interval = RepertoireDateInterval(
                begin = groupedByDate.keys.last(),
                end = groupedByDate.keys.first()
            )

            val ex = shouldThrow<BadRequest> {
                ticketClient.getTicketsByInterval(interval)
            }

            ex.status() shouldBe HttpStatus.BAD_REQUEST.value()
            ex.message shouldContain Repertoire.invalidDateInterval(interval)
        }

        "Get tickets by valid date interval" {
            val keys = groupedByDate.keys.toSortedSet().toList()
            val begin = keys.first()
            val end = keys.get(keys.indexOf(keys.last()) - Random.nextInt(keys.size - 3))
            val interval = RepertoireDateInterval(begin, end)
            val filteredByInterval = groupedByDate.filterKeys { (it >= begin) && (it <= end) }
            val response = shouldNotThrowAny { ticketClient.getTicketsByInterval(interval) }.toTicketCreate()

            response.size shouldBe filteredByInterval.size
            response.keys shouldContainAll filteredByInterval.keys
            response.forEach {
                it.value shouldContainAll filteredByInterval[it.key]!!
            }
        }

        "Update ticket" {
            val id = ticketClient.getAll().random().id
            val rep = repertoires.random()
            val place = Generate.containedPlace(hallSize)

            ticket = TicketCreate(rep.specId, rep.date, place)

            shouldNotThrowAny {
                ticketClient.updateTicket(id, ticket)
            }

            val response = ticketClient.getTicketInfo(id)

            response.id shouldBe id
            response.place shouldBe place
            response.repertoireInfo.date shouldBe rep.date
            response.repertoireInfo.specId shouldBe rep.specId
        }

        "Delete ticket" {
            val t = ticketClient.getAll().random()

            shouldNotThrowAny {
                ticketClient.deleteTicket(t.id)
            }

            ticketClient.getAll() shouldNotContain t
        }
    }

    override fun afterSpec(spec: Spec) {
        clearDB()
    }
}