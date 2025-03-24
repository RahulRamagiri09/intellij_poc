//package com.example.sidebarplugin
//
//import com.intellij.openapi.components.PersistentStateComponent
//import com.intellij.openapi.components.State
//import com.intellij.openapi.components.Storage
//
//@State(name = "UrlState", storages = [Storage("UrlState.xml")])
//class UrlState : PersistentStateComponent<UrlState.State> {
//
//    private var myState = State()
//
//    data class State(var storedUrl: String? = null)
//
//    override fun getState(): State {
//        return myState
//    }
//
//    override fun loadState(state: State) {
//        this.myState = state
//    }
//
//    fun getStoredUrl(): String? {
//        return myState.storedUrl
//    }
//
//    fun setStoredUrl(url: String) {
//        myState.storedUrl = url
//    }
//}


package com.example.sidebarplugin

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "UrlState", storages = [Storage("UrlState.xml")])
class PersistentState : PersistentStateComponent<PersistentState.State> {

    private var myState = State()

    data class State(var storedUrl: String? = null, var authToken: String? = null)

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        this.myState = state
    }

    fun getStoredUrl(): String? {
        return myState.storedUrl
    }

    fun setStoredUrl(url: String) {
        myState.storedUrl = url
    }

    fun getAuthToken(): String? {
        return myState.authToken
    }

    fun setAuthToken(token: String) {
        myState.authToken = token
    }

    // Method to reset the stored URL and auth token
    fun resetState() {
        myState.storedUrl = null
        myState.authToken = null
    }
}