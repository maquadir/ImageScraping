package com.maq.imagescraping

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


        var index = viewModel.titleArray.indexOf(title)
        var points = viewModel.pointsArray[index]
        var score = viewModel.scoreArray[index]
        var topicId = viewModel.topicIdArray[index]
        var dateTime = viewModel.dateTimeArray[index]
        var imageArray = viewModel.imageArray
        searchData.add(Item(title,imageArray,points,score,topicId,dateTime))
        viewModel.displayRecyclerView(searchData)


    }

    private fun toggleFunction() {
        val toggle: ToggleButton = findViewById(R.id.toggleButton)
        Data = viewModel.Data
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
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
                var result = ToggleData.size.toString() + " entries displayed"
                viewModel.displayToast(result)
                viewModel.displayRecyclerView(ToggleData)
            } else {
                // The toggle is disabled
                var result = "All entries displayed"
                viewModel.displayToast(result)
                viewModel.displayRecyclerView(Data)
            }
        }
    }

    private fun setupUI() {
        //display progress loader
        findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE

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
        Log.i("Image","RecyclerView called")
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        findViewById<ProgressBar>(R.id.loader).visibility = View.INVISIBLE
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = ItemAdapter(
            data,
            applicationContext,this@MainActivity
        )
    }


}

