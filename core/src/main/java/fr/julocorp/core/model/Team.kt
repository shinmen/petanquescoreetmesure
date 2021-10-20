package fr.julocorp.core.model

import java.util.*

data class Team (val id: UUID, val name: String, var totalScore: Point) {
    init {
        require(name.isNotBlank())
    }

    fun addScore(score: Point) {
        this.totalScore = totalScore + score
    }
}