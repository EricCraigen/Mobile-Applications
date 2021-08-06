package com.example.pigdicerollinggame

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var tvGameTitle : TextView

    private lateinit var tvPlayerName : TextView
    private lateinit var tvPlayerGamesWon : TextView
    private lateinit var tvPlayerTotalScore : TextView
    private lateinit var tvPlayerTurnScore : TextView

    lateinit var tvCpuName : TextView
    private lateinit var tvCpuGamesWon : TextView
    private lateinit var tvCpuTotalScore : TextView
    private lateinit var tvCpuTurnScore : TextView

    private lateinit var tvDiceTotal : TextView
    private lateinit var ivDiceImage1 : ImageView
    private lateinit var ivDiceImage2 : ImageView
    private lateinit var ivWinner : ImageView
    private lateinit var ivLoser : ImageView

    private lateinit var btnRoll: Button
    private lateinit var btnHold : Button

    private val dice1 = Dice(6)
    private val dice2 = Dice(6)

    private var playerGamesWon = 0
    private var playerTotalScore = 0
    private var playerTurnScore = 0

    private var cpuGamesWon = 0
    private var cpuTotalScore = 0
    private var cpuTurnScore = 0

    private var dice1Roll = 0
    private var dice2Roll = 0
    private var diceTotal = 0

    private var userTurn = true
    private var winAmount = 100

    private val PLAYER_TURN_SCORE = "PLAYER_TURN_SCORE"
    private val PLAYER_TOTAL_SCORE = "PLAYER_TOTAL_SCORE"
    private val PLAYER_GAMES_WON = "PLAYER_GAMES_WON"

    private val CPU_TURN_SCORE = "CPU_TURN_SCORE"
    private val CPU_TOTAL_SCORE = "CPU_TOTAL_SCORE"
    private val CPU_GAMES_WON = "CPU_GAMES_WON"

    private val DICE_TOTAL = "DICE_TOTAL"
    private val USER_TURN = "USER_TURN"

    private val DICE_1_ROLL = "DICE_1_ROLL"
    private val DICE_2_ROLL = "DICE_2_ROLL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("STATE", "onCreate")

        setContentView(R.layout.activity_main)

        tvGameTitle = findViewById(R.id.tvGameTitle)
        tvGameTitle.setOnClickListener { showInfo() }

        tvPlayerName = findViewById(R.id.tvPlayerName)
        tvPlayerGamesWon = findViewById(R.id.tvPlayerGamesWon)
        tvPlayerTotalScore = findViewById(R.id.tvPlayerTotalScore)
        tvPlayerTurnScore = findViewById(R.id.tvPlayerTurnScore)

        tvCpuName = findViewById(R.id.tvCpuName)
        tvCpuGamesWon = findViewById(R.id.tvCpuGamesWon)
        tvCpuTurnScore = findViewById(R.id.tvCpuTurnScore)
        tvCpuTotalScore = findViewById(R.id.tvCpuTotalScore)

        tvDiceTotal = findViewById(R.id.tvDiceTotalScore)

        ivDiceImage1 = findViewById(R.id.ivDice1)
        ivDiceImage2 = findViewById(R.id.ivDice2)
        ivWinner = findViewById(R.id.ivWinner)
        ivLoser = findViewById(R.id.ivLoser)

        btnRoll = findViewById(R.id.btnRoll)
        btnHold = findViewById(R.id.btnHold)
        btnRoll.setOnClickListener { rollDice() }
        btnHold.setOnClickListener{ holdTurn() }

        ivDiceImage1.setImageResource(R.drawable.dice_1)
        ivDiceImage2.setImageResource(R.drawable.dice_1)
        btnHold.isEnabled = false
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("STATE", "onRestoreInstanceState")
        playerTurnScore = savedInstanceState.getInt(PLAYER_TURN_SCORE)
        playerTotalScore = savedInstanceState.getInt(PLAYER_TOTAL_SCORE)
        playerGamesWon = savedInstanceState.getInt(PLAYER_GAMES_WON)

        cpuTurnScore = savedInstanceState.getInt(CPU_TURN_SCORE)
        cpuTotalScore = savedInstanceState.getInt(CPU_TOTAL_SCORE)
        cpuGamesWon = savedInstanceState.getInt(CPU_GAMES_WON)

        userTurn = savedInstanceState.getBoolean(USER_TURN)

        diceTotal = savedInstanceState.getInt(DICE_TOTAL)

        dice1Roll = savedInstanceState.getInt(DICE_1_ROLL)
        dice2Roll = savedInstanceState.getInt(DICE_2_ROLL)

        getDiceImages()

        tvPlayerTurnScore.text = savedInstanceState.getInt(PLAYER_TURN_SCORE).toString()
        tvPlayerTotalScore.text = savedInstanceState.getInt(PLAYER_TOTAL_SCORE).toString()
        tvPlayerGamesWon.text = savedInstanceState.getInt(PLAYER_GAMES_WON).toString()

        tvCpuTurnScore.text = savedInstanceState.getInt(CPU_TURN_SCORE).toString()
        tvCpuTotalScore.text = savedInstanceState.getInt(CPU_TOTAL_SCORE).toString()
        tvCpuGamesWon.text = savedInstanceState.getInt(CPU_GAMES_WON).toString()

        if (userTurn) {
            btnHold.isEnabled = true
            tvPlayerName.setBackgroundColor(Color.CYAN)
            tvCpuName.setBackgroundColor(Color.WHITE)
        } else {
            btnHold.isEnabled = false
            tvPlayerName.setBackgroundColor(Color.WHITE)
            tvCpuName.setBackgroundColor(Color.CYAN)
        }

        tvDiceTotal.text = savedInstanceState.getInt(DICE_TOTAL).toString()
    }

    override fun onSaveInstanceState(savedState: Bundle) {
        super.onSaveInstanceState(savedState)
        Log.i("STATE", "onSaveInstanceState")
        savedState.putInt(PLAYER_TURN_SCORE, playerTurnScore)
        savedState.putInt(PLAYER_TOTAL_SCORE, playerTotalScore)
        savedState.putInt(PLAYER_GAMES_WON, playerGamesWon)

        savedState.putInt(CPU_TURN_SCORE, cpuTurnScore)
        savedState.putInt(CPU_TOTAL_SCORE, cpuTotalScore)
        savedState.putInt(CPU_GAMES_WON, cpuGamesWon)

        savedState.putBoolean(USER_TURN, userTurn)

        savedState.putInt(DICE_TOTAL, diceTotal)

        savedState.putInt(DICE_1_ROLL, dice1Roll)
        savedState.putInt(DICE_2_ROLL, dice2Roll)
    }

    private fun getDiceImages() {
        val drawableResource1 = when (dice1Roll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        ivDiceImage1.setImageResource(drawableResource1)
        ivDiceImage1.contentDescription = dice1Roll.toString()
        val drawableResource2 = when (dice2Roll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        ivDiceImage2.setImageResource(drawableResource2)
        ivDiceImage2.contentDescription = dice2Roll.toString()
    }

    private fun rollDice() {
        btnHold.isEnabled = true
        tvPlayerName.setBackgroundColor(Color.CYAN)
        tvCpuName.setBackgroundColor(Color.WHITE)

        dice1Roll = dice1.roll()
        dice2Roll = dice2.roll()
        diceTotal = dice1Roll + dice2Roll
        tvDiceTotal.text = "$diceTotal"
        getDiceImages()

        if (dice1Roll == 1 && dice2Roll == 1) {
            playerTurnScore = 0
            playerTotalScore = 0
            tvPlayerTurnScore.text = "$playerTurnScore"
            tvPlayerTotalScore.text = "$playerTotalScore"
            holdTurn()
        } else if ((dice1Roll == 1 && dice2Roll != 1) || (dice1Roll != 1 && dice2Roll == 1)) {
            playerTotalScore -= playerTurnScore
            playerTurnScore = 0
            tvPlayerTurnScore.text = "$playerTurnScore"
            holdTurn()
        } else {
            playerTurnScore += diceTotal
            playerTotalScore += diceTotal
            tvPlayerTurnScore.text = "$playerTurnScore"
        }
    }


    private fun cpuTurn() {
        btnHold.isEnabled = false
        if (!userTurn) {
            object : CountDownTimer(6000, 2000) {  // Notification to onTick every second, lasting for 3 seconds
                override fun onTick(millisUntilFinished: Long) {
                    dice1Roll = dice1.roll()
                    dice2Roll = dice2.roll()
                    diceTotal = dice1Roll + dice2Roll
                    tvDiceTotal.text = "$diceTotal"

                    getDiceImages()

                    if (dice1Roll == 1 && dice2Roll == 1) {
                        cpuTurnScore = 0
                        cpuTotalScore = 0
                        tvCpuTurnScore.text = "$cpuTurnScore"
                        tvCpuTotalScore.text = "$cpuTotalScore"

                        btnRoll.isEnabled = true
                        btnHold.isEnabled = true
                        onFinish()
                        cancel()
                    } else if ((dice1Roll == 1 && dice2Roll != 1) || (dice1Roll != 1 && dice2Roll == 1)) {
                        cpuTotalScore -= cpuTurnScore
                        cpuTurnScore = 0
                        tvCpuTurnScore.text = "$cpuTurnScore"

                        btnRoll.isEnabled = true
                        btnHold.isEnabled = true
                        onFinish()
                        cancel()
                    } else {
                        cpuTurnScore += diceTotal
                        cpuTotalScore += diceTotal
                        tvCpuTurnScore.text = "$cpuTurnScore"
                    }
                }

                override fun onFinish() {

                    cpuTurnScore = 0
                    tvCpuTurnScore.text = "$cpuTurnScore"
                    tvCpuTotalScore.text = "$cpuTotalScore"
                    btnRoll.isEnabled = true
                    btnHold.isEnabled = true
                    userTurn = true
                    tvCpuName.setBackgroundColor(Color.WHITE)
                    tvPlayerName.setBackgroundColor(Color.CYAN)
                    if (cpuTotalScore >= winAmount) {
                        cpuGamesWon += 1
                        tvCpuGamesWon.text = "$cpuGamesWon"
                        ivLoser.isVisible = true
                        ivLoser.setOnClickListener { resetGame() }
                    }
                    cancel()
                }
            }.start()
        }
    }

    private fun holdTurn() {
        btnRoll.isEnabled = false
        userTurn = false
        tvPlayerName.setBackgroundColor(Color.WHITE)
        tvCpuName.setBackgroundColor(Color.CYAN)

        tvPlayerTotalScore.text = "$playerTotalScore"
        playerTurnScore = 0
        tvPlayerTurnScore.text = "$playerTurnScore"

        if (playerTotalScore >= winAmount) {
            playerGamesWon += 1
            tvPlayerGamesWon.text = "$playerGamesWon"
            ivWinner.isVisible = true
            ivWinner.setOnClickListener{ resetGame() }
        } else {
        cpuTurn()
        }
    }

    private fun resetGame() {
        tvPlayerName.setBackgroundColor(Color.CYAN)
        tvCpuName.setBackgroundColor(Color.WHITE)

        playerTotalScore = 0
        tvPlayerTotalScore.text = "$playerTotalScore"
        playerTurnScore = 0
        tvPlayerTurnScore.text = "$playerTurnScore"

        cpuTotalScore = 0
        tvCpuTotalScore.text = "$cpuTotalScore"
        cpuTurnScore = 0
        tvCpuTurnScore.text = "$cpuTurnScore"

        diceTotal = 0
        tvDiceTotal.text = "$diceTotal"

        ivDiceImage1.setImageResource(R.drawable.dice_1)
        ivDiceImage2.setImageResource(R.drawable.dice_1)
        ivWinner.isVisible = false
        ivLoser.isVisible = false
        btnRoll.isEnabled = true
        btnHold.isEnabled = false
    }

    private fun showInfo() {
        val versionNumber = 0.2
        Toast.makeText(applicationContext,getString(R.string.toast_message, versionNumber), Toast.LENGTH_LONG).show()
    }

    class Dice(private val numSides: Int) {
        fun roll(): Int {
            return (1..numSides).random()
        }
    }

}