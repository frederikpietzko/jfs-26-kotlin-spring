package com.example.petclinic

import com.example.petclinic.api.dto.OwnerRequest
import com.example.petclinic.api.mapper.OwnerMapper
import com.example.petclinic.service.OwnerService
import jakarta.validation.Validator
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router

@SpringBootApplication
@Import(ProgrammaticConfig::class)
class Jfs26KotlinSpringApplication

fun main(args: Array<String>) {
    runApplication<Jfs26KotlinSpringApplication>(*args)
}

class ProgrammaticConfig : BeanRegistrarDsl({

    if ("test" !in env.activeProfiles) {
        registerBean<PasswordEncoder> {
            BCryptPasswordEncoder()
        }

        registerBean<UserDetailsService> {
            val user = User.builder().username("user").password("password").roles("USER").build()
            val admin = User.builder().username("admin").password("password").roles("ADMIN").build()
            InMemoryUserDetailsManager(user, admin)
        }

        registerBean {
            val http = bean<HttpSecurity>()
            http {
                authorizeHttpRequests {
                    authorize(anyRequest, authenticated)
                }
                httpBasic { }
            }
            http.build()
        }
    }

    registerBean {
        OwnerHandler(bean(), bean(), bean())
    }
})

fun OwnerHandler(
    ownerService: OwnerService,
    ownerMapper: OwnerMapper,
    validator: Validator
): RouterFunction<ServerResponse> {
    return router {
        "/owners".nest {
            GET {
                ok().body(
                    ownerService
                        .findAll()
                        .map { ownerMapper.toOwnerResponse(it) }
                        .toList()
                )
            }
            POST {
                val req = it.body<OwnerRequest>()
                if (validator.validate(req).isNotEmpty()) {
                    return@POST badRequest().build()
                }
                val entity = ownerMapper.toOwner(req)
                status(HttpStatus.CREATED).body(
                    ownerService.save(entity)
                )
            }

            "{id}".nest {
                GET {
                    val id = it.pathVariable("id").toLong()
                    when (val owner = ownerService.findById(id)) {
                        null -> notFound().build()
                        else -> ok().body(owner)
                    }
                }
                PUT {
                    val id = it.pathVariable("id").toLong()
                    val updated = it.body<OwnerRequest>().also(validator::validate).let(ownerMapper::toOwner)
                    val entity = ownerService.update(id, updated)
                    if (entity == null) {
                        notFound().build()
                    } else {
                        ok().body(
                            ownerService.save(entity)
                        )
                    }
                }

                DELETE {
                    val id = it.pathVariable("id").toLong()
                    ownerService.delete(id)
                    noContent().build()
                }
            }
        }
    }
}