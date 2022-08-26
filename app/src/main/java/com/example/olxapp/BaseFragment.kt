package com.example.olxapp

import android.app.Dialog
import android.view.Window
import androidx.fragment.app.Fragment

 open class BaseFragment:Fragment(){
    lateinit var mDialog:Dialog
    open fun showProgressBar(){
        mDialog=Dialog(activity!!)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.dialog_progressbar)
        mDialog.setCancelable(true)
        mDialog.show()
    }
    open fun hideProgressBar(){
        mDialog.dismiss()
    }



}
