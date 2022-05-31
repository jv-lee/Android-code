package com.lee.app.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.lee.app.BottomTitleDialog
import com.lee.app.R
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.item.ViewItem

/**
 * 每一个item持有一个dialog 解决方案
 * @author jv.lee
 * @date 2020/9/7
 */
class FormItem : ViewItem<String>() {

    override fun getItemView(context: Context, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_form, parent, false)
    }

    override fun convert(holder: BaseViewHolder, entity: String, position: Int) {
        val dialog = getDialog2(holder, entity, position)

        val editText = holder.getView<EditText>(R.id.et_input)
        editText?.setText("this is content position $position")

        val btnShow = holder.getView<Button>(R.id.btn_show)
        btnShow.setOnClickListener {
            dialog.show()
        }

    }

    /**
     * 解决方式一
     */
    private fun getDialog1(context: Context, entity: String, position: Int): Dialog {
        var dialog = FormAdapter.dialogMap[position]
        if (dialog == null) {
            dialog = BottomTitleDialog(context)
            dialog.setContentTitle("title - > $position")
        }
        return dialog
    }

    /**
     * 建议解决方式二 更加优雅
     */
    private fun getDialog2(holder: BaseViewHolder, entity: String, position: Int): Dialog {
        var dialog = holder.convertView.getTag(entity.hashCode())
        if (dialog == null) {
            dialog = BottomTitleDialog(holder.convertView.context)
            dialog.setContentTitle("title - > $position")
            holder.convertView.setTag(entity.hashCode(), dialog)
        }
        return dialog as Dialog
    }

}