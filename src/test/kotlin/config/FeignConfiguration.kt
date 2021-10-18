package config

import clients.*
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration


@EnableFeignClients(
    clients = [
        ActorClient::class,
        RoleClient::class,
        SpectacleClient::class,
        SpecRoleClient::class,
        SpecActorClient::class,
        SpecRoleActorClient::class,
        RepertoireClient::class,
    ]
)
@Configuration
@EnableAutoConfiguration
class FeignConfiguration