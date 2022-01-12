package com.liubao.likelike

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.get
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    val img1 by lazy {
        findViewById<ImageView>(R.id.img1)
    }
    val img2 by lazy {
        findViewById<ImageView>(R.id.img2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val screenShot = BitmapFactory.decodeResource(resources, R.drawable.screen_shot)
//        val screenShotClip = Bitmap.createBitmap(screenShot, 0, 11, screenShot.width, 120)
        val screenShotClip = BitmapFactory.decodeResource(resources, R.drawable.screen_shot_clip2)
        val value1 = screenShot.getPixel(0, 0)
        val value2 = screenShotClip.getPixel(0, 0)
        Log.d(TAG, (value1 == value2).toString())

        img1.setImageDrawable(ColorDrawable(value1))
        img2.setImageDrawable(ColorDrawable(value2))


        isContains(screenShot, screenShotClip).also {
            Log.d(TAG, it.toString())

        }
        findViewById<TextView>(R.id.btn).setOnClickListener {
            val point = isContains(screenShot, screenShotClip).toString()
            Log.d(TAG, point.toString())
        }
    }

    private fun isContains(screenShot: Bitmap?, screenShotClip: Bitmap?): Point {
        if (screenShot == null || screenShotClip == null) return Point(-1, -1)
        Log.d(TAG, (screenShot.getPixel(0, 0) == screenShotClip.getPixel(0, 0)).toString())




        for (y in 0 until screenShot.height - screenShotClip.height) {
            if (screenShot.width == screenShotClip.width) {
                val find = ifAllEqual(
                    screenShot, screenShotClip, 0, y
                )
                if (find) return Point(0, y)
            } else {
                for (x in 0 until screenShot.width - screenShotClip.width) {
                    val find = ifAllEqual(
                        screenShot, screenShotClip, x, y
                    )
                    if (find) return Point(x, y)
                }
            }
        }

        return Point(-1, -1)

    }

    private fun ifAllEqual(bitmap1: Bitmap, bitmap2: Bitmap, startX: Int, startY: Int): Boolean {
        for (y in 0 until bitmap2.height) {
            for (x in 0 until bitmap2.width) {
                val color2 = bitmap2.getPixel(x, y)
                val color1 = bitmap1.getPixel(startX + x, startY + y)
                if (color1 != color2) {
                    return false
                }
            }
        }
        return true
    }
}

