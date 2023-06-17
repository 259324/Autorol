package com.example.aurorol

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


class MainActivity : AppCompatActivity(), MainInterface {

    private val TAG_DEBUG = "debug"


    private lateinit var mSurfaceDrag: SurfaceDrag
    private lateinit var mSurfaceRol: SurfaceRol
    private lateinit var mWiFiThread: WiFiThread
    private lateinit var mWiFiInterface: WiFiInterface
    private lateinit var progressBar: ProgressBar

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG_DEBUG, "onCreate")
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)



        mWiFiThread = WiFiThread(this)
        mWiFiInterface = mWiFiThread
        mSurfaceDrag = SurfaceDrag(applicationContext,findViewById(R.id.SV_dragIcon),
            findViewById(R.id.IV_dragUP),findViewById(R.id.IV_dragDOWN),mWiFiInterface)
        mSurfaceRol = SurfaceRol(applicationContext,findViewById(R.id.SV_rol))

        connecting()

        mWiFiThread.start()




    }

    override fun onConnected(){
        progressBar.visibility = View.INVISIBLE
        mSurfaceDrag.vis()
        mSurfaceRol.vis()
    }

    override fun connecting() {
        Log.d(TAG_DEBUG, "connecting()")
        progressBar.visibility = View.VISIBLE
        mSurfaceDrag.inv()
        mSurfaceRol.inv()
    }

    override fun draw(rollOut: Double) {
        mSurfaceRol.draw(rollOut/1000)
    }


    override fun onDestroy() {
        super.onDestroy()
//        Log.d(TAG_DEBUG, "onDestroy")
        mWiFiThread.interrupt()
    }

    override fun onStop() {
        super.onStop()
//        Log.d(TAG_DEBUG, "onStop")
        mWiFiThread.suspendThread()
    }

}