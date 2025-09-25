package pt.ulusofona.deisi.a2020.cm.g7.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.covid_tests_list_item.*
import kotlinx.android.synthetic.main.fragment_covid_tests_form.*
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.CovidTest
import pt.ulusofona.deisi.a2020.cm.g7.ui.viewmodels.CovidTestsListViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

val REQUEST_CODE = 123
private  val PERMISSION_CODE = 123
var currentPhoto: String? = null

class CovidTestFormFragment : Fragment() {

    private lateinit var viewModel: CovidTestsListViewModel
    private var covidTest: CovidTest? = null

    @OnClick(R.id.button_submit_form)
    fun onClickSubmit() {

        if(!validate()) {
            return
        }

        if(this.covidTest == null ) {

            this.covidTest =
                CovidTest(
                    result = input_result.text.toString(),
                    location = input_location.text.toString(),
                    dateTest = SimpleDateFormat("dd-MM-yyyy").parse(input_date.text.toString())
                )

            viewModel.insert(this.covidTest as CovidTest)
            Toast.makeText(activity as Context, "${getString(R.string.test_registered)}", Toast.LENGTH_LONG).show()
            activity?.onBackPressed()
        }
    }

    // Valida se os campos resultado, localização e data estão preenchidos
    private fun validate(): Boolean {

        val datePattern = "\\d{2}-\\d{2}-\\d{4}"
        var validForm = true

        if(this.covidTest == null && input_result.text.isEmpty()) {
            input_result.error = "${getString(R.string.mandatory_fields)}"
            validForm = false
        } else if(this.covidTest != null && input_result.text.isNotEmpty()) {
            this.covidTest?.result = input_result.text.toString()
        }

        if(this.covidTest == null && input_location.text.isEmpty()) {
            input_location.error = "${getString(R.string.mandatory_fields)}"
            validForm = false
        } else if(this.covidTest != null && input_location.text.isNotEmpty()) {
            this.covidTest?.location = input_location.text.toString()
        }

        if(this.covidTest == null && input_date.text.isEmpty()) {
            input_date.error = "${getString(R.string.mandatory_fields)}"
            validForm = false
        } else if(this.covidTest == null && !input_date.text.matches(Regex(datePattern))
            || this.covidTest != null && input_date.text.isNotEmpty() && !input_date.text.matches(Regex(datePattern))) {
            input_date.error = "${getString(R.string.invalid_date_format)}"
            validForm = false
        } else if(this.covidTest != null && input_date.text.isNotEmpty() && input_date.text.matches(Regex(datePattern))) {
            this.covidTest?.setDate(SimpleDateFormat("dd-MM-yyyy").parse(input_date.text.toString()))
        }

        return validForm
    }

    private fun updateFormData() {

        val resultText = covidTest?.result
        val locationText = covidTest?.location
        val dateText = SimpleDateFormat("dd-MM-yyyy").format(covidTest?.dateTest)

        result?.text = resultText
        location?.text = locationText
        date?.text = dateText
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View?

        if(arguments == null) {
            view = inflater.inflate(R.layout.fragment_covid_tests_form, container, false)
        } else {
            view = inflater.inflate(R.layout.fragment_covid_test_details, container, false)
            this.covidTest = this.arguments?.getParcelable(EXTRA_COVID_TEST)
        }

        ButterKnife.bind(this, view)
        viewModel = ViewModelProviders.of(this).get(CovidTestsListViewModel::class.java)

        return view
    }

    override fun onStart() {

        this.covidTest?.let {
            input_result.setText(it.result)
            input_location.setText(it.location)
            input_date.setText(SimpleDateFormat("dd-MM-yyyy").format(it.dateTest))
        }

        if(this.covidTest != null) {
            updateFormData()
        }

        super.onStart()
    }

    private fun openCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageFile()
        val uriSavedImage: Uri = Uri.fromFile(File(currentPhoto))
        camera.putExtra(MediaStore.EXTRA_OUTPUT ,uriSavedImage)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        startActivityForResult(camera, REQUEST_CODE)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File? {
        val format = SimpleDateFormat("yyyyMMddhhmmss")
        val hour = format.format(Date())
        val timeStamp: String = hour.format(format)
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        currentPhoto = image.absolutePath
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== REQUEST_CODE){
            button_photo.setImageBitmap(openFile(currentPhoto.toString()))
        }
    }

    companion object {
        fun openFile(imagePath: String): Bitmap? {
            val imgFile = File(imagePath)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                return myBitmap
            } else {
                return null
            }
        }
    }

    @SuppressLint("WrongConstant")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_photo.setOnClickListener {
            if (PermissionChecker.checkSelfPermission(activity as Context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED ||
                PermissionChecker.checkSelfPermission(activity as Context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                openCamera()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (permissions[0] == Manifest.permission.CAMERA) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera()
                    }
                }
            }
        }
    }
}