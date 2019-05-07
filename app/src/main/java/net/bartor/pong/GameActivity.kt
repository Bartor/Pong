package net.bartor.pong

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), GameView.PointCounter {
    private var lPoints = 0
    private var rPoints = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val game = GameView(this)
        game.setOnPointCounter(this)
        gameContainer.addView(game)
    }

    override fun onPointCount(left: Boolean) {
        if (left) rPoints++
        else lPoints++

        runOnUiThread {
            findViewById<TextView>(R.id.scoreText).text = "$lPoints:$rPoints"
        }
    }
}
