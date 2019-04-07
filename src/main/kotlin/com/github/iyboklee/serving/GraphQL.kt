package com.github.iyboklee.serving

import com.github.iyboklee.server.ApiServerContext
import com.github.pgutkowski.kgraphql.RequestException
import com.github.pgutkowski.kgraphql.schema.Schema
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route

data class GraphQLRequest(val query: String)

data class Error(val message: String?, val errorType: String)

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
            try {
                val response = getSchema().execute(request.query)
                call.respondText(response, ContentType.Application.Json)
            } catch (e: RequestException) {
                call.respond(Errors(listOf(
                    Error(e.message, e.javaClass.simpleName)
                )))
            }
        }
    }
}