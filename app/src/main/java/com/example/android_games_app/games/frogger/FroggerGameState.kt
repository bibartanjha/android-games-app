package com.example.android_games_app.games.frogger

import com.example.android_games_app.games.frogger.FroggerFixedValues.defaultFrogXOffset
import com.example.android_games_app.games.frogger.FroggerFixedValues.numFrogHomes
import com.example.android_games_app.games.frogger.FroggerFixedValues.numLivesDefault
import com.example.android_games_app.games.frogger.utils.Frog
import com.example.android_games_app.games.frogger.utils.FrogHome
import com.example.android_games_app.utils.GameProgressStatus

data class FroggerGameState (
    val gameProgressStatus: GameProgressStatus = GameProgressStatus.NOT_STARTED,
    val valuesBasedOnScreenSizeAreSet: Boolean = false,
    val frogXOffset: Float = defaultFrogXOffset,
    val frogCurrentRowIndex: Int = FroggerFixedValues.gameRows.size - 1,
    val frogDisplayStatus: Frog.FrogDisplayStatus = Frog.FrogDisplayStatus.POINTING_UP,
    val objectXOffsets: List<List<Float>> = emptyList(),

    val frogAliveStatus: Frog.FrogAliveStatus = Frog.FrogAliveStatus.ALIVE,
    var frogDiedAnimationCounter: Int = 0,

    var rowObjectAnimCounter: Int = 0,

    val riverObjectThatFrogIsTravelingWith: Pair<Int, Int> = Pair(-1, -1), // index corresponds to gameRows in FroggerFixedValues

    val frogHomes: List<FrogHome> = List(numFrogHomes) {
        FrogHome(isOccupied = false)
    },

    val numLivesLeft: Int = numLivesDefault


)