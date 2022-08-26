package com.example.olxapp.ui.details.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.olxapp.R

class DetailImagesAdapter(var context:Context ,
     private val imageList :ArrayList<String>,
     var onItemClickListener: onItemClick):PagerAdapter() {
    private var inflater:LayoutInflater?=null
    private var doNotifyDataSetChangeOnce =false

    init {
      inflater = LayoutInflater.from(context)
    }
    override fun getCount(): Int {
        if(doNotifyDataSetChangeOnce){
            doNotifyDataSetChangeOnce =false
            notifyDataSetChanged()
        }
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater?.inflate(R.layout.adapter_image_details,container,false)
        val imageView  =view?.findViewById<ImageView>(R.id.imageView)

        imageView?.setOnClickListener(View.OnClickListener {
            onItemClickListener.onClick(position)
        })
        Glide.with(context)
            .load(imageList.get(position))
            .placeholder(R.drawable.big_placeholder)
            .into(imageView!!)
        container.addView(view,0)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view == `object`
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }
    interface onItemClick{
        fun onClick(position: Int)
    }
}