package com.example.olxapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olxapp.BaseFragment
import com.example.olxapp.R
import com.example.olxapp.model.CategoriesModel
import com.example.olxapp.ui.home.adapter.CategoriesAdapter
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

class HomeFragment : BaseFragment(), CategoriesAdapter.IemClickListener {


    private lateinit var categoriesAdapter: CategoriesAdapter
    val dp=FirebaseFirestore.getInstance()
    private lateinit var categoriesModel:MutableList<CategoriesModel>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
          val root= inflater.inflate(R.layout.fragment_home,container,false)
        return root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
            text5.text=SharedPref(activity!!).getString(Constants.CITY_NAME)
        getCategoryList()
    }
    private fun textListener(){
        searchText.addTextChangedListener(object :TextWatcher{
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
        var temp:MutableList<CategoriesModel> = ArrayList()
        for (data in categoriesModel){
            if(data.key.contains(toString.capitalize())|| data.key.contains(toString)){
                temp.add(data)
            }
        }
        categoriesAdapter.updateList(temp)
    }

    private fun getCategoryList() {
        showProgressBar()

               dp.collection("Categories").get().addOnSuccessListener {
                       hideProgressBar()
                       categoriesModel = it.toObjects(CategoriesModel::class.java)
                       setAdapter()
                   }


    }

    private fun setAdapter() {
         recycleviewHome.layoutManager = GridLayoutManager(context, 3)
            categoriesAdapter = CategoriesAdapter(categoriesModel, this)
            recycleviewHome.adapter = categoriesAdapter

    }

    override fun onItemClick(position: Int) {
     val bundle = Bundle()
        bundle.putString(Constants.KEY,categoriesModel.get(position).key)
        findNavController().navigate(R.id.action_home_to_browse,bundle)
    }
}