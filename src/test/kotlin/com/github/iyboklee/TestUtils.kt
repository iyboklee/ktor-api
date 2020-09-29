package com.github.iyboklee

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.iyboklee.repository.Database
import com.zaxxer.hikari.HikariConfig
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val Jackson = jacksonObjectMapper().apply {
  enable(SerializationFeature.INDENT_OUTPUT)
  disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  setSerializationInclusion(JsonInclude.Include.NON_NULL)
  setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
  setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
  setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

  registerModule(JavaTimeModule().apply {
      addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME))
  })
}

fun database(
    name: String,
    driverClassName: String = "org.h2.Driver",
    jdbcUrl: String = "jdbc:h2:mem:test;MODE=MYSQL;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'conf/local/h2-init.sql'",
    username: String = "sa",
    password: String = "sa",
    minimumIdle: Int = 1,
    maximumPoolSize: Int = 3
) {
  Database.init(name) {
    HikariConfig().apply {
      poolName = name
      this.driverClassName = driverClassName
      this.jdbcUrl = jdbcUrl
      this.username = username
      this.password = password
      this.minimumIdle = minimumIdle
      this.maximumPoolSize = maximumPoolSize
    }
  }
}