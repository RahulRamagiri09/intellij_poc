//package com.example.sidebarplugin.storage
//
//object AuthTokenStorage {
//    var accessToken: String? = null
//}
object AuthTokenStorage {
    // Declare the variable to store the access token
    var accessToken: String? = null

    // Optionally, add a function to clear the access token if needed
    fun clear() {
        accessToken = null
    }
}
