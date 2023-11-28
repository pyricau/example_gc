package com.example.demo


sealed class JavaObject {

  // Used by Heap.gc()
  var marked = false

  class JavaClass(val name: String, val parentClass: JavaClass? = null) : JavaObject() {
    val staticFields = mutableMapOf<String, JavaObject?>()

    override fun toString(): String {
      return "JavaClass(name='$name', parentClass=${parentClass?.name}, staticFields=${staticFields.keys})"
    }
  }

  class JavaInstance(
    val clazz: JavaClass
  ) : JavaObject() {
    val fields = mutableMapOf<String, JavaObject?>()

    override fun toString(): String {
      return "JavaInstance(clazz=${clazz.name}, fields=${fields.keys})"
    }
  }

  class JavaPrimitiveArray : JavaObject()

  class JavaObjectArray(
    val clazz: JavaClass,
    val size: Int
  ) : JavaObject() {
    val array = arrayOfNulls<JavaObject>(size)

    override fun toString(): String {
      return "JavaObjectArray(clazz=$clazz, size=$size)"
    }
  }
}