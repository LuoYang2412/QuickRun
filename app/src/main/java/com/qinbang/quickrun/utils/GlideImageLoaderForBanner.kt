package com.qinbang.quickrun.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

/**
 * Banner图片加载器
 */
class GlideImageLoaderForBanner : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        imageView!!.scaleType = ImageView.ScaleType.FIT_CENTER
        Glide.with(context!!).load(path).into(imageView)
    }

    override fun createImageView(context: Context?): ImageView {
        return super.createImageView(context)
    }
}