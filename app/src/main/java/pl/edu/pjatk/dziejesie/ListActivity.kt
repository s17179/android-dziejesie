package pl.edu.pjatk.dziejesie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {
    private lateinit var listItemAdapter: ListItemAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(
                this,
                "Wylogowano pomyślnie",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(Intent(this, MainActivity::class.java))

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val item = ListItem(
            "https://firebasestorage.googleapis.com/v0/b/dzieje-sie-fded9.appspot.com/o/8451f53a-b858-4098-9e49-7e571792d13f?alt=media&token=e1b2e3df-e6ae-467d-be5a-dfc5b558e29c",
            "Wypadek samochodowy",
            "ul. Koszykowa, Warszawa",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec venenatis, ante ut tempus lobortis, leo tellus suscipit purus, vel rhoncus quam nisi eu lacus. Etiam pulvinar arcu at sapien ullamcorper pharetra. Donec eu justo ut lorem vulputate interdum. Vestibulum maximus enim id nunc pharetra, vel vestibulum dolor mattis."
        )
        listItemAdapter = ListItemAdapter(arrayListOf(item), this)

        recyclerViewList.adapter = listItemAdapter
        recyclerViewList.layoutManager = LinearLayoutManager(this)
    }
}