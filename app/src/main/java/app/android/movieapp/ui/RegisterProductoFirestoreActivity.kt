package app.android.movieapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import app.android.movieapp.databinding.ActivityRegisterProductoFirestoreBinding
import app.android.movieapp.ui.fragment.MovieFirestoreFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream


class RegisterProductoFirestoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterProductoFirestoreBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var openCameraLauncher: ActivityResultLauncher<Intent>

    private var isTitleEmpty: Boolean = true
    private var isReleaseDateEmpty: Boolean = true
    private var isOverviewEmpty: Boolean = true
    private var isVoteAverageEmpty: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterProductoFirestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = Firebase.firestore
        storage = FirebaseStorage.getInstance()

        binding.btnCamara.setOnClickListener{
            if(permissionValidated()){
                takePhoto()
            }
        }

        openCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val photoBitmap = result.data?.extras?.get("data") as Bitmap
                binding.imgFoto.setImageBitmap(photoBitmap)
                validarCamposVacios()
            }
        }

        binding.tilTitle.editText?.addTextChangedListener { text ->
            isTitleEmpty = text.toString().isBlank()
            validarCamposVacios()
        }

        binding.tilReleaseDate.editText?.addTextChangedListener { text ->
            isReleaseDateEmpty = text.toString().isBlank()
            validarCamposVacios()
        }

        binding.tilOverview.editText?.addTextChangedListener { text ->
            isOverviewEmpty = text.toString().isBlank()
            validarCamposVacios()
        }

        binding.tilVoteAverage.editText?.addTextChangedListener { text ->
            isVoteAverageEmpty = text.toString().isBlank()
            validarCamposVacios()
        }

        binding.btnRegisterMovie.setOnClickListener {
            guardarInformacionStorage()
        }
    }

    private fun validarCamposVacios(){
        binding.btnCamara.isEnabled = !isTitleEmpty && !isReleaseDateEmpty && !isOverviewEmpty && !isVoteAverageEmpty
        binding.btnRegisterMovie.isEnabled = !isTitleEmpty && !isReleaseDateEmpty && !isOverviewEmpty && !isVoteAverageEmpty && binding.imgFoto.drawable != null
    }

    private fun guardarInformacionStorage() {
        binding.imgFoto.setDrawingCacheEnabled(true)
        binding.imgFoto.buildDrawingCache()
        val bitmap = (binding.imgFoto.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        val imageName = "/imagenes/" +  binding.tilTitle.editText?.text.toString() + ".jpg"
        val imageRef: StorageReference = storage.reference.child(imageName)
        val uploadTask: UploadTask = imageRef.putBytes(imageData)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                val title = binding.tilTitle.editText?.text.toString()
                val releaseDate = binding.tilReleaseDate.editText?.text.toString()
                val overview = binding.tilOverview.editText?.text.toString()
                val voteAverage = binding.tilVoteAverage.editText?.text.toString().toDouble()
                addMovie(title, releaseDate, it.toString(), overview, voteAverage)
            }
        }
    }

    private fun addMovie(
        title: String,
        releaseDate: String,
        posterPath: String,
        overview: String,
        voteAverage: Double
    ) {
        val movie = hashMapOf<String, Any>(
            "title" to title,
            "release_date" to releaseDate,
            "poster_path" to posterPath,
            "overview" to overview,
            "vote_average" to voteAverage,
        )
        firestore.collection("peliculas").add(movie)
            .addOnSuccessListener {
                Toast.makeText(this, "Pelicula agregada exitosamente.", Toast.LENGTH_LONG).show()
                Thread.sleep(500)
                finish()
            }
    }

    private fun permissionValidated(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)
        val permissionList: MutableList<String> = mutableListOf()

        if(cameraPermission != PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.CAMERA)
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), 1000)
            return false;
        }
        return true;
    }

    private fun takePhoto(){
        val cameraIntent = Intent()
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE)
        openCameraLauncher.launch(cameraIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> {
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    takePhoto()
            }
        }
    }


}