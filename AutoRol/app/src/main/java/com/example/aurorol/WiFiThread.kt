package com.example.aurorol

import android.util.Log
import java.io.IOException
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class WiFiThread() : Thread(), WiFiInterface {
    private val TAG_WIFI = "siec_domowa"

    private val SERVER_PORT = 23
    private val SERVER_IP = "192.168.1.23"
//    private val SERVER_IP = "192.168.0.15"
    private var socket: Socket? = null
    private lateinit var outputStream: OutputStream
    private var isPaused = false
    private val lock = Object()
    private val mutex: Lock = ReentrantLock()
    private var rollAction = 0

    init {
        Log.d(TAG_WIFI, "WiFiInterface init")
    }

    override fun run() {
        Log.d(TAG_WIFI, "WiFi Thread start")
        while (true){
            try {
                socket = Socket(SERVER_IP, SERVER_PORT)
                Log.d(TAG_WIFI, "Socket created")
                outputStream = socket!!.getOutputStream()
                while (socket!!.isConnected){
                    //            var message: String?
//            val input = BufferedReader(InputStreamReader(socket.getInputStream()))



//            while (!currentThread().isInterrupted) {
//            synchronized(lock) {
//                while (isPaused) {
//                    try {
//                        Log.d(TAG_WIFI, "suspended")
//                        lock.wait()
//                        Log.d(TAG_WIFI, "State: "+ currentThread().state.toString())
//                    } catch (e: InterruptedException) {
//                        Log.e(TAG_WIFI, "err1 $e")
//                        currentThread().interrupt()
//                    }
//                }
//            }
                    if(newRollAction()){
                        outputStream.write(rollAction)
                        outputStream.flush()
                        Log.d(TAG_WIFI, rollAction.toString())
                    }

//            message = input.readLine()
//            if (message != null) {
//                // Odebranie wiadomości od serwera
//                Log.d(TAG_WIFI, "Received message: $message")
//            } else {
//                // Przerwanie wątku w przypadku błędu lub zerwania połączenia
////                Log.e(TAG_WIFI, "Connection closed, thread interrupted")
////                interrupt()
//            }
//            val out = OutputStreamWriter(socket.getOutputStream())
//            val `in` = BufferedReader(InputStreamReader(socket.getInputStream())).toString()

//        }
//                    Log.d(TAG_WIFI, "WiFi Thread stop")
                }

            } catch (e: IOException) {
                Log.e(TAG_WIFI, "Failed creating socket $e")
                Log.d(TAG_WIFI, "WiFi Thread stop")
            }


        }




    }

    fun suspendThread() {
        if(currentThread().isAlive)
            isPaused = true
        else
            Log.w(TAG_WIFI, "suspending failed")
    }

    fun resumeThread() {
        if(!currentThread().isInterrupted && currentThread().state != State.RUNNABLE) {
            isPaused = false
            synchronized(lock) {
                Log.d(TAG_WIFI, "resumed")
                lock.notify()
                Log.d(TAG_WIFI, "State: "+ currentThread().state.toString())
            }
        }else
            Log.w(TAG_WIFI, "resuming failed")

    }
    private var prevRollAction = 2
    private fun newRollAction():Boolean{
        mutex.lock()
        return if(rollAction != prevRollAction) {
            prevRollAction = rollAction
            mutex.unlock()
            true
        }else{
            mutex.unlock()
            false
        }
    }

    override fun roll(rollAction_: Int) {
        mutex.lock()
        rollAction=rollAction_
        mutex.unlock()
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