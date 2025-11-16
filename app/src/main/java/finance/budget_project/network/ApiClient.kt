package finance.budget_project.network


import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


object ApiClient {
    const val BASE_URL = "http://192.168.1.61:4000"



    private val client = OkHttpClient()

    fun signup(username: String, email: String, password: String, callback: (Boolean, String) -> Unit) {
        val json = """{"username":"$username","email":"$email","password":"$password"}"""
        val request = Request.Builder()
            .url("$BASE_URL/signup")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message ?: "Unknown error")
            }
            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful, response.body?.string() ?: "")
            }
        })
    }

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        val json = """{"email":"$email","password":"$password"}"""
        val request = Request.Builder()
            .url("$BASE_URL/login")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message ?: "Unknown error")
            }
            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful, response.body?.string() ?: "")
            }
        })
    }

}