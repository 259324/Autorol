package com.example.aurorol

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), MainInterface {

    private val TAG_DEBUG = "debug"


    private lateinit var mSurfaceDrag: SurfaceDrag
    private lateinit var mSurfaceRol: SurfaceRol
    private lateinit var mWiFiThread: WiFiThread
    private lateinit var mWiFiInterface: WiFiInterface
    private lateinit var progressBar: ProgressBar

    // true gdy jest atkywny tryb kalibracji
    private var menuRoz = false
    private var menuZwin = false
    private var auto = false

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Obsługa wciśnięcia opcji menu
        return when (item.itemId) {
            R.id.menu_item1 -> {
//                Toast.makeText(this,"zwin",Toast.LENGTH_SHORT).show()
                mWiFiInterface.roll(3)
                if(menuZwin){
                    //zakoncz kalib
                    mSurfaceRol.vis()
                    draw(0.0)
                    item.title = resources.getString( R.string.max_zwiniecie)
                }else{
                    mSurfaceRol.inv()
                    item.title = resources.getString( R.string.max_zwiniecieAKT)
                }
                menuZwin = !menuZwin
                true
            }
            R.id.menu_item2 -> {
//                Toast.makeText(this,"2",Toast.LENGTH_SHORT).show()
                mWiFiInterface.roll(4)
                if(menuRoz){
                    //zakoncz kalib
                    mSurfaceRol.vis()
                    item.title = resources.getString( R.string.max_rozwiniecie)
                }else{
                    mSurfaceRol.inv()
                    item.title = resources.getString( R.string.max_rozwiniecieAKT)
                }
                menuRoz = !menuRoz
                true
            }
            R.id.menu_item3 -> {
//                Toast.makeText(this,"2",Toast.LENGTH_SHORT).show()
                mWiFiInterface.roll(5)
                if(auto){
                    mSurfaceDrag.vis()
                    item.title = "Auto OFF"
                }else{
                    mSurfaceDrag.inv()
                    item.title = "Auto ON"
                }
                auto = !auto
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
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
        mSurfaceRol.draw(rollOut)
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