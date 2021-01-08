package com.dreame.reader.common.ui.widgets.edittext

interface LengthWatcher {
    fun onChanged(inputLength: Int, minLength: Int, maxLength: Int)
}