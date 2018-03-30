package proglife.com.ua.intellektiks.ui.goods

import com.arellomobile.mvp.InjectViewState
import proglife.com.ua.intellektiks.business.CommonInteractor
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.extensions.DownloadableFile
import proglife.com.ua.intellektiks.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class GoodsShowPresenter(goodsPreview: GoodsPreview) : BasePresenter<GoodsShowView>() {

    fun progress(files: List<DownloadableFile>?) {
        if (files == null) return
        val total: Int = files.size
        val count: Int = files.filter { it.state == DownloadableFile.State.FINISHED }.size
        val progress: Int? = files.firstOrNull { it.state == DownloadableFile.State.PROCESSING }?.progress
        viewState.showProgress(if (count < total) count + 1 else total, total, progress)
    }

    @Inject
    lateinit var mCommonInteractor: CommonInteractor

    init {
        injector().inject(this)
        viewState.showInfo(goodsPreview)
        mCommonInteractor.getGoods(goodsPreview.id)
                .compose(sAsync())
                .doOnSubscribe { viewState.showLoading() }
                .doOnEvent { _, _ -> viewState.dismissLoading() }
                .subscribe(
                        {
                            viewState.showGoods(it)
                        },
                        {}
                )
    }

}