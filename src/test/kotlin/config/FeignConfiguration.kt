package config

import clients.ActorClient
import clients.RoleClient
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration


@EnableFeignClients(
    clients = [
        ActorClient::class,
        RoleClient::class,
    ]
)
@Configuration
@EnableAutoConfiguration
class FeignConfiguration