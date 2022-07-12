package com.example.weatherforecastapplication.playing

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

data class Node<T>(var data: T, var next: Node<T>? = null, var privous: Node<T>? = null)
class Linked<T> {
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    private var size = 0
    fun isEmpty(): Boolean {
        return size == 0
    }

    fun push(data: T) {
        // add to front
        val node = Node(data, head, head?.privous)
        if (head == null)
            tail = node
        size++
    }
}

fun main(agrs: Array<String>) {
    fun makeLaunch() {

    }

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val innerScope = CoroutineScope(Dispatchers.Default)
    val handler = CoroutineExceptionHandler{
        coroutineContext, throwable ->

        print("snwand")
    }
    runBlocking {

//        innerScope.launch() {
     val job =       scope.launch {
//                launch {
                    throw Exception("problen ya nas")
//                }

            }


        }


}