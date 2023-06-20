package com.example.aurorol

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.RoundedCorner
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class SurfaceRol(context: Context, private val surfaceView: SurfaceView ) : SurfaceView(context), SurfaceHolder.Callback {

    private val TAG = "SurRol"

    init {
        Log.e(TAG, "init")
        surfaceView.setZOrderOnTop(true)
        surfaceView.holder.addCallback(this)
        surfaceView.holder.setFormat(PixelFormat.TRANSPARENT)
    }


    fun draw(rollOut: Double) {
        //proporcje szyby do ramy wyswietlanego okna (poziom,pion)
        val prop = arrayOf(11,17)
        Log.e(TAG, "draw")
        val canvas = surfaceView.holder.lockCanvas()
        if (canvas != null) {
            Log.e(TAG, "canva ok")
            val paint = Paint().apply {
                color = context.resources.getColor(R.color.grey)
                isAntiAlias = true
            }
            canvas.drawColor(0,PorterDuff.Mode.CLEAR)

            val ramaGD = surfaceView.height/prop[1]
            val ramaLP = surfaceView.width/prop[0]

            val rectF = RectF((ramaLP)+0f,(ramaGD)+0f,
                (surfaceView.width-(ramaLP))+0f,
                (ramaGD+(surfaceView.height-ramaGD-ramaGD)*rollOut).toFloat()+0f
            )

            canvas.drawRoundRect(rectF, 40f, 40f, paint)
            surfaceView.holder.unlockCanvasAndPost(canvas)
        }else{
            Log.e(TAG, "canva == NULL")
        }
    }

    fun inv(){surfaceView.visibility=View.INVISIBLE}
    fun vis(){surfaceView.visibility=View.VISIBLE}

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated")
//        draw(0.6)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d(TAG, "surfaceChanged")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed")
    }


}