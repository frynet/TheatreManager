package tests

import clients.HallClient
import com.frynet.theatre.hall.HallSize
import config.FeignConfiguration
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.random.Random


@SpringBootTest(
    classes = [
        FeignConfiguration::class
    ]
)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner::class)
class HallTest : StringSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var hallClient: HallClient

    private var rows = 0
    private var columns = 0
    private lateinit var size: HallSize
    private lateinit var before: HallSize

    init {
        "Get size for empty hall" {
            size = hallClient.getHallSize()

            size.rows shouldBe 0
            size.columns shouldBe 0
        }

        "Expand hall" {
            rows = Random.nextInt(4, 10)
            columns = Random.nextInt(4, 10)

            hallClient.changeHallSize(HallSize(rows, columns))

            size = hallClient.getHallSize()

            size.rows shouldBe rows
            size.columns shouldBe columns
        }

        "Cut rows" {
            before = hallClient.getHallSize()

            rows = Random.nextInt(1, before.rows)

            size.rows = before.rows - rows
            size.columns = before.columns

            hallClient.changeHallSize(size)
            size = hallClient.getHallSize()

            size.rows shouldBe before.rows - rows
            size.columns shouldBe before.columns
        }

        "Add rows" {
            before = hallClient.getHallSize()

            rows = Random.nextInt(1, 10)

            size.rows = before.rows + rows
            size.columns = before.columns

            hallClient.changeHallSize(size)
            size = hallClient.getHallSize()

            size.rows shouldBe before.rows + rows
            size.columns shouldBe before.columns
        }

        "Cut columns" {
            before = hallClient.getHallSize()

            columns = Random.nextInt(1, before.columns)

            size.rows = before.rows
            size.columns = before.columns - columns

            hallClient.changeHallSize(size)
            size = hallClient.getHallSize()

            size.rows shouldBe before.rows
            size.columns shouldBe before.columns - columns
        }

        "Add columns" {
            before = hallClient.getHallSize()

            columns = Random.nextInt(1, 10)

            size.rows = before.rows
            size.columns = before.columns + columns

            hallClient.changeHallSize(size)
            size = hallClient.getHallSize()

            size.rows shouldBe before.rows
            size.columns shouldBe before.columns + columns
        }

        "Expand hall by rows & columns" {
            rows = Random.nextInt(4, 10)
            columns = Random.nextInt(4, 10)

            before = hallClient.getHallSize()

            size.rows = before.rows + rows
            size.columns = before.columns + columns

            hallClient.changeHallSize(size)
            size = hallClient.getHallSize()

            size.rows shouldBe before.rows + rows
            size.columns shouldBe before.columns + columns
        }

        "Cut rows and columns" {
            before = hallClient.getHallSize()

            rows = Random.nextInt(1, before.rows)
            columns = Random.nextInt(1, before.columns)

            size.rows = before.rows + rows
            size.columns = before.columns + columns

            hallClient.changeHallSize(size)
            size = hallClient.getHallSize()

            size.rows shouldBe before.rows + rows
            size.columns shouldBe before.columns + columns
        }
    }

    override fun afterSpec(spec: Spec) {
        hallClient.deleteAll()
    }
}