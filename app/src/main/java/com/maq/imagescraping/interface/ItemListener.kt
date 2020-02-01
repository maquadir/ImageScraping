package com.noorlabs.calcularity.interfaces

import android.view.View
import com.maq.imagescraping.items.Item

interface ItemListener  {

    fun displayRecyclerView(data: ArrayList<Item>)
    fun displayToast(message:String)



}