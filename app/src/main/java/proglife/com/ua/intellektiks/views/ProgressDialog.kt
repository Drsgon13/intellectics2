package proglife.com.ua.intellektiks.views


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Window
import android.widget.ProgressBar
import proglife.com.ua.intellektiks.R

class ProgressDialog(context: Context) {

    private val dialog: AlertDialog?

    init {
        val progressBar = ProgressBar(context)
        progressBar.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(context, R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.MULTIPLY)
        dialog = AlertDialog.Builder(context)
                .setView(ProgressBar(context))
                .create()
        dialog!!.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show() {
        if (dialog != null && dialog.isShowing) dialog.dismiss()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}
