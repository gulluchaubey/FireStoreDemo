package com.example.firestoredemo.home.homeFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firestoredemo.R

class HomeAdapter(var context:Context,var homeDataList:ArrayList<HashMap<String,String>>,var clickHandler:(position: Int)-> Unit):
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {


    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {

        var subImg = itemView.findViewById<ImageView>(R.id.ivSubImg)
        var subName = itemView.findViewById<TextView>(R.id.tvSubName)

        fun setData(pos: Int, hashList: HashMap<String,String>){
            Glide.with(context)
                .load(hashList["icon"])
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .apply(RequestOptions.circleCropTransform())
                .into(subImg)
            subName.text=hashList["name"]

            itemView.setOnClickListener {
                clickHandler.invoke(pos)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(position,homeDataList[position])
    }

    override fun getItemCount(): Int {
       return homeDataList.size
    }
}