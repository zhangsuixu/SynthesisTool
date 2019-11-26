package com.synthesistool.view

import com.service.bean.MainPageParam

interface MainDraggerView {
    fun refreshView(response: MainPageParam)

    fun initDataFail()
}