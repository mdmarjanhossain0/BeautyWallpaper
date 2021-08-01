package com.appbytes.beautywallpaper.util

import com.appbytes.beautywallpaper.ui.AreYouSureCallback


data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val uiComponentType: UIComponentType,
    val messageType: MessageType
)

sealed class UIComponentType{

    class Toast: UIComponentType()

    class Dialog: UIComponentType()

    class AreYouSureDialog(
        val callback: AreYouSureCallback
    ): UIComponentType()

    class None: UIComponentType()
}

sealed class MessageType{

    class Success: MessageType() {
        override fun toString(): String {
            return javaClass.name
        }
    }

    class Error: MessageType() {
        override fun toString(): String {
            return javaClass.name
        }
    }

    class Info: MessageType() {
        override fun toString(): String {
            return javaClass.name
        }
    }

    class None: MessageType() {
        override fun toString(): String {
            return javaClass.name
        }
    }
}


interface StateMessageCallback{

    fun removeMessageFromStack()
}
