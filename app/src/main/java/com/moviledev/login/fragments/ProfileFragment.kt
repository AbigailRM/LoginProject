package com.moviledev.login.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.moviledev.login.R
import com.moviledev.login.databinding.FragmentProfileBinding
import com.moviledev.login.User


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var database: FirebaseDatabase

    private lateinit var dbRef: DatabaseReference

    private lateinit var user : User

    //Progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        //init FirebaseAuth
        auth = FirebaseAuth.getInstance()

        //init progress dialog
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadUser()

        binding.ibLogOut.setOnClickListener {
            auth.signOut()

            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_view_fragment, LoginFragment()).commit()
        }

        //database = FirebaseDatabase.getInstance()
        //dbRef = database.getReference("Users")

        return binding.root
    }

    private fun loadUser() {

        progressDialog.setMessage("Checking User...")

        val uid = auth.currentUser?.uid.toString()

        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        //if (uid.isEmpty()){
            dbRef.child(uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot){


                        //user = snapshot.getValue<Post>(User::class.java)!!
                        //if (snapshot.exists()){
                            binding.etName.text = snapshot.child("name").value.toString()

                            binding.etLastName.text = snapshot.child("lastName").value.toString()

                            binding.etSex.text = snapshot.child("sex").value.toString()

                            binding.etDateOfBirth.text = snapshot.child("dateOfBirth").value.toString()

                            binding.etPhone.text = snapshot.child("phone").value.toString()

                            binding.etEmail.text = snapshot.child("email").value.toString()
                       // }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        progressDialog.dismiss()
                        Toast.makeText(requireActivity(), "Failed getting profile data", Toast.LENGTH_SHORT).show()
                    }
                })
       // }

    }

}