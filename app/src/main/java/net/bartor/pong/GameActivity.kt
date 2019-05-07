package net.bartor.pong

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), GameView.PointCounter {
    private var lPoints = 0
    private var rPoints = 0
    private lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        prefs = getPreferences(Context.MODE_PRIVATE)
        lPoints = prefs.getInt("l", 0)
        rPoints = prefs.getInt("r", 0)

        val game = GameView(this)
        game.setOnPointCounter(this)
        gameContainer.addView(game)
        scoreText.text = "$lPoints:$rPoints"
    }

    override fun onPointCount(left: Boolean) {
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
