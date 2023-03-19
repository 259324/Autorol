package com.example.aurorol

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnTouchListener  {

    private val TAG = "Gest"

    private lateinit var mSurfaceDrag: SurfaceDrag
    private lateinit var mSurfaceRol: SurfaceRol
    private lateinit var IM_ArrowUP: ImageView
    private lateinit var IM_ArrowDOWN: ImageView
    private lateinit var dragSV: SurfaceView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dragSV = findViewById(R.id.SV_dragIcon)
        dragSV.setOnTouchListener(this)

        mSurfaceDrag = SurfaceDrag(this,dragSV)
        mSurfaceRol = SurfaceRol(this,findViewById(R.id.SV_rol))

        IM_ArrowUP = findViewById(R.id.IV_dragUP)
        IM_ArrowDOWN = findViewById(R.id.IV_dragDOWN)


    }

    var wasCenter =false
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return when (motionEvent.action) {
            //Finger touches
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "Action was DOWN ")
                if (motionEvent.y < (dragSV.height/2)+dragSV.width/2 &&  motionEvent.y >(dragSV.height/2)-dragSV.width/2) {
                    wasCenter = true
                }
                true
            }
            MotionEvent.ACTION_MOVE -> {
                if(wasCenter) {
                    mSurfaceDrag.draw(motionEvent.x, motionEvent.y)
                    if (motionEvent.y < dragSV.height / 3) {
                        IM_ArrowUP.visibility = ImageView.VISIBLE
                    } else {
                        IM_ArrowUP.visibility = ImageView.INVISIBLE
                    }
                    if (motionEvent.y > (dragSV.height * 2 / 3)) {
                        IM_ArrowDOWN.visibility = ImageView.VISIBLE
                    } else {
                        IM_ArrowDOWN.visibility = ImageView.INVISIBLE
                    }
//                Log.d(TAG, "Action was MOVE")
//                Log.d(TAG, "("+motionEvent.x+","+motionEvent.y+")")
                }
                true
            }
            //Finger untouches
            MotionEvent.ACTION_UP -> {
                wasCenter = false
                mSurfaceDrag.resetIcon()
                IM_ArrowUP.visibility=ImageView.INVISIBLE
                IM_ArrowDOWN.visibility=ImageView.INVISIBLE
                Log.d(TAG, "Action was UP")
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "Action was CANCEL")
                true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                Log.d(TAG, "Movement occurred outside bounds of current screen element")
                true
            }
            else -> super.onTouchEvent(motionEvent)
        }
    }
}