package com.princeakash.projectified.utils

import android.content.Context
import android.view.ViewGroup
import com.google.android.material.chip.Chip

object HelperClass {

    fun getChipsArray(context: Context) : Array<Chip> {
        return Array(languages.size) { i -> generateChip(i, context) }
    }

    private fun generateChip(index: Int, context: Context): Chip{
        val chip = Chip(context)
        chip.isCheckable = true
        chip.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        chip.text = languages[index]
        return chip
    }

    val languages = arrayOf("C", "C++", "Java", "Python", "Kotlin", "Javascript")
    const val CHIPS = "Chips"
    const val TAG = "Hello"
}