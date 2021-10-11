package config

import clients.ActorClient
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration


@EnableFeignClients(
    clients = [
        ActorClient::class
    ]
)
@Configuration
@EnableAutoConfiguration
class FeignConfiguration