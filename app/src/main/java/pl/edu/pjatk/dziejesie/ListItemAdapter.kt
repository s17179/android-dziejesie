package pl.edu.pjatk.dziejesie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class ListItemAdapter(
    private val items: MutableList<ListItem>,
    private val context: ListActivity
): RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {
    class ListItemViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val holder = ListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )

        return holder
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val currentItem = items[position]

        holder.itemView.apply {
            Picasso.get().load(currentItem.picture).into(imageView)
            textViewName.text = currentItem.name
            textViewLocation.text = currentItem.location
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemDetailsActivity::class.java)

            intent.putExtra("name", currentItem.name)
            intent.putExtra("picture", currentItem.picture)
            intent.putExtra("location", currentItem.location)
            intent.putExtra("note", currentItem.note)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}