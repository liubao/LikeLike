package com.liubao.likelike

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibility : AccessibilityService() {
    companion object {
        const val TAG = "MyAccessibility"
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        event.action
        val eventType = event.getEventType();
        var eventText = "";
        Log.i(TAG, "==============Start====================");
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED ->
                eventText = "TYPE_VIEW_CLICKED";

            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> {
                eventText = "TYPE_VIEW_LONG_CLICKED"
                if (event.contentDescription == "Comment")
                    onCommentClicked(event)

            }

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ->
                eventText = "TYPE_WINDOW_STATE_CHANGED"

            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED ->
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED"

            AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE ->
                eventText = "CONTENT_CHANGE_TYPE_SUBTREE"

        }
        Log.i(TAG, eventType.toString())
        Log.i(TAG, eventText)


        Log.i(TAG, "=============END=====================");
    }

    private fun onCommentClicked(event: AccessibilityEvent) {


    }

    override fun onInterrupt() {

    }

}