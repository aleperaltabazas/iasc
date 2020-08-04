package utn.frba.iasc.babylon.storage

import utn.frba.iasc.babylon.model.Entity

abstract class Storage<T : Entity>(
    private val ts: MutableList<T> = mutableListOf()
) {
    fun add(t: T) = ts.add(t)

    fun update(t: T) {
        ts.removeIf { it.id == t.id }
        ts.add(t)
    }

    fun remove(t: T) = ts.remove(t)

    fun find(id: String): T? = ts.find { it.id == id }

    fun find(f: (T) -> Boolean): T? = ts.find(f)

    fun findAll(): List<T> = ts

    fun findAll(f: (T) -> Boolean): List<T> = ts.filter(f)
}