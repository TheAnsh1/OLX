package com.example.olxapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.olxapp.BaseFragment
import com.example.olxapp.R
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment: BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        edName.setText(SharedPref(activity!!).getString(Constants.USER_NAME))
        edEmail.setText(SharedPref(activity!!).getString(Constants.USER_EMAIL))
        edPhonesettings.setText(SharedPref(activity!!).getString(Constants.USER_PHONE))
        edPostalAddress.setText(SharedPref(activity!!).getString(Constants.USER_ADDRESS))

        listener()

    }

    private fun listener() {
        savebutton.setOnClickListener(View.OnClickListener {
            saveData()
        })
    }

    private fun saveData() {
        if(edName.text.toString().isEmpty())
            edName.setError(getString(R.string.enter_name))
        else if (edEmail.text.toString().isEmpty())
            edEmail.setError(getString(R.string.enter_email))
        else {
            SharedPref(activity!!).setString(Constants.USER_NAME,edName.text.toString())
            SharedPref(activity!!).setString(Constants.USER_EMAIL,edEmail.text.toString())
            SharedPref(activity!!).setString(Constants.USER_PHONE,edPhonesettings.text.toString())
            SharedPref(activity!!).setString(Constants.USER_ADDRESS,edPostalAddress.text.toString())

            Toast.makeText(context,"Saved Successfully",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_settings_to_profile)


        }
    }
}