package utils

import com.frynet.theatre.data.actors.ActorCreate
import com.frynet.theatre.data.hall.HallPlace
import com.frynet.theatre.data.hall.HallSize
import com.frynet.theatre.data.repertoires.RepertoireInfo
import com.frynet.theatre.data.roles.RoleCreate
import com.frynet.theatre.data.roles.RoleInfo
import com.frynet.theatre.data.sale_tickets.TicketCreate
import com.frynet.theatre.data.spectacles.SpectacleCreate
import com.frynet.theatre.data.spectacles.SpectacleInfo
import java.time.LocalDate
import kotlin.random.Random

class Generate {

    companion object {

        private val letters = ('a'..'z') + ('A'..'Z')

        private fun randomWord(length: Int): String {
            return (1..length)
                .map { Random.nextInt(0, letters.size) }
                .map(letters::get)
                .joinToString("")
        }

        fun actors(count: Int): List<ActorCreate> {
            val result = mutableListOf<ActorCreate>()

            for (i in 0 until count) {
                result.add(ActorCreate(randomWord(Random.nextInt(4, 10))))
            }

            return result
        }

        fun spectacles(count: Int): List<SpectacleCreate> {
            val result = mutableListOf<SpectacleCreate>()

            for (i in 0 until count) {
                result.add(SpectacleCreate(randomWord(Random.nextInt(4, 10))))
            }

            return result
        }

        fun roles(count: Int): List<RoleCreate> {
            val result = mutableListOf<RoleCreate>()

            for (i in 0 until count) {
                result.add(RoleCreate(randomWord(Random.nextInt(4, 10))))
            }

            return result
        }

        fun notContained(list: List<Long>): Long {
            var result = list.first()

            while (list.contains(result)) {
                result++
            }

            return result
        }

        fun notContained(list: List<Long>, count: Int): List<Long> {
            val result = mutableListOf<Long>()
            var element = list.first()

            for (i in 0 until count) {
                while (list.contains(element) || result.contains(element)) {
                    element++
                }

                result.add(element)
            }

            return result
        }

        fun notContainedDate(list: List<LocalDate>): LocalDate {
            var result = list.first()

            while (list.contains(result)) {
                result = if (Random.nextBoolean()) {
                    result.plusDays(Random.nextLong(15))
                } else {
                    result.minusDays(Random.nextLong(20))
                }
            }

            return result
        }

        fun containedPlace(size: HallSize) = HallPlace(
            row = Random.nextInt(1, size.rows),
            column = Random.nextInt(1, size.columns)
        )

        fun notContainedPlace(size: HallSize) = HallPlace(
            row = Random.nextInt(size.rows + 1, size.rows + 20),
            column = Random.nextInt(size.columns + 1, size.columns + 20)
        )

        fun notContainedRoles(roles: List<RoleInfo>, count: Int): List<RoleInfo> {
            val result = mutableListOf<RoleInfo>()
            val rolesIds = notContained(roles.map { it.id }, count)

            rolesIds.forEach {
                result.add(RoleInfo(it, randomWord(3)))
            }

            return result
        }

        fun repertoires(spectacles: List<SpectacleInfo>, count: Int): List<RepertoireInfo> {
            data class PrimaryKey(
                var specId: Long,

                var date: LocalDate
            )

            var pk: PrimaryKey
            val pkList = mutableListOf<PrimaryKey>()
            val after = datesAfter(LocalDate.now(), count)
            val before = datesBefore(LocalDate.now(), count)
            val result = mutableListOf<RepertoireInfo>()

            repeat(count) {
                pk = PrimaryKey(spectacles.random().id, (after + before).random())

                if (!pkList.contains(pk)) {

                    pkList.add(pk)

                    result.add(
                        RepertoireInfo(
                            specId = pk.specId,
                            date = pk.date,
                            price = Random.nextInt(100, 300)
                        )
                    )
                }
            }

            return result
        }

        fun tickets(repertoires: List<RepertoireInfo>, hallSize: HallSize, count: Int): List<TicketCreate> {
            var place: HallPlace
            var ticket: TicketCreate
            var rep: RepertoireInfo
            val result = mutableListOf<TicketCreate>()

            repeat(count) {
                rep = repertoires.random()
                place = containedPlace(hallSize)
                ticket = TicketCreate(rep.specId, rep.date, place)

                if (!result.contains(ticket)) {
                    result.add(ticket)
                }
            }

            return result
        }

        fun datesAfter(date: LocalDate, count: Int = 1): List<LocalDate> {
            val result = mutableListOf<LocalDate>()

            repeat(count) {
                result.add(date.plusDays(Random.nextLong(4, 356)))
            }

            return result
        }

        fun datesBefore(date: LocalDate, count: Int = 1): List<LocalDate> {
            val result = mutableListOf<LocalDate>()

            repeat(count) {
                result.add(date.minusDays(Random.nextLong(4, 356)))
            }

            return result
        }
    }
}