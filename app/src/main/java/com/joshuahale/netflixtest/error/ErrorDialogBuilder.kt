package com.joshuahale.netflixtest.error

import android.app.AlertDialog
import android.content.Context
import com.joshuahale.netflixtest.R
import com.joshuahale.netflixtest.error.interfaces.OnErrorDialogListener

class ErrorDialogBuilder{

    companion object {

        fun getErrorDialog(context: Context, message: String, listener: OnErrorDialogListener) {
            AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.error_title))
                .setMessage(message)
                .setNegativeButton(R.string.ok, null)
                .setPositiveButton(R.string.retry) { _, _ ->listener.onRetryClicked() }
                .show()
        }
    }
}