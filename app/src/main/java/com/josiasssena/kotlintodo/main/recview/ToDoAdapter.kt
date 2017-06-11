package com.josiasssena.kotlintodo.main.recview

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import com.josiasssena.kotlintodo.R
import com.josiasssena.kotlintodo.core.Todo
import com.josiasssena.kotlintodo.realm.TodoRealmManager
import io.realm.Realm

/**
 * File created by josiassena on 4/11/17.
 */
class ToDoAdapter : RecyclerView.Adapter<TodoViewHolder>() {

    private val todoList: List<Todo> = TodoRealmManager().getAllToDos()
    private val realm = Realm.getDefaultInstance()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return TodoViewHolder(layoutInflater.inflate(R.layout.rec_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]

        holder.title.text = todo.title
        holder.body.text = todo.body

        holder.itemView.setOnLongClickListener {
            displayDeleteTodoDialog(holder.itemView.context, todo)
            true
        }

        holder.itemView.setOnClickListener {
            displayEditTodoDialog(holder.itemView.context, todo)
        }
    }

    private fun displayEditTodoDialog(context: Context, todo: Todo) {
        val body = LayoutInflater.from(context).inflate(R.layout.todo_dialog, null)
        val etTitle = body.findViewById(R.id.et_title) as EditText
        val etBody = body.findViewById(R.id.et_body) as EditText

        etTitle.setText(todo.title)
        etBody.setText(todo.body)

        AlertDialog.Builder(context)
                .setView(body)
                .setTitle(context.getString(R.string.edit_todo))
                .setPositiveButton(context.getString(R.string.save), { _, _ ->
                    realm.executeTransaction {
                        todo.title = etTitle.text.toString()
                        todo.body = etBody.text.toString()
                        notifyItemChanged(todoList.indexOf(todo))

                        FirebaseDatabase.getInstance()
                                .getReference("todos")
                                .child(todo.id)
                                .setValue(todo)
                    }
                })
                .show()
    }

    private fun displayDeleteTodoDialog(context: Context, todo: Todo) {
        AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(context.getString(R.string.delete_dialog_titile))
                .setMessage(context.getString(R.string.delete_dialog_body))
                .setPositiveButton(android.R.string.ok, { _, _ ->
                    realm.executeTransaction {
                        val id = todo.id

                        notifyItemRemoved(todoList.indexOf(todo))
                        todo.deleteFromRealm()

                        FirebaseDatabase.getInstance()
                                .getReference("todos")
                                .child(id)
                                .removeValue()
                    }
                })
                .setNegativeButton(android.R.string.cancel, { dialog, _ ->
                    dialog.dismiss()
                })
                .show()
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

}
