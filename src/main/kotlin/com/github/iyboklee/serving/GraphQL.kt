package com.github.iyboklee.serving

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.iyboklee.server.ApiServerContext
import com.github.pgutkowski.kgraphql.RequestException
import com.github.pgutkowski.kgraphql.schema.Schema
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

val jackson = jacksonObjectMapper()

data class GraphQLRequest(
  val query: String,
  val variables: Any?
)

data class Error(
  val message: String?,
  val errorType: String
)

data class Errors(val errors: List<Error>)

fun Routing.graphQL(context: ApiServerContext, prefix: String) {
  execute(prefix) {
    context.schema
  }
}

private fun Routing.execute(prefix: String, getSchema: () -> Schema) {
  route(prefix) {
    post {
      val request = call.receive<GraphQLRequest>()
      val query = request.query
      val variables: String? = request.variables?.let {
        jackson.writeValueAsString(it)
      }
      try {
        val response = getSchema().execute(query, variables)
        call.respondText(response, ContentType.Application.Json)
      } catch (e: RequestException) {
        call.respond(
          Errors(
            listOf(
              Error(e.message, e.javaClass.simpleName)
            )
          )
        )
      }
    }
  }
}