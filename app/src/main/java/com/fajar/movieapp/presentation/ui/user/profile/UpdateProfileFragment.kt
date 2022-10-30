package com.fajar.movieapp.presentation.ui.user.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fajar.movieapp.R
import com.fajar.movieapp.data.local.preference.UserPreferences
import com.fajar.movieapp.databinding.FragmentUpdateProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private val REQUEST_CODE_PERMISSION = 3

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
        observeData()
    }

    private fun observeData() {
        viewModel.getUser().observe(viewLifecycleOwner) {
            bindDataToForm(it)
        }
    }

    private fun setOnClickListener() {
        binding.ivProfileImage.setOnClickListener {
            checkPermissions()
        }
        binding.btnUpdate.setOnClickListener {
            if (validateInput()) {
                viewModel.updateUser(parseFormIntoData())

                val options = NavOptions.Builder()
                    .setPopUpTo(R.id.updateProfileFragment, true)
                    .build()
                val action =
                    UpdateProfileFragmentDirections.actionUpdateProfileFragmentToProfileFragment()
                findNavController().navigate(action, options)
                Toast.makeText(requireContext(), "Update success", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun parseFormIntoData(): UserPreferences {
        return UserPreferences(
            username = binding.etUsername.text.toString().trim(),
            email = binding.etUsername.text.toString().trim(),
            fullName = binding.etFullName.text.toString().trim(),
            dateOfBirth = binding.etDateOfBirth.text.toString().trim(),
            address = binding.etAddress.text.toString().trim()
        ).apply {
            viewModel.getUser().observe(viewLifecycleOwner) {
                this.id = it.id
            }
        }
    }

    private fun bindDataToForm(user: UserPreferences?) {
        Log.d("profile", user.toString())
        user?.let {
            binding.apply {
                etUsername.setText(user.username)
                etEmail.setText(user.email)
                etFullName.setText(user.fullName)
                etDateOfBirth.setText(user.dateOfBirth)
                etAddress.setText(user.address)

                user.profileImage?.let {
                    if (it.isEmpty().not()) {
                        Glide.with(this@UpdateProfileFragment)
                            .load(convertStringToBitmap(it))
                            .into(binding.ivProfileImage)
                    }
                }
            }
        }
    }

    private fun checkPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION
            )
        ) {
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private fun isGranted(
        activity: Activity,
        permission: String, //for camera
        permissions: Array<String>, //for read write storage/gallery
        request: Int
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission denied")
            .setMessage("Permission is denied, Please allow app permission from App Settings")
            .setPositiveButton("App Settings") { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        Log.d("profile", "set $bitmap")
        viewModel.setProfileImage(convertBitmapToString(bitmap))
    }

    private fun convertBitmapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun convertStringToBitmap(string: String): Bitmap {
        val imageBytes = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun validateInput(): Boolean {
        var isValid = true
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val fullName = binding.etFullName.text.toString()
        val dateOfBirth = binding.etDateOfBirth.text.toString()
        val address = binding.etAddress.text.toString()

        if (username.isEmpty()) {
            isValid = false
            binding.etUsername.error = "Username or password must not be empty"
        }
        if (email.isEmpty()) {
            isValid = false
            binding.etEmail.error = "Email must not be empty"
        }
        if (fullName.isEmpty()) {
            isValid = false
            binding.etFullName.error = "Full name must not be empty"
        }
        if (dateOfBirth.isEmpty()) {
            isValid = false
            binding.etDateOfBirth.error = "Date of birth must not be empty"
        }
        if (address.isEmpty()) {
            isValid = false
            binding.etAddress.error = "Address must not be empty"
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}