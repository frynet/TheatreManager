package utils

import kotlin.random.Random

class Generate {

    companion object {

        private val letters = ('a'..'z') + ('A'..'Z')

        fun randomWord(length: Int): String {
            return (1..length)
                .map { Random.nextInt(0, letters.size) }
                .map(letters::get)
                .joinToString("")
        }
    }
}