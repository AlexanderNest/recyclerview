package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    var data = arrayListOf<String>()
    var currentId = 0
    var poolOfRemoved = arrayListOf<String>()
    lateinit var cor : Job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }
        else{
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        }

        recyclerView.adapter = CustomRecyclerAdapter(data)

        var dat = savedInstanceState?.getStringArrayList("data")
        if (dat != null) {
            for (i in dat){
                data.add(i)
            }
        }
        recyclerView.adapter?.notifyDataSetChanged()

        autoAdd()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        var id = savedInstanceState.getInt("currentId")
        currentId = id
        poolOfRemoved = savedInstanceState.getStringArrayList("removed") as ArrayList<String>
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("data", data)
        outState.putInt("currentId", currentId)
        outState.putStringArrayList("removed", poolOfRemoved)
        cor.cancel()
    }

    private fun autoAdd(){
        cor = GlobalScope.launch{
            while (true){
                val index = (0..data.size).random()

                Thread(Runnable {
                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        if (poolOfRemoved.isEmpty()) {
                            data.add(index, "$currentId")
                            currentId++
                        } else {
                            data.add(index, poolOfRemoved[0])
                            poolOfRemoved.removeAt(0)
                        }

                        recyclerView.adapter?.notifyDataSetChanged()
                    })
                }).start()

                delay(5000)
            }
        }
    }


    fun onRemoveButtonClicked(view: View){
        var parentlayout = view.parent as LinearLayout
        var id = (parentlayout.getChildAt(0) as TextView).text

        val anim = AnimationUtils.loadAnimation(this, R.anim.remove)

        anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }
            override fun onAnimationEnd(animation: Animation) {
                data.remove(id)
                poolOfRemoved.add(id as String)
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(anim)
    }
}