package com.example.aurorol

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView

@SuppressLint("ClickableViewAccessibility")
class SurfaceDrag(private val context: Context, private val surfaceView : SurfaceView,
                  private val IM_ArrowUP :ImageView, private val IM_ArrowDOWN: ImageView,
                  private val WiFi: WiFiInterface)
                : SurfaceView(context), SurfaceHolder.Callback, View.OnTouchListener {

    private val TAG = "SurDrag"
    private val TAG_GEST = "gest"
    init {
    Log.e(TAG, "init")
        surfaceView.setZOrderOnTop(true)
        surfaceView.holder.addCallback(this)
        surfaceView.holder.setFormat(PixelFormat.TRANSPARENT)
        surfaceView.setOnTouchListener(this)
    }

    private var wasCenter = false

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val dragSV = surfaceView
        return when (motionEvent.action) {
            //Finger touches
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG_GEST, "Action was DOWN ")
                WiFi.roll(0)
                if (motionEvent.y < (dragSV.height / 2) + dragSV.width / 2 && motionEvent.y > (dragSV.height / 2) - dragSV.width / 2) {
                    wasCenter = true
                }
                true
            }
            MotionEvent.ACTION_MOVE -> {
                if (wasCenter) {draw(motionEvent.x, motionEvent.y)

                    if (motionEvent.y < dragSV.height / 3) {
                        if (IM_ArrowUP.visibility == ImageView.INVISIBLE) {
                            WiFi.roll(2)
                        }
                        IM_ArrowUP.visibility = ImageView.VISIBLE
                    } else {
                        if (IM_ArrowUP.visibility == ImageView.VISIBLE) {
                            WiFi.roll(0)
                        }
                        IM_ArrowUP.visibility = ImageView.INVISIBLE
                    }


                    if (motionEvent.y > (dragSV.height * 2 / 3)) {
                        if (IM_ArrowDOWN.visibility == ImageView.INVISIBLE) {
                            WiFi.roll(1)
                        }
                        IM_ArrowDOWN.visibility = ImageView.VISIBLE
                    } else {
                        if (IM_ArrowDOWN.visibility == ImageView.VISIBLE) {
                            WiFi.roll(0)
                        }
                            IM_ArrowDOWN.visibility = ImageView.INVISIBLE
                    }

//                Log.d(TAG_GEST, "Action was MOVE")
//                Log.d(TAG_GEST, "("+motionEvent.x+","+motionEvent.y+")")
                }
                true
            }
            //Finger un touch
            MotionEvent.ACTION_UP -> {
                wasCenter = false
                resetIcon()

                Log.d(TAG_GEST, "Action was UP")

                if (motionEvent.y > (dragSV.height * 2 / 3)) {
                    if (IM_ArrowDOWN.visibility == ImageView.VISIBLE){
                        WiFi.roll(1)
                    }
                }
                if (motionEvent.y < dragSV.height / 3) {
                    if (IM_ArrowUP.visibility == ImageView.VISIBLE) {
                        WiFi.roll(2)
                    }
                }
                IM_ArrowUP.visibility = ImageView.INVISIBLE
                IM_ArrowDOWN.visibility = ImageView.INVISIBLE

                true
            }
            MotionEvent.ACTION_CANCEL -> {
                WiFi.roll(0)
                Log.d(TAG_GEST, "Action was CANCEL")
                true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                WiFi.roll(0)
                Log.d(TAG_GEST, "Movement occurred outside bounds of current screen element")
                true
            }
            else -> super.onTouchEvent(motionEvent)
        }
    }

    fun draw(x :Float,y:Float) {
//        Log.e(TAG, "draw")
        val canvas = surfaceView.holder.lockCanvas()
        if (canvas != null) {
//            Log.e(TAG, "canva ok")
            val paint = Paint()
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.drag_icon)
            val bitmapReSized = Bitmap.createScaledBitmap(bitmap,surfaceView.width,surfaceView.width,false)
            canvas.drawColor(0,PorterDuff.Mode.CLEAR)
            canvas.drawBitmap(bitmapReSized, x-(surfaceView.width/2), y-(surfaceView.width/2), paint)
            surfaceView.holder.unlockCanvasAndPost(canvas)
        }else{
//            Log.e(TAG, "canva == NULL")
        }
    }

    fun resetIcon(){
        Log.e(TAG, "resetIcon")
        val canvas = surfaceView.holder.lockCanvas()
        if (canvas != null) {
            Log.e(TAG, "canva ok")
            val paint = Paint()
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.drag_icon)
            val bitmapReSized = Bitmap.createScaledBitmap(bitmap,surfaceView.width,surfaceView.width,false)
            canvas.drawColor(0,PorterDuff.Mode.CLEAR)
            canvas.drawBitmap(bitmapReSized, 0f, (surfaceView.height.toFloat()/2)-(surfaceView.width/2), paint)
            surfaceView.holder.unlockCanvasAndPost(canvas)
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