package com.maq.propertyapp.properties

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maq.imagescraping.R
import com.maq.imagescraping.databinding.ListViewItemBinding
import com.maq.imagescraping.items.Item
import com.maq.imagescraping.items.ItemImageSliderAdapter
import com.maq.propertyapp.network.RecyclerViewClickListener
import retrofit2.http.Url

class ItemAdapter(
    val dataList: ArrayList<Item>, val context: Context, private val listener:RecyclerViewClickListener
                      ): RecyclerView.Adapter<ItemAdapter.PropertyViewHolder>() {


    lateinit var imageUrls:List<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
         PropertyViewHolder(
             DataBindingUtil.inflate(
                 LayoutInflater.from(parent.context),
                 R.layout.list_view_item,
                 parent,
                 false
             )
        )

    override fun getItemCount() = dataList.size


    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {

        holder.listViewItemBinding.title.text = dataList[position].title
        holder.listViewItemBinding.date.text = dataList[position].dateTime
        imageUrls = dataList[position].images

        //setup imageslider to display multiple images in imageview
        holder.listViewItemBinding.viewpager.adapter =
            ItemImageSliderAdapter(
                context,
                imageUrls,
                listener,
                dataList
            )
        holder.listViewItemBinding.indicator.setViewPager(holder.listViewItemBinding.viewpager)


    }

   inner class PropertyViewHolder(val listViewItemBinding: ListViewItemBinding) : RecyclerView.ViewHolder(listViewItemBinding.root)


}

