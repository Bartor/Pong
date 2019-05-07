package net.bartor.pong

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), GameView.PointCounter {
    private lateinit var diff: GameDiff
    private lateinit var mode: GameMode

    private var lPoints = 0
    private var rPoints = 0

    private var points = 0
    private lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        diff = intent?.getSerializableExtra("diff") as GameDiff
        mode = intent?.getSerializableExtra("mode") as GameMode

        prefs = getPreferences(Context.MODE_PRIVATE)
        lPoints = prefs.getInt("l", 0)
        rPoints = prefs.getInt("r", 0)
        points = prefs.getInt("p", 0)

        val game = GameView(this, mode, diff)
        game.setOnPointCounter(this)
        gameContainer.addView(game)
        scoreText.text = "$lPoints:$rPoints"
    }

    override fun onPointCount(left: Boolean) {
        if (mode != GameMode.SOLO) {
            if (left) rPoints++
            else lPoints++

            with(prefs.edit()) {
                putInt("l", lPoints)
                putInt("r", rPoints)
                apply()
            }

            runOnUiThread {
                scoreText.text = "$lPoints:$rPoints"
            }
        }
    }

    override fun onBounce() {
        if (mode == GameMode.SOLO) {
            points++

            with(prefs.edit()) {
                putInt("p", points)
                apply()
            }

            runOnUiThread {
                scoreText.text = "$"
            }
        }
    }
}
