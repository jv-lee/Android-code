package com.lee.app.adapter

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import com.lee.app.BottomTitleDialog
import com.lee.app.R
import com.lee.library.adapter.LeeViewHolder
import com.lee.library.adapter.listener.LeeViewItem

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description 每一个item持有一个dialog 解决方案
 */
class FormItem : LeeViewItem<String> {

    override fun getItemLayout(): Int {
        return R.layout.item_form
    }

    override fun openClick(): Boolean {
        return true
    }

    override fun openShake(): Boolean {
        return true
    }

    override fun openRecycler(): Boolean {
        return false
    }

    override fun isItemView(entity: String?, position: Int): Boolean {
        return entity != null
    }

    override fun convert(holder: LeeViewHolder?, entity: String?, position: Int) {
        holder ?: return
        entity ?: return

//        initDialog(holder.convertView.context, entity, position)
        val dialog = getDialog(holder, entity, position)

        val editText = holder.getView<EditText>(R.id.et_input)
        editText?.setText("this is content position $position")

        val btnShow = holder.getView<Button>(R.id.btn_show)
        btnShow.setOnClickListener {
//            FormAdapter.dialogMap[position]?.show()
            dialog.show()
        }

    }

    override fun viewRecycled(holder: LeeViewHolder?, entity: String?, position: Int) {
    }

    /**
     * 解决方式一
     */
    private fun initDialog(context: Context, entity: String, position: Int) {
        var dialog = FormAdapter.dialogMap[position]
        if (dialog == null) {
            dialog = BottomTitleDialog(context)
            dialog.setContentTitle("title - > $position")
            FormAdapter.dialogMap[position] = dialog
        }

    }

    /**
     * 建议解决方式二 更加优雅
     */
    private fun getDialog(holder: LeeViewHolder, entity: String, position: Int): Dialog {
        var dialog = holder.convertView.getTag(entity.hashCode())
        if (dialog == null) {
            dialog = BottomTitleDialog(holder.convertView.context)
            dialog.setContentTitle("title - > $position")
            holder.convertView.setTag(entity.hashCode(), dialog)
        }
        return dialog as Dialog
    }

}