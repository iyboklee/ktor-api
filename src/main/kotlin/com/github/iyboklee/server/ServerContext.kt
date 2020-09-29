package com.github.iyboklee.server

interface ServerContext : AutoCloseable {

  val name: String

  fun init()

}