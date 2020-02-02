package com.maq.imagescraping

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.maq.imagescraping.databinding.ActivityMainBinding
import com.maq.imagescraping.items.Item
import com.maq.propertyapp.network.RecyclerViewClickListener
import com.maq.propertyapp.properties.ItemAdapter
import com.maq.propertyapp.properties.ItemViewModel
import com.maq.propertyapp.properties.ItemViewModelFactory
import com.noorlabs.calcularity.interfaces.ItemListener
import kotlinx.android.synthetic.main.custom_toast.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), RecyclerViewClickListener, ItemListener {

    var Data = ArrayList<Item>()
    var searchData = ArrayList<Item>()
    var ToggleData = ArrayList<Item>()
    private  lateinit var viewModel: ItemViewModel
    private lateinit var factory: ItemViewModelFactory
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //databinding
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        checkNetwork()

        setupViewModel()

        setupUI()

        toggleFunction()


    }

    @SuppressLint("NewApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.actionbar_menu, menu)

        //search view bar and item
        val searchItem = menu?.findItem(R.id.search_location)
        val searchView = searchItem?.actionView as SearchView

        //search for a note based on contained text or user-name
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                //convert all notes or username to lowercase
                when (newText) {
                    in viewModel.titleArray -> searchTitle(newText)
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })

        //when close button of search view is clicked
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {



                return false
            }


        })

        return true
    }

    //function to search map based on username
    private fun searchTitle(title: String) {


        val index = viewModel.titleArray.indexOf(title)
        val points = viewModel.pointsArray[index]
        val score = viewModel.scoreArray[index]
        val topicId = viewModel.topicIdArray[index]
        val dateTime = viewModel.dateTimeArray[index]
        val imageArray = viewModel.imageArray
        searchData.add(Item(title,imageArray,points,score,topicId,dateTime))
        viewModel.displayRecyclerView(searchData)


    }

    private fun toggleFunction() {
        Data = viewModel.Data
        binding.toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If toggle is enabled, the app should only display results where the sum of “points”,
                //“score” and “topic_id” adds up to an even number
                var i = 0
                ToggleData.clear()
                while(i < Data.size){
                    val points = Data[i].points.toInt()
                    val score = Data[i].score.toInt()
                    val topic_id = Data[i].topic_id.toInt()

                    val total = points + score + topic_id
                    if(total % 2 == 0){
                        ToggleData.add(Data[i])
                    }
                    i++
                }

                Collections.reverse(ToggleData)
                val result = ToggleData.size.toString() + " entries displayed"
                viewModel.displayToast(result)
                viewModel.displayRecyclerView(ToggleData)
            } else {
                // If the toggle is disabled, the app should display all results
                val result = "All entries displayed"
                viewModel.displayToast(result)
                viewModel.displayRecyclerView(Data)
            }
        }
    }

    private fun setupUI() {
        //display progress loader
        binding.loader.visibility = View.VISIBLE

        //fetch data from Imghur API and display in view
        viewModel.fetchData(this)
    }

    private fun checkNetwork() {
        //check if phone is connected to internet
        if(isNetworkConnected() == false){
            viewModel.displayToast("Connect your phone to internet and click refresh")
            return
        }
    }

    private fun setupViewModel() {
        factory =
            ItemViewModelFactory()
        viewModel = ViewModelProviders.of(this, factory).get(ItemViewModel::class.java)

        //setup databinding
        binding.viewModel = viewModel

        //setup listener
        viewModel.itemListener = this
    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }

    override fun displayToast(message:String){

        val layout = layoutInflater.inflate(R.layout.custom_toast,linearLayout)
        val myToast = Toast(applicationContext)
        myToast.setGravity(Gravity.TOP,0,200)
        myToast.view = layout
        val toastText = layout.findViewById(R.id.custom_toast_message) as TextView
        toastText.text = message

        myToast.show()
    }

    override fun onRecyclerViewItemClick(view: View, item: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayRecyclerView(data: ArrayList<Item>) {
        binding.loader.visibility = View.INVISIBLE
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.setHasFixedSize(true)

        binding.recyclerView.adapter = ItemAdapter(
            data,
            applicationContext,this@MainActivity
        )
    }


}

