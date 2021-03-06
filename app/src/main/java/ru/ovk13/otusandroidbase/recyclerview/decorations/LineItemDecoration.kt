package ru.ovk13.otusandroidbase.recyclerview.decorations

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LineItemDecoration(
    private val offsetHorizontal: Int,
    private val offsetBottom: Int,
    private val color: Int,
    private val width: Float
) : RecyclerView.ItemDecoration() {

    private val mBounds = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val paint = Paint()
        paint.color = color
        paint.strokeWidth = width

        val childCount = parent.getChildCount()
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val childLeft = child.left.toFloat()
            val childBottom = child.bottom.toFloat()
            canvas.drawLine(
                childLeft - 10,
                child.top.toFloat(),
                childLeft - 10,
                childBottom + 10,
                paint
            )
            canvas.drawLine(
                childLeft - 10,
                childBottom + 10,
                child.right.toFloat(),
                childBottom + 10,
                paint
            )
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = offsetHorizontal
        outRect.right = offsetHorizontal
        outRect.bottom = offsetBottom
    }
}