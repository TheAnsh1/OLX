package com.example.olxapp.ui.browseCategory.browseadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olxapp.R
import com.example.olxapp.model.DataItemModel
import java.text.SimpleDateFormat

class BrowseCategoryAdapter(var dataItemModel: MutableList<DataItemModel>, var mClickListener:ItemClickListener) : RecyclerView.Adapter<BrowseCategoryAdapter.ViewHolder>() {



    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val viewHolder=
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_my_ads,parent,false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewPrice.setText(dataItemModel.get(position).price)
        holder.textViewBrand.setText(dataItemModel.get(position).brand)
        holder.textViewAddress.setText(dataItemModel.get(position).address)

        Glide.with(context)
            .load(dataItemModel.get(position).images.get(0))
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageView)
try {
    val adf = SimpleDateFormat("dd/MM/yyyy")
    val formattedDate = adf.format(dataItemModel.get(position).createdDate?.time!!)
    holder.textViewDate.setText(formattedDate)
}catch (e:Exception){
    println(e)
}

        holder.imageView.setOnClickListener(View.OnClickListener {
            mClickListener.onItemClick(position)
        })
    }

    override fun getItemCount(): Int {
        return dataItemModel.size
    }

    fun updateList(temp: MutableList<DataItemModel>) {
        dataItemModel=temp
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewPrice=itemView.findViewById<TextView>(R.id.tvprice)
        val imageView=itemView.findViewById<ImageView>(R.id.imageView)
        val textViewBrand=itemView.findViewById<TextView>(R.id.tvBrand)
        val textViewAddress=itemView.findViewById<TextView>(R.id.tvAddress)
      val textViewDate= itemView.findViewById<TextView>(R.id.tvDate)
    }
    interface ItemClickListener{
        fun onItemClick(position:Int)
    }
}

