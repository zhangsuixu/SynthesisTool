package com.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.RelativeLayout
import android.widget.TextView
import com.common.tools.DensityUtils
import com.service.bean.MainPageParam
import com.ui.R
import com.ui.widget.rvadapter.RvCommonAdapter
import com.ui.widget.rvadapter.ViewHolder

class MainBottomAdapter(context: Context, data: List<MainPageParam.MainBottomParam>?) : RvCommonAdapter<MainPageParam.MainBottomParam>(context, R.layout.rv_bottom_item, data) {

    private val screenWidth = DensityUtils.getWidth()

    private var listener: OnChooseListener? = null

    /** 选中位置 */
    fun select(position: Int) {
        for (indexBottom in mDatas) {
            indexBottom.isSelected = false
        }
        mDatas[position].isSelected = true
        notifyDataSetChanged()
    }

    fun refreshView(data: List<MainPageParam.MainBottomParam>?) {
        if (null == data) {
            return
        }

        mDatas.clear()
        mDatas.addAll(data)
        notifyDataSetChanged()
    }

    override fun convert(holder: ViewHolder, item: MainPageParam.MainBottomParam, position: Int) {

        val rlBottomContainer = holder.getView<RelativeLayout>(R.id.rl_bottom_container)
        val tvTitle = holder.getView<TextView>(R.id.tv_title)
        rlBottomContainer.layoutParams.width = screenWidth / mDatas.size
        tvTitle.text = item.name

        if (item.isSelected) {
            holder.setImageResource(R.id.iv_icon, item.selectedId)
            tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_121C32))
        } else {
            holder.setImageResource(R.id.iv_icon, item.unselectedId)
            tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_BCBCBC))
        }

        if (position != itemCount - 1) {
            holder.setVisible(R.id.view_line, true)
        } else {
            holder.setVisible(R.id.view_line, false)
        }

        holder.setOnClickListener(R.id.rl_bottom_container) {
            select(position)
            listener?.onChoose(position)
        }
    }

    fun setOnChooseListener(listener: OnChooseListener) {
        this.listener = listener
    }

    interface OnChooseListener {
        fun onChoose(position: Int)
    }
}
