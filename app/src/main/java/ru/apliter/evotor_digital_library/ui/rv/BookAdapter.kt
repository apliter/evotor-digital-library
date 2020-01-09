package ru.apliter.evotor_digital_library.ui.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.apliter.data.entities.DataBook
import ru.apliter.evotor_digital_library.R

class BookAdapter(
    private inline val clickListener: (bookId: String) -> Unit
) : ListAdapter<DataBook, BookViewHolder>(
    callBack
) {

    private val onClickListener = View.OnClickListener { view ->
        clickListener((view.tag as DataBook).bookId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        val viewHolder =
            BookViewHolder(view)
        viewHolder.itemView.setOnClickListener(onClickListener)
        return viewHolder
    }

    override fun onViewRecycled(holder: BookViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.tag = null
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.itemView.tag = item
            holder.bind(item)
        }
    }

    companion object {
        private val callBack: DiffUtil.ItemCallback<DataBook> =
            object : DiffUtil.ItemCallback<DataBook>() {
                override fun areItemsTheSame(oldItem: DataBook, newItem: DataBook): Boolean {
                    return oldItem.bookId == newItem.bookId
                }

                override fun areContentsTheSame(oldItem: DataBook, newItem: DataBook): Boolean {
                    return oldItem.vacant == newItem.vacant
                }

            }
    }
}