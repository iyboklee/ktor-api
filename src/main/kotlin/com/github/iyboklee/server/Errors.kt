package com.github.iyboklee.server

class ServiceUnavailable : RuntimeException()

class BadRequest : RuntimeException()

class Unauthorized : RuntimeException()

class NotFound : RuntimeException()

class SecretInvalidError : RuntimeException()