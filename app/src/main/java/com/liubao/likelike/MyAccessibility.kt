package com.liubao.likelike

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

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
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                eventText = "TYPE_VIEW_CLICKED";
            }

            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> {
                eventText = "TYPE_VIEW_LONG_CLICKED"


            }
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                eventText = "TYPE_VIEW_FOCUSED"
                onCommentShow(event)

            }

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ->
                eventText = "TYPE_WINDOW_STATE_CHANGED"

            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED ->
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED"

            AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE ->
                eventText = "CONTENT_CHANGE_TYPE_SUBTREE"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                onWindowContentChanged(event)
                eventText = "TYPE_WINDOW_CONTENT_CHANGED"

            }
        }

        Log.i(TAG, eventType.toString())
        Log.i(TAG, eventText)

        Log.i(TAG, "=============END=====================");
    }

    private fun onCommentShow(event: AccessibilityEvent) {
        if (event.source == null) return
        Log.i(TAG, event.source.toString())
        if (event.source.className.equals("android.widget.LinearLayout") == false) return
        if (event.source.childCount != 2) return
        event.source.findAccessibilityNodeInfosByText("Like")?.apply {
            this.forEach {
                var result = it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                result = it.parent?.performAction(AccessibilityNodeInfo.ACTION_CLICK) ?: result
                Log.d(TAG, result.toString())
            }
        }
        findNodeByFilter(event.source, "Like", "Like")?.apply {
            this.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

    val mainHandler = Handler()

    val hashMap: HashMap<AccessibilityNodeInfo, Boolean> = HashMap()
    var oldNode: AccessibilityNodeInfo? = null
    var oldTime = 0L
    var bounds = Rect()
    var filterBounds = Rect(0, 400, 8888, 1600)
    private fun onWindowContentChanged(event: AccessibilityEvent) {
        val targetItem = event.source.getChild(1)
        val count = targetItem.childCount

        val likeNode = findNodeByFilter(targetItem, "Comment")
        if (likeNode == null) return
        likeNode.getBoundsInScreen(bounds)
//        if (!filterBounds.contains(bounds)) {
//            return
//        }
//        if (likeNode == oldNode) return
        oldNode = likeNode
        Log.d(TAG, "like node is ${likeNode?.toString()}")
        likeNode?.apply {
            val timeToPost = if (System.currentTimeMillis() - oldTime < 1000) {
                100
            } else {
                100
            }
            oldTime = System.currentTimeMillis()
            val performed = likeNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            Log.d(TAG, "node click performed is ${performed}")
        }
    }

    private fun findNodeByFilter(
        node: AccessibilityNodeInfo,
        contentDescription: String? = null,
        text: String? = null
    ): AccessibilityNodeInfo? {
        val targetText = if (contentDescription.isNullOrEmpty()) text else contentDescription
        var likeNode: AccessibilityNodeInfo? = null
        val count = node.childCount
        for (index in 0 until count) {
            node.getChild(index).apply {
                if (targetText == this.contentDescription || targetText == this.text) {
                    likeNode = this
                } else {
                    likeNode = findNodeByFilter(this, contentDescription, text)
                }
            }
        }
        return likeNode
    }


    override fun onInterrupt() {

    }

}