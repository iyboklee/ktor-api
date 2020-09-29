package com.github.iyboklee.server

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.github.iyboklee.serving.graphQL
import com.github.iyboklee.serving.restApis
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.event.Level
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Application.init() {
  val context = ApiServerContext(this).apply {
    init()
  }
  environment.monitor.subscribe(ApplicationStopped) {
    context.close()
  }

  initWithContext(context)
}

fun Application.initWithContext(context: ApiServerContext) {
  val environment = context.configuration.property("environment").getString()
  log.info("Environment: $environment")

  routing(context)

  install(CallLogging) {
    level = Level.INFO
    filter { call -> call.request.path().startsWith("/api") }
  }
  install(DefaultHeaders) {
    header(io.ktor.http.HttpHeaders.Server, "Api-Server")
  }
  install(ConditionalHeaders)
  install(XForwardedHeaderSupport)
  install(StatusPages) {
    exception<ServiceUnavailable> {
      call.respond(HttpStatusCode.ServiceUnavailable)
    }
    exception<BadRequest> {
      call.respond(HttpStatusCode.BadRequest)
    }
    exception<Unauthorized> {
      call.respond(HttpStatusCode.Unauthorized)
    }
    exception<NotFound> {
      call.respond(HttpStatusCode.NotFound)
    }
    exception<SecretInvalidError> {
      call.respond(HttpStatusCode.Forbidden)
    }
    exception<Throwable> { cause ->
      log.error("Unexpected exception occurred: ${cause.message}", cause)
      call.respond(HttpStatusCode.InternalServerError)
    }
  }
  install(ContentNegotiation) {
    jackson {
      disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

      setSerializationInclusion(JsonInclude.Include.NON_NULL)
      setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
      setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
      setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

      registerModule(JavaTimeModule().apply {
        addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME))
      })
    }
  }
  install(CORS) {
    anyHost()
  }
}

private fun Application.routing(context: ApiServerContext) {
  val configuration = context.configuration
  val staticRootDir = configuration.property("static").getString()
  val hcheckPath = configuration.property("hcheck").getString()
  val apisConfig = configuration.config("apis")

  install(Routing) {
    static {
      staticRootFolder = File(staticRootDir)

      file("/graphiql.html", "graphiql.html")
    }

    restApis(context, apisConfig.propertyOrNull("rest")?.getString() ?: "/api")
    graphQL(context, apisConfig.propertyOrNull("graphql")?.getString() ?: "/graphql")

    head(hcheckPath) {
      call.respond(HttpStatusCode.OK)
    }
  }
}