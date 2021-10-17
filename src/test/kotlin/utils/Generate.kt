package utils

import com.frynet.theatre.actors.ActorCreate
import com.frynet.theatre.roles.RoleCreate
import com.frynet.theatre.roles.RoleInfo
import com.frynet.theatre.spectacles.SpectacleCreate
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

        fun notContainedRoles(roles: List<RoleInfo>, count: Int): List<RoleInfo> {
            val result = mutableListOf<RoleInfo>()
            val rolesIds = notContained(roles.map { it.id }, count)

            rolesIds.forEach {
                result.add(RoleInfo(it, randomWord(3)))
            }

            return result
        }
    }
}