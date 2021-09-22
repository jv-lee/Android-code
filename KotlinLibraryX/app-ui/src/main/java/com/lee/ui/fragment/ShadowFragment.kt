package com.lee.ui.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.shape.*
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding
import com.lee.library.widget.nav.NumberDotView
import com.lee.ui.R
import com.lee.ui.databinding.FragmentShadowBinding

/**
 * @author jv.lee
 * @date 2021/1/14
 * @description
 */
class ShadowFragment : BaseFragment(R.layout.fragment_shadow) {

    private val binding by binding(FragmentShadowBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<NumberDotView>(R.id.numberDotView).setNumber(12)

        setMessageShape()
    }

    override fun bindView() {
        binding.numberDotView.setNumber(12)
        setMessageShape()
    }

    override fun bindData() {

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

        binding.tvMessageShape.apply {
            //不限制子view在父容器范围内 所以角标可以延伸出去
            (parent as ViewGroup).clipChildren = false
            background = drawable
        }

    }

}