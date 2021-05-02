package com.example.newsapp



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class NewsListAdapter(private val listener : NewsItemClicked) : RecyclerView.Adapter<NewsViewHolder>() {
    private val items =  ArrayList<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        when {
            currentItem.author.toString() == "null" -> {
                holder.author.text = ""
            }
            else -> {
                holder.author.text = currentItem.author.toString()
            }
        }
        Glide.with(holder.itemView.context).load(currentItem.urlToImage).into(holder.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateNews(updatedNews: ArrayList<News>) {
        items.clear()
        items.addAll(updatedNews)
        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView : TextView = itemView.findViewById(R.id.title_view)
    val image : ImageView = itemView.findViewById(R.id.img_view)
    val author : TextView = itemView.findViewById(R.id.author)
}

interface NewsItemClicked {
    fun onItemClicked(item : News)
}