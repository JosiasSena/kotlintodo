package com.josiasssena.kotlintodo.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.josiasssena.kotlintodo.R
import com.josiasssena.kotlintodo.core.Todo
import com.josiasssena.kotlintodo.main.recview.ToDoAdapter
import com.josiasssena.kotlintodo.realm.TodoRealmManager
import io.realm.Realm

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val adapter = ToDoAdapter()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private var addTodoDialog: AlertDialog? = null
    private var dialogBody: View? = null

    companion object {
        val TAG = MainActivity::class.java.simpleName!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(this)

        buildAddTodoDialog()

        initRecView()

        getTodos()
    }

    private fun getTodos() {
        val ref = database.getReference("todos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val realm = Realm.getDefaultInstance()
                val children = dataSnapshot.children.toList()

                children.asSequence()
                        .map { it.getValue(Todo::class.java) }
                        .forEach {
                            val todo = it

                            realm.executeTransaction {
                                realm.insertOrUpdate(todo)
                                adapter.notifyDataSetChanged()
                            }
                        }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: " + error.message)
            }
        })
    }

    override fun onDestroy() {
        addTodoDialog?.hide()
        super.onDestroy()
    }

    private fun initRecView() {
        val recView = findViewById(R.id.rv_todo) as RecyclerView
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = adapter
        recView.setItemViewCacheSize(30)
    }

    override fun onClick(v: View?) {
        clearDialogViews()
        addTodoDialog?.show()
    }

    private fun clearDialogViews() {
        val etTitle = dialogBody?.findViewById(R.id.et_title) as EditText
        val etBody = dialogBody?.findViewById(R.id.et_body) as EditText

        etTitle.setText("")
        etBody.setText("")
    }

    private fun buildAddTodoDialog() {
        dialogBody = LayoutInflater.from(this).inflate(R.layout.todo_dialog, null)
        val etTitle = dialogBody?.findViewById(R.id.et_title) as EditText
        val etBody = dialogBody?.findViewById(R.id.et_body) as EditText

        addTodoDialog = AlertDialog.Builder(this)
                .setView(dialogBody)
                .setTitle(R.string.add_new_todo)
                .setPositiveButton(R.string.save, { _, _ ->
                    val todo = Todo()
                    todo.title = etTitle.text.toString()
                    todo.body = etBody.text.toString()

                    TodoRealmManager().insertTodo(todo)
                    adapter.notifyDataSetChanged()
                }).create()
    }
}
