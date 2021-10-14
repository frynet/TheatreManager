package com.frynet.theatre.spectacles_roles

import org.springframework.stereotype.Service


@Service
class SpecRoleConverter {

    fun toInfo(o: SpecRoleEntity) = SpecRoleInfo(
        o.id.role,
        o.main
    )
}