package com.frynet.theatre.data.spectacles_roles

import org.springframework.stereotype.Service


@Service
class SpecRoleConverter {

    fun toInfo(o: SpecRoleEntity) = SpecRoleInfo(
        o.id.role,
        o.main
    )
}