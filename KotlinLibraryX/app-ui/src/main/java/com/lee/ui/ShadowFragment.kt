package com.lee.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.shape.*
import com.lee.library.widget.nav.NumberDotView

/**
 * @author jv.lee
 * @date 2021/1/14
 * @description
 */
class ShadowFragment : Fragment(R.layout.fragment_shadow) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<NumberDotView>(R.id.numberDotView).setNumber(12)

        setMessageShape()
    }

    private fun setMessageShape() {
        val shapeModel = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(20f)
            setRightEdge(object : TriangleEdgeTreatment(20f, false) {
                override fun getEdgePath(
                    length: Float,
                    center: Float,
                    interpolation: Float,
                    shapePath: ShapePath
                ) {
                    super.getEdgePath(length, 35f, interpolation, shapePath)
                }
            })
        }.build()
        val drawable = MaterialShapeDrawable(shapeModel).apply {
            setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            paintStyle = Paint.Style.FILL
        }
        view?.findViewById<TextView>(R.id.tv_message_shape)?.apply {
            //不限制子view在父容器范围内 所以角标可以延伸出去
            (parent as ViewGroup).clipChildren = false
            background = drawable
        }

    }

}