package com.mobile.azrinurvani.clippingexample

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View

//TODO: Step 1.1 Create ClippedView class for a custom view that extends View
class ClippedView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context,attrs,defStyleAttr) {

    //TODO: Step 1.6 In ClippedView define a variable paint of a Paint.
    // a. Enable anti-aliasing,
    // b. and use the stroke width and
    // c. text size defined in the dimensions.
    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }
    //TODO: Step 1.7 Create and initialize a path variable of a Path to store locally the path of what has been drawn.
    private val path = Path()

    //TODO: Step 1.8 Add variables for dimensions for a clipping rectangle around the whole set of shapes.
    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)

    //TODO: Step 1.9 Add variables for the inset of a rectangle and the offset of a small rectangle.
    private val rectInset = resources.getDimension(R.dimen.rectInset)
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)

    //TODO: Step 1.10 Add a variable for the radius of a circle. This is the circle that is drawn inside the rectangle.
    private val circleRadius = resources.getDimension(R.dimen.circleRadius)

    //TODO: Step 1.11 Add an offset and a text size for text that is drawn inside the rectangle.
    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    //TODO: Step 1.12 Set up the coordinates for two columns.
    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight

    //TODO: Step 1.13 Add the coordinates for each row, including the final row for the transformed text.
    private val rowOne = rectInset
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom
    private val textRow = rowFour + (1.5f*clipRectBottom)

    //TODO: Step 6.1 At the class level, create and initialize a rectangle variable.
    // RectF is a class that holds rectangle coordinates in floating point.
    private var rectF = RectF(
        rectInset,
        rectInset,
        clipRectRight - rectInset,
        clipRectBottom - rectInset
    )
    //TODO: Step 10.1 At the top-level, create a variable for the y coordinates of an additional row - quickRecet variable
    private val rejectRow = rowFour + rectInset + 2 * clipRectBottom


    //TODO: Step 1.14 Override onDraw() and call a function for each shape you are drawing.
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBackAndUnclippedRectangle(canvas)
        drawDifferenceClippingExample(canvas)
        drawCircularClippingExample(canvas)
        drawIntersectionClippingExample(canvas)
        drawCombinedClippingExample(canvas)
        drawRoundedRectangleClippingExample(canvas)
        drawOutsideClippingExample(canvas)
        drawSkewedTextExample(canvas)
        drawTranslatedTextExample(canvas)
        drawQuickRejectExample(canvas)
    }

    //TODO: Step 1.16 Create a drawClippedRectangle() method that takes an argument canvas of type Canvas
    private fun drawClippedRectangle(canvas: Canvas?){
        //TODO: Step 1.17  set the boundaries of the clipping rectangle for the whole shape.
        // Apply a clipping rectangle that constrains to drawing only the square.
        canvas?.clipRect(
            clipRectLeft,clipRectTop,clipRectRight,clipRectBottom
        )
        //TODO: Step 1.18 Add code to fill the canvas with white color.
        // Only the region inside the clipping rectangle will be filled!
        canvas?.drawColor(Color.WHITE)

        //TODO: Step 1.19 Change the color to red and draw a diagonal line inside the clipping rectangle.
        paint.color = Color.RED
        canvas?.drawLine(
            clipRectLeft,clipRectTop,clipRectRight,clipRectBottom,paint
        )

        //TODO: Step 1.20 Set the color to green and draw a circle inside the clipping rectangle
        paint.color = Color.GREEN
        canvas?.drawCircle(
            circleRadius,clipRectBottom-circleRadius,circleRadius,paint
        )

        //TODO: Step 1.21 Set the color to blue and draw text aligned with the right edge of the clipping rectangle.
        // Use Canvas.drawText() to draw text.
        paint.color = Color.BLUE
        paint.textSize = textSize
        // Align the RIGHT side of the text with the origin.
        paint.textAlign = Paint.Align.RIGHT
        canvas?.drawText(
            context.getString(R.string.clipping),
            clipRectRight,textOffset,paint
        )
    }

    //TODO: Step 1.15 Create stubs for each of the drawing functions so that the code will continue to compile.
    private fun drawBackAndUnclippedRectangle(canvas: Canvas?) {
        //TODO: Step 1.22 To see the drawClippedRectangle() method in action,
        //draw the first unclipped rectangle by implementing the drawUnclippedRectangle() method as shown below.
        canvas?.drawColor(Color.GRAY)
        // a. Save the canvas.
        canvas?.save()
        // b. Translate to the first row and column position.
        canvas?.translate(columnOne,rowOne)
        // c. Draw by calling drawClippedRectangle().
        drawClippedRectangle(canvas)
        // d. Then restore the canvas to its previous state.
        canvas?.restore()
    }

    private fun drawDifferenceClippingExample(canvas: Canvas?) {
        //TODO: Step 2.1 Save the canvas.
        // Translate the origin of the canvas into open space to the first row, second column, to the right of the first rectangle.
        // Apply two clipping rectangles. The DIFFERENCE operator subtracts the second rectangle from the first one.
        canvas?.save()
        // Move the origin to the right for the next rectangle.
        canvas?.translate(columnTwo,rowOne)
        // Use the subtraction of two clipping rectangles to create a frame.
        canvas?.clipRect(
            2 * rectInset,2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )
        // The method clipRect(float, float, float, float, Region.Op
        // .DIFFERENCE) was deprecated in API level 26. The recommended
        // alternative method is clipOutRect(float, float, float, float),
        // which is currently available in API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            canvas?.clipRect(
                4 * rectInset,4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset,
                Region.Op.DIFFERENCE
            )
        else {
            canvas?.clipOutRect(
                4 * rectInset,4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset
            )
        }
        drawClippedRectangle(canvas)
        canvas?.restore()
    }

    private fun drawCircularClippingExample(canvas: Canvas?) {
        //TODO : Step 3.1 Implement the following code
        canvas?.save()
        canvas?.translate(columnOne,rowTwo)
        // Clears any lines and curves from the path but unlike reset(),
        // keeps the internal data structure for faster reuse.
        path.rewind()
        path.addCircle(
            circleRadius,clipRectBottom - circleRadius,
            circleRadius,Path.Direction.CCW)
        // The method clipPath(path, Region.Op.DIFFERENCE) was deprecated in
        // API level 26. The recommended alternative method is
        // clipOutPath(Path), which is currently available in
        // API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            canvas?.clipPath(path,Region.Op.DIFFERENCE)
        }else{
            canvas?.clipOutPath(path)
        }
        drawClippedRectangle(canvas)
        canvas?.restore()
    }
    private fun drawIntersectionClippingExample(canvas: Canvas?) {
        //TODO: Step 4.1 Implement drawIntersectionClippingExample() using the following code
        canvas?.save()
        canvas?.translate(columnTwo,rowTwo)
        canvas?.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight - smallRectOffset,
            clipRectBottom - smallRectOffset
        )
        // The method clipRect(float, float, float, float, Region.Op
        // .INTERSECT) was deprecated in API level 26. The recommended
        // alternative method is clipRect(float, float, float, float), which
        // is currently available in API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            canvas?.clipRect(
                clipRectLeft+smallRectOffset,
                clipRectTop+smallRectOffset,
                clipRectRight,clipRectBottom,
                Region.Op.INTERSECT
            )
        }else{
            canvas?.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight,clipRectBottom
            )
        }
        drawClippedRectangle(canvas)
        canvas?.restore()
    }
    private fun drawCombinedClippingExample(canvas: Canvas?) {
        //TODO: Step 5.1 Implement drawCombineClippingExample() method using the following code
        canvas?.save()
        canvas?.translate(columnOne,rowThree)
        path.rewind()
        path.addCircle(
            clipRectLeft + rectInset + circleRadius,
            clipRectTop + circleRadius + rectInset,
            circleRadius,Path.Direction.CCW
        )
        path.addRect(
            clipRectRight/2 - circleRadius,
            clipRectTop + circleRadius +rectInset,
            clipRectLeft/2 + circleRadius,
            clipRectBottom - rectInset,
            Path.Direction.CCW
        )
        canvas?.clipPath(path)
        drawClippedRectangle(canvas)
        canvas?.restore()
    }
    private fun drawRoundedRectangleClippingExample(canvas: Canvas?) {
        //TODO: Step 6.2 Implement the function drawRoundedRectangleClippingExample() using the following code
        canvas?.save()
        canvas?.translate(columnTwo,rowThree)
        path.rewind()
        path.addRoundRect(
            rectF,clipRectRight/4,
            clipRectRight/4,
            Path.Direction.CCW
        )
        canvas?.clipPath(path)
        drawClippedRectangle(canvas)
        canvas?.restore()
    }
    private fun drawOutsideClippingExample(canvas: Canvas?) {
        //TODO: Step 7.1 Implement the function drawOutsideClippingExample() using the following code
        canvas?.save()
        canvas?.translate(columnOne,rowFour)
        canvas?.clipRect(
            2 * rectInset, 2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 *rectInset
        )
        drawClippedRectangle(canvas)
        canvas?.restore()
    }
    private fun drawTranslatedTextExample(canvas: Canvas?) {
        //TODO: Step 8.1 Implement the function drawTranslatedTextExample() using the following code
        canvas?.save()
        paint.color = Color.GREEN
        // Align the RIGHT side of the text with the origin.
        paint.textAlign = Paint.Align.LEFT
        // Apply transformation to canvas.
        canvas?.translate(columnTwo,textRow)
        canvas?.drawText(
            context.getString(R.string.translated),
            clipRectLeft,
            clipRectTop,
            paint
        )
        canvas?.restore()
    }
    private fun drawSkewedTextExample(canvas: Canvas?) {
        //TODO: Step 9.1 Implement the function drawSkewedTextExample() using the following code
        canvas?.save()
        paint.color = Color.YELLOW
        paint.textAlign = Paint.Align.RIGHT
        // Position text.
        canvas?.translate(columnTwo, textRow)
        // Apply skew transformation.
        canvas?.skew(0.2f,0.3f)
        canvas?.drawText(
            context.getString(R.string.skewed),
            clipRectLeft,
            clipRectTop,
            paint
        )
        canvas?.restore()
    }
    private fun drawQuickRejectExample(canvas: Canvas?) {
        //TODO: Step 10.2 Add the following drawQuickRejectExample() function to ClippedView, and uncomment its invocation in onDraw().
        // Read the code, as it contains everything you need to know to use quickReject().
        val inClipRectangle = RectF(clipRectRight/2,
            clipRectBottom/2,
            clipRectRight * 2,
            clipRectBottom *2)

        val notInClipRectangle = RectF(
            RectF(clipRectRight+1,
            clipRectBottom+1,
            clipRectRight*2,
            clipRectBottom*2)
        )

        canvas?.save()
        canvas?.translate(columnOne,rejectRow)
        canvas?.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight,clipRectBottom
        )

        if (canvas?.quickReject(inClipRectangle, Canvas.EdgeType.AA)!!){
                canvas?.drawColor(Color.WHITE)
        }else{
            canvas?.drawColor(Color.BLACK)
            canvas?.drawRect(inClipRectangle,paint)
        }
        canvas?.restore()
    }

    /* Note for QuickReject
    * The quickReject() method allows you to check whether a specified rectangle or path would lie completely outside the currently visible regions, after all transformations have been applied.
    * The quickReject() method is incredibly useful when you are constructing more complex drawings and need to do so as fast as possible. With quickReject(), you can decide efficiently which objects you do not have to draw at all, and there is no need to write your own intersection logic.
    * The quickReject() method returns true if the rectangle or path would not be visible at all on the screen. For partial overlaps, you still have to do your own checking.
    * The EdgeType is either AA (Antialiased: Treat edges by rounding-out, because they may be antialiased) or BW (Black-White: Treat edges by just rounding to nearest pixel boundary) for just rounding to the nearest pixel.*/
}