package com.example.aurorol

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.*
import java.net.Socket

class WiFiThread(private val mainActivity: MainActivity) : Thread(), WiFiInterface {
    private val TAG_WIFI = "siec_domowa"

    private val SERVER_PORT = 23
    private val SERVER_IP = "192.168.131.41" //poco
//    private val SERVER_IP = "192.168.1.23" //keddom
//    private val SERVER_IP = "192.168.0.15" / raw_ap
    private lateinit var socket: Socket
    private var isPaused = false
//    private val mutex: Lock = ReentrantLock()
    private val handler = Handler(Looper.getMainLooper())

//    var kalibRoz_start = false
//    var kalibRoz_stop = false
//    var kalibZwin_start = false
//    var kalibZwin_stop = false
    private var rollAction = 0
    private var MAXstep = 0.0

    var msg =""
    init {
        Log.d(TAG_WIFI, "WiFiInterface init")
    }

    override fun run() {

        Log.d(TAG_WIFI, "WiFi Thread start")

        var outputStream: OutputStream
        var inputStream: InputStream

        var timedOut:Int

        while (true){
            try {
                socket = Socket(SERVER_IP, SERVER_PORT)
                Log.d(TAG_WIFI, "Socket created")
                outputStream = socket.getOutputStream()
                inputStream = socket.getInputStream()

                handler.post {
                    mainActivity.onConnected()
                }
                Log.e(TAG_WIFI, "Connected")
                // oczekiwanie na informacje o MAXstep
                while (inputStream.available()<1){}
                    if(inputStream.available()>0){
                        var i :Int
                        msg=""

                        while(inputStream.available()>0){
                            val c = inputStream.read()
                            i = c - 48
                            if(i>-1){
                                msg+=i.toString()
                            }else {
                                if(!msg.isEmpty()){
                                        MAXstep = msg.toDouble()
                                        Log.d(TAG_WIFI, "MAXstep = $msg")
                                }
                            }
                        }
                }
                while (inputStream.available()<1){}
                if(inputStream.available()>0){
                    var i :Int
                    msg=""

                    while(inputStream.available()>0){
                        val c = inputStream.read()
                        i = c - 48
                        if(i>-1){
                            msg+=i.toString()
                        }else {
                            if(!msg.isEmpty()){
                                    mainActivity.draw(msg.toDouble()/MAXstep)
                                    Log.d(TAG_WIFI, "init z = $msg")
                            }
                        }
                    }
                }

                while (socket.isConnected){

//                    if(kalibRoz){
//                        outputStream.write(3)
//                        outputStream.flush()
//                        Log.d(TAG_WIFI,"kalibRoz" )
//                    }
//                    else if(kalibZwin){
//                        outputStream.write(4)
//                        outputStream.flush()
//                        Log.d(TAG_WIFI,"kalibZwin" )
//                    }
                    if(newRollAction()){
                        outputStream.write(rollAction)
                        outputStream.flush()
                        Log.e(TAG_WIFI, "Action $rollAction")

                        timedOut=0
                        while (inputStream.available()<1){
                            sleep(10)
                            if(timedOut++>500){
                                Log.e(TAG_WIFI, "Timed Out")
                                handler.post {
                                    mainActivity.connecting()
                                }
                                break
                            }
                        }
                        if(timedOut>500){
                            break
                        }
                    }


                    if(inputStream.available()>0){
                        var i :Int
                        msg=""

                        while(inputStream.available()>0){
                            val c = inputStream.read()
                            if(c==45){
                                msg+="-"
                                Log.d(TAG_WIFI, "minus")
                            }else{
                                i = c - 48
                                if(i>-1){
                                    msg+=i.toString()
                                }else {
                                    Log.d(TAG_WIFI, "z = $msg")
                                    if(!msg.isEmpty()){
                                        if(msg.toDouble()<0){
                                            MAXstep = -msg.toDouble()
                                            Log.d(TAG_WIFI, "MAXstep = $MAXstep")
                                            mainActivity.draw(1.0)
                                        }else{
                                            mainActivity.draw(msg.toDouble()/MAXstep)
                                        }
                                    }
                                    msg=""

                                }
                            }

                        }
                    }

                }
                Log.e(TAG_WIFI, "Disconnected")


            } catch (e: IOException) {
                Log.e(TAG_WIFI, "Failed creating socket $e")
//                Log.d(TAG_WIFI, "WiFi Thread stop")
            }
        }
    }

    fun suspendThread() {
        if(currentThread().isAlive)
            isPaused = true
        else
            Log.w(TAG_WIFI, "suspending failed")
    }

//    fun resumeThread() {
//        if(!currentThread().isInterrupted && currentThread().state != State.RUNNABLE) {
//            isPaused = false
//            synchronized(lock) {
//                Log.d(TAG_WIFI, "resumed")
//                lock.notify()
//                Log.d(TAG_WIFI, "State: "+ currentThread().state.toString())
//            }
//        }else
//            Log.w(TAG_WIFI, "resuming failed")
//
//    }
    private var prevRollAction = 2
    private fun newRollAction():Boolean{
//        mutex.lock()
        return if(rollAction != prevRollAction) {
            prevRollAction = rollAction
//            mutex.unlock()
            true
        }else{
//            mutex.unlock()
            false
        }
    }

    override fun roll(rollAction: Int) {
//        mutex.lock()
        this.rollAction =rollAction
//        mutex.unlock()
    }


//
//    fun isClosed(): Boolean {
//        return if(socket.isClosed){
//            Log.d(TAG_WIFI, "Socket is Closed")
//            true
//        } else{
//            Log.d(TAG_WIFI, "Socket is Opened")
//            false
//        }
//    }
//
//    fun isConnected(): Boolean {
//        return if(socket.isConnected){
//            Log.d(TAG_WIFI, "Socket is Connected")
//            true
//        } else{
//            Log.d(TAG_WIFI, "Socket is not Connected")
//            false
//        }
//    }
}