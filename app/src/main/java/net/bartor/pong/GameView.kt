package net.bartor.pong

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import net.bartor.pong.elements.Ball
import net.bartor.pong.elements.Paddle

class GameView(context: Context, attributeSet: AttributeSet) : SurfaceView(context, attributeSet), SurfaceHolder.Callback {
    private val thread : GameThread

    private val ball = Ball(10f, 0f, 0f)
    private val lPaddle = Paddle(0f, 10f, 0f)
    private val rPaddle = Paddle(0f, 0f, 0f)

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    fun update() {
        ball.update()
        if (ball.x <= 0 || ball.x + ball.getSize() >= width) ball.bounce(true)
        if (ball.y <= 0 || ball.y + ball.getSize() >= height) ball.bounce(false)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        ball.draw(canvas)
        lPaddle.draw(canvas)
        rPaddle.draw(canvas)
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        ball.y = height/2f
        ball.x = width/2f

        lPaddle.height = height/3f
        rPaddle.height = height/3f

        lPaddle.y = height/2f - lPaddle.height/2
        rPaddle.y = height/2f - rPaddle.height/2
        rPaddle.x = width - 10f - rPaddle.getWidth()

        thread.running = true
        thread.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        for (i in 0 until event!!.pointerCount) {
            if (event.getX(i) < width/2f) {
                lPaddle.y = event.getY(i) - lPaddle.height/2
            } else {
                rPaddle.y = event.getY(i) - rPaddle.height/2
            }
        }
        return true
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        thread.running = false
        thread.join()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}
}