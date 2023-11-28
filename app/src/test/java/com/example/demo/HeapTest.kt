package com.example.demo

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HeapTest {

  @Test
  fun `can add objects to the heap`() {
    val heap = Heap()
    with(heap) {
      val exampleClass = newJavaClass("com.example.ExampleClass")
      val instance = exampleClass.newInstance()

      assertThat(objects).containsOnly(exampleClass, instance)
    }
  }

  @Test
  fun `Calling gc() removes unreachable instance`() {
    val heap = Heap()
    with(heap) {
      val exampleClass = newJavaClass("com.example.ExampleClass")
      val instance = exampleClass.newInstance()

      gc()

      assertThat(objects).doesNotContain(instance)
    }
  }

  @Test
  fun `Calling gc() does not remove statically reachable instance`() {
    val heap = Heap()
    with(heap) {
      val exampleClass = newJavaClass("com.example.ExampleClass")
      val instance = exampleClass.newInstance()

      exampleClass.staticFields["singleton"] = instance

      gc()

      // instance is still reachable after GC
      assertThat(objects).contains(instance)
    }
  }

  @Test
  fun `Clearing static field does not remove unreachable instance`() {
    val heap = Heap()
    with(heap) {
      val exampleClass = newJavaClass("com.example.ExampleClass")
      val instance = exampleClass.newInstance()

      exampleClass.staticFields["singleton"] = instance

      gc()

      exampleClass.staticFields["singleton"] = null

      // instance is unreachable, but the heap doesn't know yet (no gc)
      assertThat(objects).contains(instance)
    }
  }

  @Test
  fun `Calling gc() after clearing static field removes unreachable instance`() {
    val heap = Heap()
    with(heap) {
      val exampleClass = newJavaClass("com.example.ExampleClass")
      val instance = exampleClass.newInstance()

      exampleClass.staticFields["singleton"] = instance

      gc()

      exampleClass.staticFields["singleton"] = null

      gc()

      assertThat(objects).doesNotContain(instance)
    }
  }
}