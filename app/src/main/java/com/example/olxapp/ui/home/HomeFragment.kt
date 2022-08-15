package com.example.olxapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olxapp.R
import com.example.olxapp.model.CategoriesModel
import com.example.olxapp.ui.home.adapter.CategoriesAdapter
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), CategoriesAdapter.IemClickListener {


    private lateinit var categoriesAdapter: CategoriesAdapter
    val dp=FirebaseFirestore.getInstance()
    private lateinit var categoriesModel:MutableList<CategoriesModel>
   private lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         root= inflater.inflate(R.layout.fragment_home,container,false)


        return root

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
            text5.text=SharedPref(activity!!).getString(Constants.CITY_NAME)
        getCategoryList()

    }
    private fun textListener(){
        searcheditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
             filterList(s.toString())
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
     dp.collection("Categories").get().addOnSuccessListener {
         categoriesModel=it.toObjects(CategoriesModel::class.java)
         setAdapter()
     }
    }

    private fun setAdapter() {
    recycleview.layoutManager=GridLayoutManager(context,3)
        categoriesAdapter=CategoriesAdapter(categoriesModel,this)
        recycleview.adapter=categoriesAdapter
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(context,"Hey"+position,Toast.LENGTH_SHORT ).show()
    }
}