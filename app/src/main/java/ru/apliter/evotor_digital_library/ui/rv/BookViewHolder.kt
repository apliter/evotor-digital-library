package ru.apliter.evotor_digital_library.ui.rv

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_book.view.*
import ru.apliter.data.entities.DataBook

class BookViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {


    fun bind(item: DataBook) {
        containerView.author_text_view.text = item.author
        containerView.title_text_view.text = item.title
        containerView.label_is_vacant.visibility = if (item.vacant) View.GONE else View.VISIBLE
    }
}