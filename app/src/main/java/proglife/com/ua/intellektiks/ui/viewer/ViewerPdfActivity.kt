package proglife.com.ua.intellektiks.ui.viewer

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_viewer_pdf.*
import kotlinx.android.synthetic.main.content_main.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.ui.base.BaseActivity
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL

/**
 * Created by Evhenyi Shcherbyna on 04.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ViewerPdfActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_viewer_pdf)

        val title = intent.getStringExtra(Constants.Field.TITLE)
        val urlString = intent.getStringExtra(Constants.Field.CONTENT)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        supportActionBar?.title = title

        Single
                .create<ByteArray> {
                    var input: InputStream? = null
                    try {
                        val url = URL(urlString)
                        val connection = url.openConnection()
                        connection.connect()
                        input = connection.getInputStream()
                        it.onSuccess(input.readBytes())
                    } catch (e: IOException) {
                        e.printStackTrace()
                        it.onError(e)
                    } finally {
                        input?.close()
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { pbLoading.visibility = View.VISIBLE }
                .doOnEvent { _, _ -> pbLoading.visibility = View.GONE }
                .subscribe(
                        {
                            pdfView.fromBytes(it)
                                    .onPageError { page, t ->
                                        t.message?.let { Snackbar.make(coordinator, it, Snackbar.LENGTH_INDEFINITE).show() }
                                    }
                                    .onLoad {  }
                                    .load()
                        },
                        {
                            Snackbar.make(coordinator, R.string.error_loading, Snackbar.LENGTH_INDEFINITE).show()
                        }
                )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }
}