package com.example.olxapp.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.trusted.sharing.ShareData
import androidx.navigation.fragment.findNavController
import com.example.olxapp.BaseFragment
import com.example.olxapp.R
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_profile.*
import com.bumptech.glide.Glide
import com.example.olxapp.ui.login.LogIn
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance

class ProfileFragment: BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setdata()
        listener()
    }

    private fun listener() {
        ll_settings.setOnClickListener(this)
        ll_logout.setOnClickListener(this)
    }

    private fun setdata() {
        tvName.text = SharedPref(activity!!).getString(Constants.USER_NAME)
        tvEmail.text = SharedPref(activity!!).getString(Constants.USER_EMAIL)

        Glide.with(activity!!)
            .load(SharedPref(activity!!).getString(Constants.USER_PHOTO))
            .placeholder(R.drawable.manscartonimage)
            .into(imageViewUser)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_settings -> {
        findNavController().navigate(R.id.action_profile_to_settings)
            }
            R.id.ll_logout -> {
                showAletDialog()
            }
        }
    }

    private fun showAletDialog() {
        var builder = AlertDialog.Builder(activity!!)
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString(R.string.Logout))
        builder.setIcon(R.drawable.warning)
        builder.setPositiveButton(getString(R.string.yes)) { dialog: DialogInterface?, which: Int ->
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            clearSession()
            startActivity(Intent(activity!!, LogIn::class.java))
            activity!!.finish()
            dialog?.dismiss()


        }
        builder.setNegativeButton(getString(R.string.ni)){ dialog:DialogInterface?,which:Int ->
            dialog?.dismiss()


        }
        val alertDialog:AlertDialog =builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun clearSession() {
   SharedPref(activity!!).setString(Constants.USER_PHOTO,"")
        SharedPref(activity!!).setString(Constants.USER_EMAIL,"")

        SharedPref(activity!!).setString(Constants.USER_NAME,"")
        SharedPref(activity!!).setString(Constants.USER_PHONE,"")
        SharedPref(activity!!).setString(Constants.USER_ID,"")
    }
}
