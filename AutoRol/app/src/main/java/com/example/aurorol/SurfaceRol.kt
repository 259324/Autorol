package com.example.aurorol

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class SurfaceRol(context: Context, private val surfaceView: SurfaceView ) : SurfaceView(context), SurfaceHolder.Callback {

    private val TAG = "SurRol"

    init {
        Log.e(TAG, "init")
        surfaceView.setZOrderOnTop(true)
        surfaceView.holder.addCallback(this)
        surfaceView.holder.setFormat(PixelFormat.TRANSPARENT)
    }

    private fun draw() {
        Log.e(TAG, "draw")
        val canvas = surfaceView.holder.lockCanvas()
        if (canvas != null) {
            Log.e(TAG, "canva ok")
            val paint = Paint()
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.window)
            val bitmapReSized = Bitmap.createScaledBitmap(bitmap,surfaceView.width,surfaceView.height,false)
            canvas.drawColor(0, PorterDuff.Mode.CLEAR)
            canvas.drawBitmap(bitmapReSized, 0f, 0f, paint)
            surfaceView.holder.unlockCanvasAndPost(canvas)
        }else{
            Log.e(TAG, "canva == NULL")
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated")
        draw()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d(TAG, "surfaceChanged")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed")
    }


}