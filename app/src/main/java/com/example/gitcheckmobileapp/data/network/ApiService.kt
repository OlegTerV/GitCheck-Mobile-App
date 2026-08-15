package com.example.gitcheckmobileapp.data.network

import android.net.http.HttpException
import com.example.gitcheckmobileapp.data.model.Resource
import jakarta.inject.Inject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Singleton
import java.net.HttpURLConnection
import java.net.URL

@Singleton
class ApiService @Inject constructor() {

    private fun createConnection(url: String, method: String): HttpURLConnection {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        connection.addRequestProperty("Accept", "application/json")

        return connection
    }

    private fun readResponseStream(connection: HttpURLConnection) : String {
        return connection.inputStream.bufferedReader().use {it.readText()}
    }

    fun searchUser(nickName: String): String {
        val connection = createConnection("https://api.github.com/search/users?q=$nickName", "GET")
        return try {
            val responseCode = connection.responseCode
            if (responseCode in 200..299){
                readResponseStream(connection)
            } else if (responseCode in 400..499) {
                throw HttpException(responseCode.toString(), " Client error.")
            } else if (responseCode in 500..599) {
                throw HttpException(responseCode.toString(), " Server error.")
            } else throw HttpException(responseCode.toString(), "Something error")
        } catch (e: IOException) {
            throw HttpException("ERR_INTERNET_DISCONNECTED", "Something error")
        } finally {
            connection.disconnect()
        }
    }

    fun getAllReposForSelectedUser(userNickname: String): String{
        val connection = createConnection("https://api.github.com/users/$userNickname/repos", "GET")
        return try {
            val responseCode = connection.responseCode
            if (responseCode in 200..299) {
                readResponseStream(connection)
            } else if (responseCode in 400..499) {
                throw HttpException(responseCode.toString(), "Client error.")
            } else if (responseCode in 500..599) {
                throw HttpException(responseCode.toString(), "Server error.")
            } else throw HttpException(responseCode.toString(), "Something error")
        } catch (e: IOException) {
            throw HttpException("ERR_INTERNET_DISCONNECTED", "Something error")
        } finally {
            connection.disconnect()
        }
    }
}

class HttpException(val code: String, message: String): Exception("$message. Code $code.")