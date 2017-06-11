package com.josiasssena.kotlintodo.realm

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
        realm.executeTransaction { realm ->
            todo.id = UUID.randomUUID().toString()
            realm.insert(todo)
        }
    }

}