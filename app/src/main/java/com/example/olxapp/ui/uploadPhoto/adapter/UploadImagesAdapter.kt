package com.example.olxapp.ui.uploadPhoto.adapter

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.olxapp.R

class UploadImagesAdapter(internal var activity:Activity,
                          internal var imagesArrayList:ArrayList<String>,
                          internal var itemClick:ItemClickListener
                         ): RecyclerView.Adapter<UploadImagesAdapter.ViewHolder>() {


    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val viewHolder=
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_upload_image,parent,false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position<imagesArrayList.size){
            val bitmap=BitmapFactory.decodeFile(imagesArrayList[position])
            holder.imageView.setImageBitmap(bitmap)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            if (position==imagesArrayList.size){
                itemClick.onItemClick()
            }
        })
    }

    override fun getItemCount(): Int {
        return imagesArrayList.size+1
    }

    fun updateList(temp: ArrayList<String>) {
        imagesArrayList=temp
        notifyDataSetChanged()
    }

    fun customNotify(selectedImagesArrayList: java.util.ArrayList<String>) {
        this.imagesArrayList=selectedImagesArrayList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView=itemView.findViewById<ImageView>(R.id.imageView)
    }
    interface ItemClickListener{
        fun onItemClick()
    }
}
