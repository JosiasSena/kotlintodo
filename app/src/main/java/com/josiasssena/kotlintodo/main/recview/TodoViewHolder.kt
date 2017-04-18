package com.josiasssena.kotlintodo.main.recview

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.rec_view_item.view.*

/**
 * @author Josias Sena
 */
class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = itemView.tv_title!!
    val body = itemView.tv_body!!
}
