package com.autopart.backend.repository

import com.autopart.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface serRepository : JpaRepository<User, Long>
