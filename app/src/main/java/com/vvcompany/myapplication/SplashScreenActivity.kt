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
//        image = findViewById(R.id.splash_screen_id);
//        linearLayout = findViewById(R.id.ss_ll);
//        linearLayout.setVisibility(View.INVISIBLE);
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
//        animation.setDuration(1000);
//        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
//        animation1.setDuration(1500);

        imageView = findViewById(R.id.splash_screen_id)
//        imageView?.setBackgroundColor(R.color.black)

        val animationOut: Animation =
            AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out)
        animationOut.duration(1500)
        val animationIn: Animation =
            AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)
        animationIn.duration(1500)

        animationOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {

                imageView?.setImageResource(R.drawable.logowhite)
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
