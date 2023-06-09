package com.example.aurorol

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class WiFiThread : Thread() {
    private val TAG_WIFI = "siec_domowa"

    private val SERVER_PORT = 23
    private val SERVER_IP = "192.168.1.23"
//    private val SERVER_IP = "192.168.0.15"
    private lateinit var socket: Socket

    init {
        Log.d(TAG_WIFI, "WiFiInterface init")
//        socket = Socket(SERVER_IP, SERVER_PORT)
    }

    private var isPaused = false
    private val lock = Object()


    override fun run() {
        Log.d(TAG_WIFI, "WiFi Thread start")
        try {
            socket = Socket(SERVER_IP, SERVER_PORT)
            Log.d(TAG_WIFI, "Socket created")
//            var message: String?
//            val input = BufferedReader(InputStreamReader(socket.getInputStream()))



            while (!currentThread().isInterrupted) {
            synchronized(lock) {
                while (isPaused) {
                    try {
                        Log.d(TAG_WIFI, "suspended")
                        lock.wait()
                        Log.d(TAG_WIFI, "State: "+ currentThread().state.toString())
                    } catch (e: InterruptedException) {
                        Log.e(TAG_WIFI, "err1 $e")
                        currentThread().interrupt()
                    }
                }
            }
            val out = OutputStreamWriter(socket.getOutputStream())

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

        }
            Log.d(TAG_WIFI, "WiFi Thread stop")
        } catch (e: IOException) {
            Log.e(TAG_WIFI, "Failed creating socket $e")
            Log.d(TAG_WIFI, "WiFi Thread stop")
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