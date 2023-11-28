package com.example.demo

import com.example.demo.JavaObject.JavaClass
import com.example.demo.JavaObject.JavaInstance
import com.example.demo.JavaObject.JavaObjectArray
import com.example.demo.JavaObject.JavaPrimitiveArray

class Heap {

  val objects = mutableSetOf<JavaObject>()

  fun newJavaClass(
    name: String,
    parentClass: JavaClass? = null
  ): JavaClass {
    return JavaClass(name, parentClass).apply { objects += this }
  }

  fun JavaClass.newInstance(): JavaInstance {
    return JavaInstance(this).apply { objects += this }
  }

  fun gc() {
    markObjectsReachableFromRoots()
    sweepObjectsNotMarked()
  }

  private fun markObjectsReachableFromRoots() {
    val queue = mutableListOf<JavaObject>()

    val gcRoots = objects.filterIsInstance<JavaClass>()
    queue += gcRoots
    while (queue.isNotEmpty()) {
      val dequeuedObject = queue.removeFirst()
      if (!dequeuedObject.marked) {
        dequeuedObject.marked = true
        val referencedObjects = dequeuedObject.findReferencedObjects()
        val unmarkedObjects = referencedObjects.filter { !it.marked }
        queue.addAll(unmarkedObjects)
      }
    }
  }

  private fun JavaObject.findReferencedObjects() = when (this) {
    is JavaClass -> staticFields.values.filterNotNull()
    is JavaInstance -> fields.values.filterNotNull()
    is JavaObjectArray -> array.filterNotNull()
    is JavaPrimitiveArray -> emptyList()
  }

  private fun sweepObjectsNotMarked() {
    objects.removeAll { !it.marked }

    // reset marked.
    objects.forEach {
      it.marked = false
    }
  }
}