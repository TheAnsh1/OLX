package com.example.olxapp.ui.uploadPhoto

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olxapp.BaseFragment
import com.example.olxapp.MainActivity
import com.example.olxapp.R
import com.example.olxapp.ui.PreviewImageActivity
import com.example.olxapp.ui.uploadPhoto.adapter.UploadImagesAdapter
import com.example.olxapp.utilities.Constants
import com.example.olxapp.utilities.OnActivityResultData
import com.example.olxapp.utilities.SharedPref
import com.google.android.gms.tasks.OnSuccessListener
//import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_upload_photo.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File
import java.nio.file.AccessMode
import java.util.*
import kotlin.collections.ArrayList

class UploadPhotoFragment:BaseFragment(), View.OnClickListener,
    UploadImagesAdapter.ItemClickListener {

    private val imageUrlList:ArrayList<String> = ArrayList()
    private var count=0
    private lateinit var uploadTask: UploadTask
    private var imagesAdapter:UploadImagesAdapter?=null
    private var selectedImagesArrayList:ArrayList<String> = ArrayList()
    private var outputFileUri: String?=null
    internal var dialog:BottomSheetDialog?=null
    internal var selectedImage: File?=null
    internal var TAG=UploadPhotoFragment::class.java.simpleName
    val dp=FirebaseFirestore.getInstance()

    internal lateinit var storageRef:StorageReference
    internal lateinit var imageRef:StorageReference
    internal lateinit var storage:FirebaseStorage
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_upload_photo,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reyclerview.layoutManager=GridLayoutManager(activity,3)
        storage= FirebaseStorage.getInstance()
        storageRef=storage.getReference()
        listener()
        registerCallBackPhoto()
    }

    private fun registerCallBackPhoto() {
        (activity as MainActivity).getOnActivityResult(object : OnActivityResultData{


            override fun resultDate(bundle: Bundle) {
            linearLayoutchoosephot.visibility=View.GONE
               reyclerview.visibility=View.VISIBLE

                val mPaths=bundle.getStringArrayList(Constants.IMAGE_PATH)
                selectedImage=File(mPaths!![0])
                outputFileUri=mPaths[0]
                selectedImagesArrayList.add(mPaths[0])
                setAdapter()
            }

        })
    }

    private fun setAdapter() {
    if(imagesAdapter!=null){
        imagesAdapter!!.customNotify(selectedImagesArrayList)
    }
        else{
            imagesAdapter= UploadImagesAdapter(activity!!,selectedImagesArrayList,this)
          reyclerview.adapter=imagesAdapter
        }
    }

    private fun listener() {
        imageviewchossephoto.setOnClickListener(this)
        uploadphotoPreview.setOnClickListener(this)
        uploadphotoUpload.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imageviewchossephoto->{
                showBottomSheetDialog()
            }
            R.id.uploadphotoPreview->{
                if(selectedImage!=null){
                    startActivity(Intent(activity,PreviewImageActivity::class.java).putExtra("imageUri",outputFileUri))
                }
            }
            R.id.uploadphotoUpload->{
                if (selectedImage== null || !selectedImage!!.exists())
                {
                    Toast.makeText(activity,"Please Select Photo",Toast.LENGTH_SHORT).show()
                }
                else {
                    saveFileInFirebaseStorage()
                }

            }
        }
    }

    private fun saveFileInFirebaseStorage() {
        for (i in 0..selectedImagesArrayList.size-1){
            val file=File(selectedImagesArrayList[i])
            uploadImage(file,file.name,i)
        }
    }

    private fun uploadImage(file: File, name: String, i: Int) {
        imageRef = storageRef.child("image/$name")
        uploadTask = imageRef.putFile(Uri.fromFile(file))
        uploadTask.addOnSuccessListener ( object : OnSuccessListener<UploadTask.TaskSnapshot>{
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
            imageRef.downloadUrl.addOnSuccessListener {
                count++
                val url=it.toString()
                imageUrlList.add(url)
                if(count == selectedImagesArrayList.size)
                    postAd()
            }

            }

        } )
    }

    private fun postAd() {
        showProgressBar()
        val documentId=dp.collection(arguments?.getString(Constants.KEY)!!).document().id
        val documentData = hashMapOf(
            Constants.ADDRESS to arguments?.getString(Constants.ADDRESS),
            Constants.BRAND to arguments?.getString(Constants.BRAND),
            Constants.PHONE to arguments?.getString(Constants.PHONE),
            Constants.PRICE to arguments?.getString(Constants.PRICE),
            Constants.YEAR to arguments?.getString(Constants.YEAR),
            Constants.KM_DRIVER to arguments?.getString(Constants.KM_DRIVER),
            Constants.AD_DESCRIPTION to arguments?.getString(Constants.AD_DESCRIPTION),

            Constants.AD_TITLE to arguments?.getString(Constants.AD_TITLE),
            Constants.TYPE to arguments?.getString(Constants.KEY),
            Constants.ID to documentId,
            Constants.CREATED_DATE  to Date(),
            Constants.USER_ID to SharedPref(activity!!).getString(Constants.USER_ID),
            "images" to imageUrlList
        )
        dp.collection(arguments?.getString(Constants.KEY)!!)
            .add(documentData)
            .addOnSuccessListener {
                updateDocumentId(it.id)
            }
    }

    private fun updateDocumentId(id: String) {
        val docData = mapOf(
            Constants.ID to id
        )
        dp.collection(arguments?.getString(Constants.KEY)!!)
            .document(id)
            .update(docData).addOnSuccessListener {
                hideProgressBar()
                Toast.makeText(activity!!,"Ad Posted Successfully",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_photo_my_ads)
            }
    }

    private fun showBottomSheetDialog() {
        val layoutInflater=activity!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view=layoutInflater.inflate(R.layout.bottomsheet_dialog,null)
        dialog= BottomSheetDialog(activity!!)
        dialog?.setContentView(view)
        dialog?.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.setBackgroundColor(resources.getColor(android.R.color.transparent))
        var textViewGrallery=dialog!!.findViewById<TextView>(R.id.textviewphotogallery)
        var textViewCamer=dialog!!.findViewById<TextView>(R.id.Camera)
        var textViewCancel=dialog!!.findViewById<TextView>(R.id.textViewCancel)

        textViewCamer?.setOnClickListener{
            dialog!!.dismiss()
            chooseImage(ImagePicker.Mode.CAMERA)
        }
        textViewGrallery?.setOnClickListener{
            dialog!!.dismiss()
            chooseImage(ImagePicker.Mode.GALLERY)

        }
        textViewCancel?.setOnClickListener{
            dialog!!.dismiss()
        }

        dialog?.show()
            val lp=WindowManager.LayoutParams()
            val window=dialog?.window
        lp.copyFrom(window!!.attributes)
        lp.width=WindowManager.LayoutParams.MATCH_PARENT
        lp.height=WindowManager.LayoutParams.MATCH_PARENT
        window.attributes=lp

    }
private fun chooseImage(mode:ImagePicker.Mode){
    ImagePicker.Builder(activity!!)
        .mode(mode)
        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
        .directory(ImagePicker.Directory.DEFAULT)
        .extension(ImagePicker.Extension.PNG)
        .allowMultipleImages(false)
        .enableDebuggingMode(true)
        .build();
}

    override fun onItemClick() {
        showBottomSheetDialog()
    }
}