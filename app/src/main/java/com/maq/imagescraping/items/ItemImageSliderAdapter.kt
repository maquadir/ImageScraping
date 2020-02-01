package com.maq.imagescraping.items

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.maq.imagescraping.R
import com.maq.propertyapp.network.RecyclerViewClickListener
import kotlinx.android.synthetic.main.image_slider_item.view.*


//class to display multiple images in an imageview

class ItemImageSliderAdapter(private val context: Context, private val images:List<String>, private val listener: RecyclerViewClickListener, val itemList: ArrayList<Item>) : PagerAdapter() {


    private var inflater: LayoutInflater? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }

    override fun getCount(): Int {

        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.image_slider_item, null)

        //use glide to load image url into imageview
        Glide.with(context)
            .load(images[position])
            .into( view.imageView_slide)

        view.setOnClickListener {
                Log.i("TAG", "This page was clicked:")
            listener.onRecyclerViewItemClick(view,itemList[position].toString())

        }


        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}