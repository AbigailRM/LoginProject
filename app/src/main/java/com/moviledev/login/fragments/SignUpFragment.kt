package com.moviledev.login.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.moviledev.login.DatePickerFragment
import com.moviledev.login.R
import com.moviledev.login.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    //Progress dialog
    private lateinit var progressDialog: ProgressDialog
    
    private lateinit var auth: FirebaseAuth

    private lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)


        //init firebase auth
        auth = FirebaseAuth.getInstance()

        //init progress dialog
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.etBirth.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnSubmit.setOnClickListener {
            validateData()
        }

        binding.tvAccount.setOnClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_view_fragment, LoginFragment()).commit()
        }


        return binding.root
    }

    private fun showDatePickerDialog() {
        val newFragment =
            DatePickerFragment.newInstance { datePicker, year, month, day -> // +1 because January is zero
                val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
                binding.etBirth.setText(selectedDate)
            }

        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private var name = ""
    private var lastName = ""
    private var sex = ""
    private var dateOfBirth = ""
    private var email = ""
    private var phone = ""
    private var password = ""
    private var cPassword = ""
    private var state = ""
    private var address = ""

    private fun validateData(){
        name = binding.etName.text.toString().trim()
        lastName = binding.etLastName.text.toString().trim()

        if (binding.rbMen.isChecked){
            sex = "Male"
        }else if (binding.rbWomen.isChecked){
            sex = "Female"
        }

        dateOfBirth = binding.etBirth.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        phone = binding.etPhone.text.toString().trim()
        password = binding.etPass.text.toString().trim()
        cPassword = binding.etConfirmPass.text.toString().trim()
        state = binding.etState.text.toString().trim()
        address = binding.etAddress.text.toString().trim()

        //validating
        if(name.isEmpty()){
            Toast.makeText(requireActivity(), "Name is required...", Toast.LENGTH_SHORT).show()
        }
        else if(lastName.isEmpty()){
            Toast.makeText(requireActivity(), "Last name is required...", Toast.LENGTH_SHORT).show()
        }
        else if(sex.isEmpty()){
            Toast.makeText(requireActivity(), "Sex is required...", Toast.LENGTH_SHORT).show()
        }
        else if(dateOfBirth.isEmpty()){
            Toast.makeText(requireActivity(), "Date of birth is required...", Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(requireActivity(), "Invalid Email Pattern...", Toast.LENGTH_SHORT).show()
        }
        else if(phone.isEmpty()){
            Toast.makeText(requireActivity(), "Phone number is required...", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            Toast.makeText(requireActivity(), "Password is required...", Toast.LENGTH_SHORT).show()
        }
        else if(cPassword.isEmpty()){
            Toast.makeText(requireActivity(), "Confirm your password...", Toast.LENGTH_SHORT).show()
        }
        else if(password != cPassword){
            Toast.makeText(requireActivity(), "Password doesn't match...", Toast.LENGTH_SHORT).show()
        }
        else if(state.isEmpty()){
            Toast.makeText(requireActivity(), "State is required...", Toast.LENGTH_SHORT).show()
        }
        else if(address.isEmpty()){
            Toast.makeText(requireActivity(), "Address is required...", Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount()
        }
    }

    private fun createUserAccount() {
        progressDialog.setMessage("Creating account...")
        progressDialog.show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Failed creating account due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun updateUserInfo() {

        progressDialog.setMessage("Saving user info...")

        //val timestamp = System.currentTimeMillis()

        //get current user uid
        val uid = auth.currentUser?.uid

        //add in db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["name"] = name
        hashMap["lastName"] = lastName
        hashMap["sex"] = sex
        hashMap["dateOfBirth"] = dateOfBirth
        hashMap["state"] = state
        hashMap["address"] = address
        hashMap["phone"] = phone
        hashMap["email"] = email
        //hashMap["timestamp"] = timestamp

        //set data to db

        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //user info saved
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Account created...", Toast.LENGTH_SHORT).show()

                val transaction = requireActivity().supportFragmentManager.beginTransaction()

                transaction.replace(R.id.frame_view_fragment, ProfileFragment()).commit()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Failed saving user info due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

}


