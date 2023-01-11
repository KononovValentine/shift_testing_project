package com.example.shifttestingproject.fragments.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class WhatCallNumberDialogFragment(
    private val title: String,
    private val items: Array<String>
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)
            alertBuilder.setTitle(title)
            alertBuilder.setItems(items, DialogInterface.OnClickListener { dialog, index ->
                val phoneIntent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:${items[index]}")
                )
                startActivity(phoneIntent)
            })
            alertBuilder.create()
        } ?: throw IllegalStateException("Exception! Activity is null!")
    }
}
