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
    var currentId = 16  //  текущий id для генерации
    var poolOfRemoved = arrayListOf<String>()  // список удаленных id
    lateinit var cor : Job  // корутин для генерации


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

        var dat = savedInstanceState?.getStringArrayList("data")  // перенос находящихся элементов при повороте экрана
        if (dat != null) {
            for (i in dat){
                data.add(i)
            }
        }
        recyclerView.adapter?.notifyDataSetChanged()

        val fill = savedInstanceState?.getBoolean("draw")
        if (fill == null){
            firstFill()
        }
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
        outState.putStringArrayList("data", data)  // на экране
        outState.putInt("currentId", currentId)  // текущий id для генерации
        outState.putStringArrayList("removed", poolOfRemoved)  // удаленные с экрана
        outState.putBoolean("filled", true)
        cor.cancel()
    }

    fun firstFill(){
        for (i in 1..15){
            data.add("$i")
        }
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun autoAdd(){
        cor = GlobalScope.launch{
            while (true){
                val index = (0..data.size).random()  // вствка в случайное место списка

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
        var parentlayout = view.parent as LinearLayout  // родитель удаляемого элемента
        var id = (parentlayout.getChildAt(0) as TextView).text  // id удаляемого элемента

        val anim = AnimationUtils.loadAnimation(this, R.anim.remove)

        anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }
            override fun onAnimationEnd(animation: Animation) {
                // сначала анимация сдвига, потом удаление
                data.remove(id)
                poolOfRemoved.add(id as String)
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(anim)
    }
}