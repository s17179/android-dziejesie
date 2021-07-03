package pl.edu.pjatk.dziejesie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_details.*

class ItemDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val auth = FirebaseAuth.getInstance()

        textViewName.text = intent.extras!!.getString("name")
        Picasso.get().load(intent.extras!!.getString("picture")).into(imageView)
        textViewLocation.text = intent.extras!!.getString("location")
        textViewAuthor.text = "Dodane przez: " + auth.currentUser!!.email
        textViewNote.text = intent.extras!!.getString("note")
    }
}