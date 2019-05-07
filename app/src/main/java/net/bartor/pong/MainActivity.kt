package net.bartor.pong

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        soloButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("diff", getDiff())
            intent.putExtra("mode", GameMode.SOLO)
            startActivity(intent)
        }

        vsButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("diff", getDiff())
            intent.putExtra("mode", GameMode.VS)
            startActivity(intent)
        }

        botButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("diff", getDiff())
            intent.putExtra("mode", GameMode.BOT)
            startActivity(intent)
        }
    }

    private fun getDiff() : GameDiff {
        return when (radios.checkedRadioButtonId) {
            R.id.easyButton -> GameDiff.EASY
            R.id.mediumButton -> GameDiff.MEDIUM
            R.id.hardButton -> GameDiff.HARD
            else -> GameDiff.MEDIUM
        }
    }
}
