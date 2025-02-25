package com.example.hangman

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LetterButtonPanel(gameHandler: Gamehandler) {
    val letters = ('A'..'Z').toList()
    var hintDisabledLetters by remember { mutableStateOf(setOf<Char>()) }
    val effectiveDisabled by remember { derivedStateOf { gameHandler.giveGuessed() union hintDisabledLetters } }
    var hintCounter by remember { mutableStateOf(0) }
    val hint = "Appears in a movie about surfers"

    Column {
        for (row in letters.chunked(7)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { letter ->
                    LetterButton(
                        letter = letter,
                        gameHandler = gameHandler,
                        isDisabled = effectiveDisabled.contains(letter),
                        onDisable = { newLetter ->
                            hintDisabledLetters = hintDisabledLetters union setOf(newLetter)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            hintCounter++
            when (hintCounter) {
                1 -> println(hint)
                2 -> {
                    gameHandler.removeHealth()
                    gameHandler.Loss()
                    val incorrectLetters =
                        ('A'..'Z').toSet() - gameHandler.giveWord().toSet() - gameHandler.giveGuessed()
                    val lettersToDisable = incorrectLetters.take(incorrectLetters.size / 2).toSet()
                    hintDisabledLetters = hintDisabledLetters union lettersToDisable
                }
                3 -> {
                    val vowels = setOf('A', 'E', 'I', 'O', 'U')
                    gameHandler.addGuessedLetters(vowels)
                }
            }
        }) {
            Text("Hint")
        }
    }
}

@Composable
fun LetterButton(
    letter: Char,
    gameHandler: Gamehandler,
    isDisabled: Boolean,
    onDisable: (Char) -> Unit
) {
    Button(
        onClick = {
            onDisable(letter)
            gameHandler.guess(letter)
        },
        modifier = Modifier.padding(4.dp),
        enabled = !isDisabled
    ) {
        Text(text = letter.toString())
    }
}

@Composable
fun NewGameButton(gameHandler: Gamehandler) {
    Button(onClick = {
        gameHandler.NewGame()
    }) {
        Text("New Game")
    }
}
