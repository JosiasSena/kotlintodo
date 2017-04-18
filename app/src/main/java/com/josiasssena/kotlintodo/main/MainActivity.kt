package com.josiasssena.kotlintodo.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.widget.EditText
import com.josiasssena.kotlintodo.R
import com.josiasssena.kotlintodo.core.Todo
import com.josiasssena.kotlintodo.realm.TodoRealmManager
import com.josiasssena.kotlintodo.main.recview.ToDoAdapter

class MainActivity : AppCompatActivity() {

    val adapter = ToDoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {

            val body = LayoutInflater.from(this).inflate(R.layout.todo_dialog, null)
            val etTitle = body.findViewById(R.id.et_title) as EditText
            val etBody = body.findViewById(R.id.et_body) as EditText

            AlertDialog.Builder(this)
                    .setView(body)
                    .setTitle("Add new ToDo item")
                    .setPositiveButton("Save", { dialog, which ->
                        val todo = Todo()
                        todo.title = etTitle.text.toString()
                        todo.body = etBody.text.toString()

                        TodoRealmManager().insertTodo(todo)
                        adapter.notifyDataSetChanged()
                    })
                    .show()
        }

        initRecView()
    }

    private fun initRecView() {
        val recView = findViewById(R.id.rv_todo) as RecyclerView
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = adapter
        recView.setItemViewCacheSize(30)
    }
}
