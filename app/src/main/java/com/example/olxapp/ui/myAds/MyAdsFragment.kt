package com.example.olxapp.ui.myAds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olxapp.BaseFragment
import com.example.olxapp.R
import com.example.olxapp.model.DataItemModel
import com.example.olxapp.ui.myAds.adapter.MyAdsAdapter
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_myads.*

class MyAdsFragment:BaseFragment(), MyAdsAdapter.ItemClickListener {

    private var documentDataList: MutableList<DataItemModel> =ArrayList()
    private lateinit var dataItemModel: MutableList<DataItemModel>
    var myAdsAdpter:MyAdsAdapter?=null
    val dp= FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root= inflater.inflate(R.layout.fragment_myads,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_ads.layoutManager=LinearLayoutManager(context)
        getMyAds()
    }

    private fun getMyAds() {
         showProgressBar()
        dp.collection(Constants.CATEGORIES)
            .get().addOnSuccessListener {
                for (i in it.documents){
                    getDataFromKeys(i.getString(Constants.KEY)!!)
                }
            }
    }

    private fun getDataFromKeys(keys: String) {
      dp.collection(keys)
          .whereEqualTo("user_id",SharedPref(activity!!).getString(Constants.USER_ID))
          .get().addOnSuccessListener {
            hideProgressBar()
              dataItemModel =it.toObjects(DataItemModel::class.java)
              documentDataList.addAll(dataItemModel)
              setAdapter()
          }
    }

    private fun setAdapter() {
        if( myAdsAdpter != null) {
            myAdsAdpter!!.updateList(documentDataList)
        }
        else{
            myAdsAdpter = MyAdsAdapter(documentDataList,this)
            rv_ads.adapter=myAdsAdpter
        }
    }

    override fun onItemClick(position: Int) {
    var bundle =Bundle()
        bundle.putString(Constants.KEY,documentDataList.get(position).type)
        bundle.putString(Constants.DOCUMENT_ID,documentDataList.get(position).id)
        findNavController().navigate(R.id.action_my_ads_to_details,bundle)
    }
}