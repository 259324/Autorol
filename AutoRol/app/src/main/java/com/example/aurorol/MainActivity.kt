package com.example.aurorol

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {

    private val TAG_DEBUG = "debug"


    private lateinit var mSurfaceDrag: SurfaceDrag
    private lateinit var mSurfaceRol: SurfaceRol
    private lateinit var mWiFiThread: WiFiThread
    private lateinit var mWiFiInterface: WiFiInterface

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG_DEBUG, "onCreate")
        setContentView(R.layout.activity_main)

        mWiFiThread = WiFiThread()
        mWiFiInterface = mWiFiThread


        mSurfaceDrag = SurfaceDrag(applicationContext,findViewById(R.id.SV_dragIcon),
            findViewById(R.id.IV_dragUP),findViewById(R.id.IV_dragDOWN),mWiFiInterface)
        mSurfaceRol = SurfaceRol(applicationContext,findViewById(R.id.SV_rol))
        mWiFiThread.start()

    }



    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG_DEBUG, "onDestroy")
        mWiFiThread.interrupt()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG_DEBUG, "onStart")
//        Log.d(TAG_WIFI, "State: "+mWiFiThread.state.toString())
//        mWiFiThread.resumeThread()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG_DEBUG, "onStop")
        mWiFiThread.suspendThread()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG_DEBUG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG_DEBUG, "onPause")
    }
}