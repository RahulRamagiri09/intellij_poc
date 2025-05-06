//package com.example.sidebarplugin.storage
//
//import com.intellij.openapi.components.PersistentStateComponent
//import com.intellij.openapi.components.State
//import com.intellij.openapi.components.Storage
//
//@State(name = "UrlState", storages = [Storage("UrlState.xml")])
//class PersistentState : PersistentStateComponent<PersistentState.State> {
//
//    private var myState = State()
//
//    data class State(var storedUrl: String? = null, var authToken: String? = null, var gitStoredUrl: String? = null)
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
//
//    fun getAuthToken(): String? {
//        return myState.authToken
//    }
//
//    fun setAuthToken(token: String) {
//        myState.authToken = token
//    }
//
//    fun getGitStoredUrl(): String? {
//        return myState.gitStoredUrl
//    }
//
//    fun setGitStoredUrl(url: String) {
//        myState.gitStoredUrl = url
//    }
//
//    // Method to reset the stored URL and auth token
//    fun resetState() {
//        myState.storedUrl = null
//        myState.authToken = null
//        myState.gitStoredUrl = null
//    }
//}


package com.example.sidebarplugin.storage

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "UrlState", storages = [Storage("UrlState.xml")])
class PersistentState : PersistentStateComponent<PersistentState.State> {

    private var myState = State()

    data class State(
        var storedUrl: String? = null,
        var authToken: String? = null,
        var gitStoredUrl: String? = null,
        var kbStoredUrl: String? = null
    )

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        this.myState = state
    }

    fun getStoredUrl(): String? = myState.storedUrl
    fun setStoredUrl(url: String) {
        myState.storedUrl = url
    }

    fun getAuthToken(): String? = myState.authToken
    fun setAuthToken(token: String) {
        myState.authToken = token
    }

    fun getGitStoredUrl(): String? = myState.gitStoredUrl
    fun setGitStoredUrl(url: String) {
        myState.gitStoredUrl = url
    }

    fun getKbStoredUrl(): String? = myState.kbStoredUrl
    fun setKbStoredUrl(url: String) {
        myState.kbStoredUrl = url
    }

    fun resetState() {
        myState.storedUrl = null
        myState.authToken = null
        myState.gitStoredUrl = null
        myState.kbStoredUrl = null
    }
}
