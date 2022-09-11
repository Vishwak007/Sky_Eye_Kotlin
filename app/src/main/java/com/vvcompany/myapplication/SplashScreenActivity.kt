package com.vvcompany.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SplashScreenActivity : AppCompatActivity() {
    var imageView : ImageView? = null
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Objects.requireNonNull(supportActionBar)?.hide()
        imageView = findViewById(R.id.splash_screen_id)

        val animationOut: Animation =
            AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out)
        animationOut.duration(2000)
        val animationIn: Animation =
            AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)
        animationIn.duration(2000)

        animationOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {

                imageView?.setImageResource(R.drawable.logo1)
                animationIn.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                    }

                    override fun onAnimationRepeat(p0: Animation?) {
                    }

                })
                imageView?.startAnimation(animationIn)
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        imageView?.startAnimation(animationOut)

        val thread: Thread = Thread {
            try {
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        thread.start()




    }
}

private operator fun Long.invoke(i: Int) {

}
