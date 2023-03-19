package com.example.aurorol

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.properties.Delegates

class SurfaceDrag(context: Context, surfaceView_: SurfaceView) : SurfaceView(context), SurfaceHolder.Callback {

    private val TAG = "SurDrag"
    private val surfaceView = surfaceView_
    private var surfaceHolder: SurfaceHolder= surfaceView.holder

    init {
    Log.e(TAG, "init")
    surfaceView.setZOrderOnTop(true)
    surfaceView.holder.addCallback(this)
    surfaceView.holder.setFormat(PixelFormat.TRANSPARENT)

    }

    fun draw(x :Float,y:Float) {
//        Log.e(TAG, "draw")
        val canvas = surfaceHolder.lockCanvas()
        if (canvas != null) {
//            Log.e(TAG, "canva ok")
            val paint = Paint()
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.drag_icon)
            val bitmapReSized = Bitmap.createScaledBitmap(bitmap,surfaceView.width,surfaceView.width,false)
            canvas.drawColor(0,PorterDuff.Mode.CLEAR)
            canvas.drawBitmap(bitmapReSized, x-(surfaceView.width/2), y-(surfaceView.width/2), paint)
            surfaceHolder.unlockCanvasAndPost(canvas)
        }else{
//            Log.e(TAG, "canva == NULL")
        }
    }

    fun resetIcon(){
        Log.e(TAG, "resetIcon")
        val canvas = surfaceHolder.lockCanvas()
        if (canvas != null) {
            Log.e(TAG, "canva ok")
            val paint = Paint()
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.drag_icon)
            val bitmapReSized = Bitmap.createScaledBitmap(bitmap,surfaceView.width,surfaceView.width,false)
            canvas.drawColor(0,PorterDuff.Mode.CLEAR)
            canvas.drawBitmap(bitmapReSized, 0f, (surfaceView.height.toFloat()/2)-(surfaceView.width/2), paint)
            surfaceHolder.unlockCanvasAndPost(canvas)
        }else{
            Log.e(TAG, "canva == NULL")
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.e(TAG, "surfaceCreated")
        resetIcon()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.e(TAG, "surfaceChanged")
        resetIcon()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.e(TAG, "surfaceDestroyed")
    }


}