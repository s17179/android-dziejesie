package pl.edu.pjatk.dziejesie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance()

//        db.collection("items")
//            .add(hashMapOf(
//                "name" to "Wypadek"
//            ))
//            .addOnSuccessListener { ref ->
//                Toast.makeText(
//                    this,
//                    "Pomyślnie dodano dokument: ${ref.id}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(
//                    this,
//                    "Błąd podczas dodawania dokumentu: $e",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }

        val currentUser = auth.currentUser

        if (currentUser != null) {
            Toast.makeText(
                this,
                "Jesteś zalogowany jako: " + currentUser.email,
                Toast.LENGTH_SHORT
            ).show()

            startActivity(Intent(this, ListActivity::class.java))
        } else {
            Toast.makeText(
                this,
                "Nie jesteś zalogowany",
                Toast.LENGTH_SHORT
            ).show()
        }

        buttonRegister.setOnClickListener { register(auth) }

        buttonLogin.setOnClickListener { login(auth) }
    }

    override fun onResume() {
        super.onResume()

        val auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            Toast.makeText(
                this,
                "Jesteś zalogowany jako: " + currentUser.email,
                Toast.LENGTH_SHORT
            ).show()

            startActivity(Intent(this, ListActivity::class.java))
        } else {
            Toast.makeText(
                this,
                "Nie jesteś zalogowany",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun register(auth: FirebaseAuth) {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(
                this,
                "Wypełnij wszystkie pola",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Pomyślnie zarejestrowano",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Błąd podczas rejestracji: " + task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun login(auth: FirebaseAuth) {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(
                this,
                "Wypełnij wszystkie pola",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Pomyślnie zalogowano",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this, ListActivity::class.java))
                    } else {
                        Toast.makeText(
                            this,
                            "Błąd podczas logowania: " + task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val f = File(currentPhotoPath)

            val storage = FirebaseStorage.getInstance().reference.child(UUID.randomUUID().toString())

            storage.putFile(Uri.fromFile(f))
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    storage.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("dupa", task.result.toString())
                        Toast.makeText(
                            this,
                            "Upload poszedł: " + task.result,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Wysrał się upload",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) { null }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "pl.edu.pjatk.dziejesie.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 1)
                }
            }
        }
    }
}