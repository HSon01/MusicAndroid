package com.ssn.sxmusic.extenstion


import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ssn.sxmusic.R

@BindingAdapter("setImage")
fun setImage(Img: ImageView, link: String) {
    Glide.with(Img)
        .load(link)
        .placeholder(R.mipmap.ic_launcher_round)
        .centerCrop()
        .timeout(4000)
        .error(R.drawable.logo_app_removebg)
        .into(Img)
}

