package com.maq.imagescraping.items

//JSON to Kotlin Class

data class Item(
    val title: String,
    var images: List<String>,
    var points:String,
    var score:String,
    var topic_id:String,
    var dateTime:String
)






