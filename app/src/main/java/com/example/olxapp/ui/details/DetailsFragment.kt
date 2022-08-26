package com.example.olxapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.olxapp.BaseFragment
import com.example.olxapp.R
import com.example.olxapp.model.DataItemModel
import com.example.olxapp.ui.PreviewImageActivity
import com.example.olxapp.ui.details.adapter.DetailImagesAdapter
import com.example.olxapp.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_include_details.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment:BaseFragment(), DetailImagesAdapter.onItemClick {
    private lateinit var dataItemModel: DataItemModel
    val dp =FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =inflater.inflate(R.layout.fragment_details,container,false)
        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getItemDetails()

        clickListener()

        if(arguments?.getString(Constants.KEY).equals(Constants.CAR))
        {
            llkmdriven.visibility=View.VISIBLE
        }
    }
    private fun clickListener() {
        tvCall.setOnClickListener(View.OnClickListener{
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("teli"+dataItemModel.phone)
            startActivity(dialIntent)

        })
    }

    private fun getItemDetails() {
        showProgressBar()
        dp.collection(arguments?.getString(Constants.KEY)!!)
            .document(arguments?.getString(Constants.DOCUMENT_ID)!!)
            .get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel =it.toObject(DataItemModel::class.java)!!
                Log.d("DATA", ": "+dataItemModel.AdTitle)

                setData()
                setPagerAdapter()
            }
    }

    private fun setPagerAdapter() {
        val detailImagesAdapter =DetailImagesAdapter(context!!,dataItemModel.images,this)
        viewpager.adapter= detailImagesAdapter
        viewpager.offscreenPageLimit =1
    }

    private fun setData() {
        tvpriced.text=Constants.CURRENCY_SYMBOL+dataItemModel.price
        titledetail.text =dataItemModel.AdTitle
        tvaddress.text = dataItemModel.address
       tvbrand.text=dataItemModel.brand
      des.text =dataItemModel.edDescribe2
        tvKmDriven.text =dataItemModel.kmdrive
        tvphoneno.text =dataItemModel.phone


        tvyear.text =dataItemModel.year
        val dateformat = SimpleDateFormat(
            "dd MMM", Locale.getDefault()
        )
        tvdate.text =dateformat.format(dataItemModel.createdDate)


    }

    override fun onClick(position: Int) {
        startActivity(Intent(activity,PreviewImageActivity::class.java)
            .putExtra("imageUrl",dataItemModel.images.get(position)))

    }
}