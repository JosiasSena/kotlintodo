package com.josiasssena.kotlintodo.realm

import com.google.firebase.database.FirebaseDatabase
import com.josiasssena.kotlintodo.core.Todo
import io.realm.Realm
import java.util.*

/**
 * @author Josias Sena
 */
class TodoRealmManager {

    fun getAllToDos(): List<Todo> {
        val realm = Realm.getDefaultInstance()
        return realm.where(Todo::class.java).findAll()
    }

    fun insertTodo(todo: Todo) {
        val realm = Realm.getDefaultInstance()
        val id = UUID.randomUUID().toString()

        realm.executeTransaction { realm ->
            todo.id = id
            realm.insert(todo)

            FirebaseDatabase.getInstance().getReference("todos")
                    .child(id)

                    .setValue(todo)
        }
    }
}