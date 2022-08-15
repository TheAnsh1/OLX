package com.example.olxapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.example.olxapp.R
import com.example.olxapp.model.CategoriesModel
import de.hdodenhof.circleimageview.CircleImageView

class CategoriesAdapter(var categoriesList:MutableList<CategoriesModel>, var itemClickListener: IemClickListener): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {


private lateinit var context:Context
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            context=parent.context
            val viewHolder=LayoutInflater.from(parent.context).inflate(R.layout.adapter_categries,parent,false)
            return ViewHolder(viewHolder)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.textViewTittle.setText(categoriesList.get(position).key)
            Glide.with(context)
                .load(categoriesList.get(position).image)
                .placeholder(R.drawable.car)
                .into(holder.imageView)
            holder.itemView.setOnClickListener(View.OnClickListener {
                itemClickListener.onItemClick(position)
             })
        }

        override fun getItemCount(): Int {
        return categoriesList.size
        }

    fun updateList(temp: MutableList<CategoriesModel>) {
        categoriesList=temp
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textViewTittle=itemView.findViewById<TextView>(R.id.circle_name)
            val imageView=itemView.findViewById<CircleImageView>(R.id.circle)
    }
    interface IemClickListener{
        fun onItemClick(position:Int)
    }
}

