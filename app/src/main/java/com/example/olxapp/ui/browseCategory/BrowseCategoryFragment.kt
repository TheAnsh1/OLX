package com.example.olxapp.ui.browseCategory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olxapp.BaseFragment
import com.example.olxapp.R
import com.example.olxapp.model.DataItemModel
import com.example.olxapp.ui.browseCategory.browseadapter.BrowseCategoryAdapter
import com.example.olxapp.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_browse.*

class BrowseCategoryFragment:BaseFragment(), BrowseCategoryAdapter.ItemClickListener {
    private  var categoriesAdapter: BrowseCategoryAdapter?=null
    private lateinit var dataItemModel: MutableList<DataItemModel>
    val dp = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_browse, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textListener()
        getList()
        rv_categories.layoutManager = LinearLayoutManager(context)
    }

    private fun getList() {
        showProgressBar()
        dp.collection(arguments?.getString(Constants.KEY)!!)
            .get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel = it.toObjects(DataItemModel::class.java)
                setAdapter()

            }
    }

    private fun setAdapter() {
        categoriesAdapter = BrowseCategoryAdapter(dataItemModel, this)
        rv_categories.adapter = categoriesAdapter
    }

    override fun onItemClick(position: Int) {
    var bundle =Bundle()
        bundle.putString(Constants.DOCUMENT_ID,dataItemModel.get(position).id)
        bundle.putString(Constants.KEY,dataItemModel.get(position).type)
        findNavController().navigate(R.id.action_browse_to_details,bundle)
    }


    private fun textListener(){
        searcheditTextbrowse.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }



        })
    }

    private fun filterList(toString: String) {
        var temp:MutableList<DataItemModel> = ArrayList()
        for (data in dataItemModel){
            if(data.brand.contains(toString.capitalize())|| data.brand.contains(toString)
                ||data.AdTitle.contains(toString.capitalize())|| data.AdTitle.contains(toString) ){
                temp.add(data)
            }
        }
        categoriesAdapter?.updateList(temp)
    }


}