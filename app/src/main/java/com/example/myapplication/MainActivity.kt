package com.example.myapplication

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var data = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            fillList()
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }
        else{
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        }


        recyclerView.adapter = CustomRecyclerAdapter(data)
    }

    private fun fillList(): List<String> {
        (0..10).forEach { i -> data.add("\$i element") }
        return data
    }
}