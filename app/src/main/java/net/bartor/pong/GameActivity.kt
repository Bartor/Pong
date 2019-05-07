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
    private var top = 0

    private lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        diff = intent?.getSerializableExtra("diff") as GameDiff
        mode = intent?.getSerializableExtra("mode") as GameMode

        prefs = getPreferences(Context.MODE_PRIVATE)
        top = prefs.getInt(when(diff) {
            GameDiff.EASY -> "te"
            GameDiff.MEDIUM -> "tm"
            GameDiff.HARD -> "th"
        }, 0)

        val game = GameView(this, mode, diff)
        game.setOnPointCounter(this)
        gameContainer.addView(game)
        scoreText.text = when(mode) {
            GameMode.SOLO -> "$points\r\nTop: $top"
            else -> "$lPoints:$rPoints"
        }
    }

    override fun onPointCount(left: Boolean) {
        if (mode != GameMode.SOLO) {
            if (left) rPoints++
            else lPoints++

            runOnUiThread {
                scoreText.text = "$lPoints:$rPoints"
            }
        } else {
            points = 0
            runOnUiThread {
                scoreText.text = "$points\r\nTop: $top"
            }
        }
    }

    override fun onBounce() {
        if (mode == GameMode.SOLO) {
            if (++points > top)  {
                top = points
                with(prefs.edit()) {
                    putInt(when(diff) {
                        GameDiff.EASY -> "te"
                        GameDiff.MEDIUM -> "tm"
                        GameDiff.HARD -> "th"
                    }, points)
                    apply()
                }
            }

            runOnUiThread {
                scoreText.text = "$points\r\nTop: $top"
            }
        }
    }
}
