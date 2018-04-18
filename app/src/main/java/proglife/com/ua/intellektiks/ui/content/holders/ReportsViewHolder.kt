package proglife.com.ua.intellektiks.ui.content.holders

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.li_lesson_footer.view.*
import kotlinx.android.synthetic.main.li_lesson_message.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.ReportMessage
import proglife.com.ua.intellektiks.ui.content.models.ReportsViewModel
import proglife.com.ua.intellektiks.utils.inflate
import java.text.SimpleDateFormat

class ReportsViewHolder(itemView: View, private val onReportAction: OnReportAction?) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SimpleDateFormat")
    private val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val messagesContainer: LinearLayout = itemView.messagesContainer
    private val tvReportStatus: TextView = itemView.tvReportStatus
    private val btnSendReport: ImageButton = itemView.btnReportSend
    private val etReportText: EditText = itemView.etReportText
    private val sendReportContainer: ViewGroup = itemView.sendReportContainer

    fun bind(reportsViewModel: ReportsViewModel?) {
        if (reportsViewModel == null) return
        messagesContainer.removeAllViews()
        reportsViewModel.messages.forEach {
            messagesContainer.addView(getMessageView(messagesContainer, it))
        }
        val status = reportsViewModel.getStatus()
        tvReportStatus.visibility = if (status == null) View.GONE else View.VISIBLE
        tvReportStatus.text = when (status) {
            ReportMessage.Status.REJECTED -> itemView.context.getString(R.string.report_rejected)
            ReportMessage.Status.APPROVED -> itemView.context.getString(R.string.report_approved)
            ReportMessage.Status.AWAIT_CHECK -> itemView.context.getString(R.string.report_await_check)
            else -> ""
        }
        val statusColor = Color.parseColor(when (status) {
            ReportMessage.Status.APPROVED -> "#008000"
            ReportMessage.Status.REJECTED -> "#FF0000"
            else -> "#222222"
        })

        sendReportContainer.visibility = if (status == null || status == ReportMessage.Status.REJECTED)
            View.VISIBLE else View.GONE

        tvReportStatus.setTextColor(statusColor)
        btnSendReport.setOnClickListener { onReportAction?.send(etReportText.text.toString()) }
    }

    private fun getMessageView(parent: ViewGroup, message: ReportMessage): View {
        val view = parent.inflate(R.layout.li_lesson_message)
        view.tvMessageDate.text = sdf.format(message.creationDate)
        view.tvMessageText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(message.message, 0) else Html.fromHtml(message.message)
        view.setBackgroundColor(if (message.type == ReportMessage.Type.EMPLOYEE)
            Color.TRANSPARENT else Color.parseColor("#E7F5FD"))
        return view
    }

    interface OnReportAction {
        fun send(message: String)
        fun typed(message: String)
    }

}