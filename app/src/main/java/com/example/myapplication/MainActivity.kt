package com.example.myapplication

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import kotlinx.coroutines.*
import java.lang.Thread.sleep

import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {

    var data = mutableListOf<String>()
    var currentId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fillList()


        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){

            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }
        else{
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        }


        recyclerView.adapter = CustomRecyclerAdapter(data)

        autoAdd()

    }

    private fun fillList(): List<String> {
        (0..10).forEach {
                i -> data.add("$currentId")
                currentId++
        }

        return data
    }

    fun autoAdd(){
        GlobalScope.launch{
            while (true){
                Thread(Runnable {
                    this@MainActivity.runOnUiThread(java.lang.Runnable {

                        data.add("$currentId")
                        currentId++
                        recyclerView.adapter?.notifyDataSetChanged()
                    })
                }).start()

                delay(5000)
            }

        }
    }


    fun onRemoveButtonClicked(view : View){
        var parentlayout = view.parent as LinearLayout
        var id = (parentlayout.getChildAt(0) as TextView).text

        val anim = AnimationUtils.loadAnimation(this, R.anim.remove)
        view.startAnimation(anim)

        data.remove(id)
        recyclerView.adapter?.notifyDataSetChanged()


        /*val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.duration = 1000
        animator.start()

        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val animatedValue = animation.animatedValue as Float
                parentlayout.translationX = animatedValue
            }
        })*/


        //data.remove(id)
        /*Thread(Runnable {
            // performing some dummy time taking operation
            var i=0;
            while(i<Int.MAX_VALUE){
                i++
            }

            // try to touch View of UI thread
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                data.add("$currentId")
                currentId++
                recyclerView.adapter?.notifyDataSetChanged()
            })
        }).start()*/


    }





}