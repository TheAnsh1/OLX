package com.example.olxapp.ui.includeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.olxapp.BaseFragment
import com.example.olxapp.R
import com.example.olxapp.utilities.Constants
import kotlinx.android.synthetic.main.fragment_include_details.*

class IncludeDetailsFragment:BaseFragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_include_details,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listener()
        if(arguments?.getString(Constants.KEY)!!.equals(Constants.CAR)){
            layoutKmdriven.visibility=View.VISIBLE

        }
    }

    private fun listener() {
        includenextid.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.includenextid->{
                sendData()
            }
        }
    }

    private fun sendData() {
       if(edBrand.text?.isEmpty()!!){
           edBrand.setError(getString(R.string.brand))
       }
        else if(edPhoneNo.text?.isEmpty()!!){
           edBrand.setError(getString(R.string.Phone))
       }
       else
       {
        val bundle=Bundle()
           bundle.putString(Constants.BRAND,edBrand.text.toString())
           bundle.putString(Constants.YEAR,edYear.text.toString())
           bundle.putString(Constants.AD_TITLE,edAdtitle.text.toString())
           bundle.putString(Constants.AD_DESCRIPTION,edDescribe2.text.toString())
           bundle.putString(Constants.ADDRESS,edAddres.text.toString())
           bundle.putString(Constants.PHONE,edPhoneNo.text.toString())
           bundle.putString(Constants.PRICE,edPrice.text.toString())
           bundle.putString(Constants.KM_DRIVER,edkmdrive.text.toString())
           bundle.putString(Constants.KEY,arguments?.getString(Constants.KEY))
       findNavController().navigate(R.id.action_details_photo_upload,bundle)


       }
    }
}