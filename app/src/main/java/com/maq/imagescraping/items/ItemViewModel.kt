package com.maq.propertyapp.properties

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maq.imagescraping.items.Item
import com.noorlabs.calcularity.interfaces.ItemListener

import kotlinx.coroutines.Job
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ItemViewModel : ViewModel() {

    private lateinit var job: Job
    var i:Int = 0
    var j:Int = 0
    var imagesArr: JSONArray? = null
    var Data = ArrayList<Item>()
    var imageArray =  ArrayList<String>()
    var finalImageArray =  ArrayList<String>()
    var itemListener: ItemListener? = null
    val titleArray = ArrayList<String>()
    val pointsArray = ArrayList<String>()
    val scoreArray = ArrayList<String>()
    val topicIdArray = ArrayList<String>()
    val dateTimeArray = ArrayList<String>()
    val imageMap = mutableMapOf<String, String>()

    private val _items = MutableLiveData<String>()
    val items : LiveData<String>
                get() = _items

    init{
        Log.i("View Model","View Model created")
    }

    //to keep track of lifetime of view model
    override fun onCleared() {
        super.onCleared()
        Log.i("View Model","View Model destroyed")
        if(::job.isInitialized) job.cancel()
    }

    //coroutines - get data from repository
    //fetching properties in iO thread and making changes to live data in main thread
     fun getItems(){

//            job =
//                viewModelScope.launch {
//
//                    Coroutines.ioThenMain(
//                        { repository.getItems() },
//                        { _items.value = it }
//                    )
//                }

    }

    fun displayToast( message:String){
       itemListener?.displayToast(message)
    }

    fun fetchData(activity: Activity) {

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val request = Request.Builder()
            .url("https://api.imgur.com/3/gallery//top/viral/week")
            .header("Authorization", "Client-ID c25479844675ee7")
            .header("User-Agent", "Epicture")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "An error has occurred $e")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    val data = JSONObject(response.body()!!.string())
                    val items = data.getJSONArray("data")

                    while(i < items.length()){
                        val item = items.getJSONObject(i)

                        val title = item["title"]
                        titleArray.add(title.toString())

                        val points = item["points"]
                        pointsArray.add(points.toString())

                        val score = item["score"]
                        scoreArray.add(score.toString())

                        val topic_id = item["topic_id"]
                        topicIdArray.add(topic_id.toString())

                        val dateTime = item["datetime"]
                        dateTimeArray.add(dateTime.toString())

                        try {
                            imagesArr = item.getJSONArray("images")
                            j=0
                            imageArray.clear()
                            while(j < imagesArr!!.length()){
                                val image = imagesArr!!.getJSONObject(j)

                                val link = image["link"]
                                imageArray.add(link.toString())

                                j++
                            }


                        }catch(e: JSONException) {
                            Log.i("Exception",e.toString())
                        }

                        var finalImageArray = imageArray.toList()
                        val FinalItem =  Item(title.toString(),
                            finalImageArray , points.toString(),
                            score.toString(), topic_id.toString(), dateTime.toString()
                        )

                        Data.add(FinalItem)

                        i++
                    }

                    Log.i("Image","Success "+i.toString()+","+j.toString())


                } catch (e: JSONException) {
                    Log.i("Image","Exception")
                    e.printStackTrace()
                }

                activity.runOnUiThread(java.lang.Runnable {
                    displayRecyclerView(Data)
                })


            }
        })
    }

    fun displayRecyclerView(data: ArrayList<Item>) {
       itemListener?.displayRecyclerView(data)
    }





}