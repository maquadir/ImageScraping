# ImghurApp
This is an Android Application written in Kotlin that searches for the top images of the week from the Imgur gallery and
display it in a list.The results are sorted in reverse chronological order.

# Installation
Clone the repo and install the dependencies.
        
      git clone git@github.com:maquadir/ImageScraping.git
      
# Architecture and Design
The application follows an MVVM architecture as given below

<img width="449" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/71425127-6ca3cc00-26ed-11ea-98b5-a344b54b7050.png">

# Requirements
- Searches for the top images of the week from the Imgur gallery and display it in a list.
- The app needs to provide the user the ability to input text as part of the search query before displaying relevant results.
  - Right now we have provided the user to input title to fetch results. More input features can be added in future.
- Each cell needs to display the following for each search result:
  - title
  - date of post in local time (DD/MM/YYYY h:mm a)
  - number of additional images in post (if there are multiple)
  - image
- Toggle feature to change the display results.
  - If toggle is enabled, the app should only display results where the sum of “points”,“score” and “topic_id” adds up to an even number
  - If the toggle is disabled, the app should display all results
      
# Setup
### Manifest File
- Since the app is going to fetch from Imghur API .We have to add the following internet permissions to the manifest file.
    
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 
- The app supports orientation change and adapts to both portrait and landscape modes by mentioning screen orientation as 'sensor' which detects screen change and adapts its layout.

         android:screenOrientation="sensor"
         
- Information related to Imghur API can be found here https://api.imgur.com/
    
### Material Styling
- A progress bar is displayed during the Request Builder read operation.
- A CardView to display the details with rounded corners and a background shadow 
- Montserrat Font styling for texts

### Check Network Connection
We check if the phone is connected to internet. If not we display a toast message.

           private fun isNetworkConnected(): Boolean {
                  val cm =
                      getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                  return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
              }

### Invoke Imghur API Url with top images of the week using Request.Builder()
We have declared an Http Client  which will call the request to invoke the Imghur API url using Request.Builder()


         val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

- From the Imghur API website we use URL https://api.imgur.com/3/gallery//top/viral/week to fetch the top viral images of the week from the Imghur Gallery

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
               
            }
        })
            
### Model
A Model contains all the data classes, database classes, API and repository.
An Item data class is used to map the API data to Kotlin. 

             data class Item(
                 val title: String,
                 var images: List<String>,
                 var points:String,
                 var score:String,
                 var topic_id:String,
                 var dateTime:String
                )
                
### Data Binding
The Data Binding Library is an Android Jetpack library that allows you to bind UI components in your XML layouts to data sources in your app using a declarative format rather than programmatically.All the UIView elements in the layout are binded to views through data binding.
                
            //databinding
            binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
             )


### View Model
We set up a view model factory which is responsible for creating view models.It contains the data required in the View and translates the data which is stored in Model which then can be present inside a View. ViewModel and View are connected through Databinding and the observable Livedata.


        factory =
            ItemViewModelFactory()
        viewModel = ViewModelProviders.of(this, factory).get(ItemViewModel::class.java)

        //setup databinding
        binding.viewModel = viewModel

- We attach a listener as a callback from the viewmodel.

        //setup listener
        viewModel.itemListener = this

### View
It is the UI that represents the current state of information that is visible to the user.A Recycler View displays the data read from the API. We setup a recycler view adapter to take care of displaying the data on the view.

- The View model observes any data change and updates the adapter.We get the result in onResponse method of okHttpClient call.

      //display progress loader
        findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE

        //fetch data from Imghur API and display in view
        viewModel.fetchData(this)
        
        recyclerView.adapter = ItemAdapter(
            data,
            applicationContext,this@MainActivity
        )
        
- We display the following features in the recyclerview item
  - title
  - date of post in local time (DD/MM/YYYY h:mm a)
  - number of additional images in post (if there are multiple)
  - image
  
- The recyclerview uses an image slider adapter to display multiple images in an imageview along with a page indictor.
        
        holder.listViewItemBinding.viewpager.adapter =
            ItemImageSliderAdapter(
                context,
                imageUrls
            )
        holder.listViewItemBinding.indicator.setViewPager(holder.listViewItemBinding.viewpager)
        
- We use Glide to display profile image using data binding(Not used at the moment)
      
      @BindingAdapter("image")
      fun loadImage(view: ImageView, url: String) {
          Glide.with(view)
              .load(url)
              .into(view)
      }

### Build Gradle
We declare the respective dependencies 

    implementation 'com.squareup.okhttp3:logging-interceptor:3.9.1'
    implementation 'com.android.support:design:28.0.0'

    implementation 'org.jsoup:jsoup:1.11.3'
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3"

    //Glide
    implementation 'com.github.bumptech.glide:glide:4.10.0'

    //recycler view and card view
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'com.android.support:cardview-v7:28.0.0'
    
    //Material design
    implementation 'com.google.android.material:material:1.0.0'

    // ViewModel and LiveData
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"

    //New Material Design
    implementation 'com.google.android.material:material:1.2.0-alpha04'

    //Kotlin Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1'
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3"

    //Viewpage indicators
    compile ('com.github.JakeWharton:ViewPagerIndicator:2.4.1')  {
        exclude group: 'com.android.support'
        exclude module: 'appcompat-v7'
        exclude module: 'support-v4'
    }

# Screenshots
<img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/73607664-1ccdf400-460d-11ea-9183-789862e3aa14.jpeg"> <img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/73607665-1ccdf400-460d-11ea-9a9f-4e1281fff81b.jpeg"> <img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/73607666-1ccdf400-460d-11ea-827d-016e9db9492c.jpeg">

# Generating signed APK
From Android Studio:

- Build menu
- Generate Signed APK...

# Support
- Stack Overflow
- Udacity



