package com.example.olxapp.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ScaleGestureDetector
import android.view.View
import com.bumptech.glide.Glide
import com.example.olxapp.R
import kotlinx.android.synthetic.main.activity_preview_image.*

class PreviewImageActivity :BaseActivity(){
    private var mScaleGestureDetector:ScaleGestureDetector?=null
    private var mScalleFactor=1.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_image)

        val imagePath = intent.extras
        if(imagePath?.containsKey("imageUri")!!){
            val imageUri=imagePath?.getString("imageUri")
            val imageBitmap=BitmapFactory.decodeFile(imageUri)
            ImageView.setImageBitmap(imageBitmap)
        }
        else{
            val imageUrl=imagePath?.getString("imageUrl")
            Glide.with(this).load(imageUrl)
                .placeholder(R.drawable.big_placeholder)
                .into(ImageView)
        }
        ImageView.scaleX=mScalleFactor
        ImageView.scaleX=mScalleFactor

        mScaleGestureDetector= ScaleGestureDetector(this,ScaleListener())
        btnclose.setOnClickListener( View.OnClickListener{
            finish()
        })
    }


 private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
     override fun onScale(detector: ScaleGestureDetector?): Boolean {
      mScalleFactor*=detector!!.scaleFactor
        mScalleFactor=Math.max(0.1f,Math.min(mScalleFactor,10.0f))
        ImageView.scaleX=mScalleFactor
        ImageView.scaleY=mScalleFactor
        return true
     }

 }
}
