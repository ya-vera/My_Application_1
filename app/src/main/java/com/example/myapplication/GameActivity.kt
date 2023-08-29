package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.myapplication.kernel.startTheGame
import com.example.myapplication.kernel.isPlay


const val f_size = 158
const val cells = 7

class GameActivity : AppCompatActivity(), View.OnClickListener  {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_game)
//    }

    private val allTale = mutableListOf<Tale>()
    private val ice_cream by lazy {
        ImageView(this)
    }
    private val h by lazy {
        ImageView(this)
    }
    private var cur = Directions.DOWN
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        h.layoutParams = FrameLayout.LayoutParams(f_size,f_size)
        h.setImageResource(R.drawable.w)
//        h.setBackgroundColor(Color.TRANSPARENT);
//        h.background = ContextCompat.getDrawable(this, R.drawable.head)
        val container: FrameLayout = findViewById(R.id.container)
        container.layoutParams = LinearLayout.LayoutParams(f_size*cells, f_size*cells)
        startTheGame()
        newIceCream()
        kernel.nextMove={move(Directions.DOWN)}
//        val ivArrowCircleUp = findViewById<LinearLayout>(R.id.ivArrowCircleUp)
        val ivArrowCircleUp: ImageView = findViewById(R.id.ivArrowCircleUp)
        ivArrowCircleUp.setOnClickListener{kernel.nextMove =  {checkD(Directions.UP, Directions.DOWN)}}
//        val ivArrowCircleDown = findViewById<LinearLayout>(R.id.ivArrowCircleDown)
        val ivArrowCircleDown: ImageView = findViewById(R.id.ivArrowCircleDown)
        ivArrowCircleDown.setOnClickListener {kernel.nextMove =  {checkD(Directions.DOWN, Directions.UP)}}
//        val ivArrowCircleLeft = findViewById<LinearLayout>(R.id.ivArrowCircleLeft)
        val ivArrowCircleLeft: ImageView = findViewById(R.id.ivArrowCircleLeft)
        ivArrowCircleLeft.setOnClickListener {kernel.nextMove =  {checkD(Directions.LEFT, Directions.RIGHT)}}
//        val ivArrowCircleRight = findViewById<LinearLayout>(R.id.ivArrowCircleRight)
        val ivArrowCircleRight: ImageView = findViewById(R.id.ivArrowCircleRight)
        ivArrowCircleRight.setOnClickListener {kernel.nextMove =  {checkD(Directions.RIGHT, Directions.LEFT)}}
        val ivPause: ImageView = findViewById(R.id.ivPause)
//        val ivPlay: ImageView = findViewById(R.id.ivPlay)
        ivPause.setOnClickListener {
//        ivPlay.setOnClickListener {
            if (isPlay) {
                ivPause.setImageResource(R.drawable.baseline_play_arrow_24)
//                  ivPlay.setImageResource(R.drawable.baseline_play_arrow_24)
            } else {
                ivPause.setImageResource(R.drawable.baseline_pause_24)
//                 ivPlay.setImageResource(R.drawable.baseline_pause_24)
            }
            isPlay = !isPlay
        }
    }
    private fun newIceCream() {
        ice_cream.layoutParams = FrameLayout.LayoutParams(f_size,f_size)
        ice_cream.setImageResource(R.drawable.baseline_icecream_24)
        (ice_cream.layoutParams as FrameLayout.LayoutParams).topMargin = (0 until cells).random()* f_size
        (ice_cream.layoutParams as FrameLayout.LayoutParams).leftMargin = (0 until cells).random()* f_size
        val container: FrameLayout = findViewById(R.id.container)
        container.removeView(ice_cream)
        container.addView(ice_cream)
    }

    fun check (){
        if (h.left == ice_cream.left && h.top == ice_cream.top) {
            val container = findViewById<FrameLayout>(R.id.container)
            container.removeView(ice_cream)
            newIceCream()
            addTale(h.top, h.left)
        }

    }

    private fun checkD (p:Directions, opp:Directions) {
        if (cur==opp) {
            move(cur)
        } else {
            move(p)
        }
    }

    private fun checkHit():Boolean{
        for (part in allTale) {
            if (part.left == h.left && part.top == h.top) {
                return true
            }
        }
        if (h.left<0 || h.top<0 || h.top>= f_size*cells || h.left>= f_size*cells) {
            return true
        }
        return false
    }

    private fun addTale (top:Int, left:Int) {
        val tale = drawTale(top, left)
        allTale.add(Tale(top, left, tale))
    }

    private fun drawTale(top: Int, left: Int): ImageView {
        val taleImage = ImageView(this)
        taleImage.setImageResource(R.drawable.o)
//        taleImage.setImageResource(R.drawable.baseline_icecream_24)
        taleImage.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        taleImage.layoutParams = FrameLayout.LayoutParams(f_size,f_size)
        (taleImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (taleImage.layoutParams as FrameLayout.LayoutParams).leftMargin = left
        val container = findViewById<FrameLayout>(R.id.container)
        container.addView(taleImage)
        return taleImage
    }

    private fun move(directions: Directions) {
        when (directions) {
            Directions.UP -> headrotate(Directions.UP,180f, -f_size)
            Directions.DOWN -> headrotate(Directions.DOWN,0f, f_size)
            Directions.RIGHT -> headrotate(Directions.RIGHT,270f, f_size)
            Directions.LEFT -> headrotate(Directions.LEFT,90f, -f_size)
        }
        runOnUiThread{
            if (checkHit()) {
                isPlay=false
                showScore()
                return@runOnUiThread
            }
            makeTale()
            val container = findViewById<FrameLayout>(R.id.container)
            check()
            container.removeView(h)
            container.addView(h)
        }
    }

    private fun headrotate (directions: Directions, angle:Float, coord:Int){
        h.rotation=angle
        when (directions){
            Directions.UP,Directions.DOWN -> {
                (h.layoutParams as FrameLayout.LayoutParams).topMargin += coord
            }
            Directions.LEFT,Directions.RIGHT -> {
                (h.layoutParams as FrameLayout.LayoutParams).leftMargin += coord
            }
        }
        cur=directions
    }

    private fun showScore() {
        AlertDialog.Builder(this)
            .setTitle("Количество очков: ${allTale.size}")
            .setPositiveButton("OKKK" ,{_, _ ->
                this.recreate()
            })
            .setCancelable(false)
            .create()
            .show()
    }

    private fun makeTale() {
        var t : Tale? = null
        for (i in 0 until allTale.size) {
            val part =allTale[i]
            val container = findViewById<FrameLayout>(R.id.container)
            container.removeView(part.imageView)
            if (i==0){
                t = part
                allTale[i]=Tale(h.top,h.left,drawTale(h.top,h.left))
            } else {
                val a = allTale[i]
                t?.let {
                    allTale[i]=Tale(it.top,it.left,drawTale(it.top,it.left))
                }
                t=a
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.main_back -> finish()
        }
    }
}

enum class Directions {
    UP,
    RIGHT,
    DOWN,
    LEFT
}