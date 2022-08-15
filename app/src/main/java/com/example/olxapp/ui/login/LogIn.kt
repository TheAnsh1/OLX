package com.example.olxapp.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.olxapp.MainActivity
import com.example.olxapp.R
import com.example.olxapp.ui.BaseActivity
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.SharedPref
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LogIn:BaseActivity() {

    private val RC_SIGN_IN = 100
    private var googleSignInOptions: GoogleSignInOptions? = null
    private var googleSignInClient: GoogleSignInClient? = null
    var callbackManager = CallbackManager.Factory.create()
    var auth = FirebaseAuth.getInstance()
    var TAG = ""
    private var loginbutton: Button? = null
    private var loginbutton2: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        loginbutton = findViewById(R.id.Button)
        loginbutton2 = findViewById(R.id.Button2)
        loginbutton?.setOnClickListener {

        if(userlogin()) {

        }
            else{
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
            }
        }

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })
        configureGoogleSignup()
        loginbutton2?.setOnClickListener{
            googleSignIn()
        }
    }

    private fun userlogin(): Boolean {
        if(auth.currentUser!=null  && AccessToken.getCurrentAccessToken()!!.isExpired)
        {
            return true
        }
        return false
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun configureGoogleSignup() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions!!)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credentials).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if(account.email!=null){
                    SharedPref(this).setString(Constants.USER_EMAIL,account.email!!)
                }
                if(account.id!=null){
                    SharedPref(this).setString(Constants.USER_ID,account.id!!)
                }
                if(account.displayName!=null){
                    SharedPref(this).setString(Constants.USER_NAME,account.displayName!!)

                }
                if(account.photoUrl!=null){
                    SharedPref(this).setString(Constants.USER_PHOTO,account.photoUrl.toString()!!)
                }
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show()
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

            val credential = FacebookAuthProvider.getCredential(token.token)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val account = auth.currentUser
                        SharedPref(this).setString(Constants.USER_ID,account?.uid!!)
                        if(account?.email!=null){
                            SharedPref(this).setString(Constants.USER_EMAIL,account.email!!)
                        }
                        if(account?.displayName!=null){
                            SharedPref(this).setString(Constants.USER_NAME,account.displayName!!)

                        }
                        if(account?.photoUrl!=null){
                            SharedPref(this).setString(Constants.USER_PHOTO,account.photoUrl.toString()!!)
                        }
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
    }


}