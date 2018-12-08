package com.google.milodrama13.wardrobe

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.MotionEvent
import android.view.View.GONE


class SwipeController : ItemTouchHelper.Callback() {
    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0,LEFT+RIGHT)
    }

    private var _swipeBack = false
    private var _visibility = GONE
    private var _buttonWidth = 300

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (_swipeBack){
            _swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ACTION_STATE_SWIPE){
            recyclerView.setOnTouchListener { v, event -> onTouchSwipe(event, dX, recyclerView, c, viewHolder, dY, actionState, isCurrentlyActive) }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    fun onTouchSwipe(event: MotionEvent, dX: Float, recyclerView: RecyclerView, c: Canvas, viewHolder: RecyclerView.ViewHolder, dY: Float, actionState: Int, isCurrentlyActive: Boolean): Boolean {
        _swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
        if (_swipeBack) {
            if (dX < -_buttonWidth)
                _visibility = RIGHT
            else if (dX > _buttonWidth)
                _visibility = LEFT
            if (_visibility != GONE) {
                recyclerView.setOnTouchListener { v, event -> onTouchDown(event, recyclerView, c, viewHolder, dY, actionState, isCurrentlyActive) }
            }
        }
        for (i in 0 until recyclerView.childCount)
            recyclerView.getChildAt(i).isClickable = false
        return false
    }

    fun onTouchDown(event: MotionEvent, recyclerView: RecyclerView, c: Canvas, viewHolder: RecyclerView.ViewHolder, dY: Float, actionState: Int, isCurrentlyActive: Boolean): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            recyclerView.setOnTouchListener { v, event -> onTouchUp(event, c, recyclerView, viewHolder, dY, actionState, isCurrentlyActive) }
        }
        return false
    }

    fun onTouchUp(event: MotionEvent, c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dY: Float, actionState: Int, isCurrentlyActive: Boolean): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive)
            recyclerView.setOnTouchListener { v, event -> false }
            _swipeBack = false
            for (i in 0 until recyclerView.childCount)
                recyclerView.getChildAt(i).isClickable = true
            _visibility = GONE
        }
        return false
    }
}