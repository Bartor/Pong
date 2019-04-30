package net.bartor.pong

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), GameView.PointCounter {
    private var lPoints = 0
    private var rPoints = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val game = GameView(this)
        game.setOnPointCounter(this)
        game.elevation = -10f
        container.addView(game)
    }

    override fun onPointCount(left: Boolean) {
        if (left) lPoints++
        else rPoints++

        scoreText.setText("${lPoints} : ${rPoints}")
    }
}
