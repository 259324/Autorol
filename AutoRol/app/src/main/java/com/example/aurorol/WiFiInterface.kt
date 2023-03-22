package com.example.aurorol

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class WiFiInterface : Thread(){


    private val TAG_WIFI = "siec_domowa"


    private val SERVER_PORT = 23
    private val SERVER_IP = "192.168.1.23"

    private var socket: Socket

    init {
        Log.d(TAG_WIFI, "WiFiInterface init")
        socket = Socket(SERVER_IP, SERVER_PORT)
    }

    fun isClosed(): Boolean {
        if(socket.isClosed){
            Log.d(TAG_WIFI, "Socket is Closed")
            return true
        }
        else{
            Log.d(TAG_WIFI, "Socket is Opened")
            return false
        }
    }

    fun isConnected(): Boolean {
        if(socket.isConnected){
            Log.d(TAG_WIFI, "Socket is Connected")
            return true
        }
        else{
        Log.d(TAG_WIFI, "Socket is not Connected")
            return false
        }
    }

    fun startListening(){

        Thread {
            while (true){

                try {
                    socket = Socket(SERVER_IP, SERVER_PORT)
                    Log.d(TAG_WIFI, "Socket created")
                    val out = OutputStreamWriter(socket.getOutputStream())
                    val `in` = BufferedReader(InputStreamReader(socket.getInputStream()))
                    while (true) {
                        val line: String = `in`.readLine()
//                        runOnUiThread {
//                            Log.d(TAG_WIFI, "msg arrived: $line")
//                        }
                    }
                } catch (e: IOException) {
                    Log.d(TAG_WIFI, "Failed creating socket $e")
                }
            }
        }.start()
    }


    }


    //        try {
//            socket.close()
//            Log.d(TAG_WIFI, "Socket closed")
//        } catch (e: IOException) {
//            Log.d(TAG_WIFI, "Failed closing socket $e")
//        }

}