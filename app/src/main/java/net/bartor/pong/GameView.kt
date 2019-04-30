package net.bartor.pong

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import net.bartor.pong.elements.Ball

class GameView(context: Context, attributeSet: AttributeSet) : SurfaceView(context, attributeSet), SurfaceHolder.Callback {
    private val thread : GameThread

    private val ball = Ball(10f, 0f, 0f)

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    fun update() {
        ball.update()
        if (ball.x <= 0 || ball.x + ball.getSize()/2 >= width) ball.bounce(true)
        if (ball.y <= 0 || ball.y + ball.getSize()/2 >= height) ball.bounce(false)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        ball.draw(canvas)
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        ball.y = height/2f
        ball.x = width/2f
        thread.running = true
        thread.start()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        thread.running = false
        thread.join()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}
}