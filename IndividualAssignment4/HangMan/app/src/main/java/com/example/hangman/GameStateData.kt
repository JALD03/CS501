package com.example.hangman

import androidx.compose.runtime.mutableStateOf

class Gamehandler {

    private var wordInQuestion: String = "penguin"
    private var guessed = mutableStateOf(setOf<Char>())
    private var health = 6

    fun NewGame() {
        guessed.value = emptySet()
        health = 6
    }

    fun giveGuessed(): Set<Char> = guessed.value

    fun giveWord(): String = wordInQuestion

    fun giveHealth(): Int = health

    fun removeHealth(): Boolean {
        health -= 1
        return true
    }

    fun addGuessedLetters(letters: Set<Char>) {
        guessed.value = guessed.value union letters
    }

    fun guess(letter: Char): Boolean {
        if (letter in guessed.value) {
            removeHealth()
            return false
        }
        if (letter in wordInQuestion) {
            guessed.value = guessed.value union setOf(letter)
            return true
        } else {
            removeHealth()
            return false
        }
    }

    fun underscores(): String {
        return wordInQuestion.map { if (it in guessed.value) it else '_' }
            .joinToString(" ")
    }

    fun Victory(): Boolean {
        return wordInQuestion.all { it in guessed.value }
    }

    fun Loss(): Boolean {
        if (health <= 0) {
            NewGame()
            return true
        }
        return false
    }
}
