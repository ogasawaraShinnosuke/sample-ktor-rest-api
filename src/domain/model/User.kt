package com.a_pags_server.domain.model

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.transactions.transaction

data class UserDTO(
    val id: Long = 0L,
    val fullName: String = "NO NAME",
    val hometown: String = "NO HOMETOWN",
    val residence: String = "NO RESIDENCE",
    val education: String = "NO EDUCATION",
    val job: String = "NO JOB"
)
object Users: LongIdTable() {
    val fullName = varchar("full_name", 256)
    val hometown = varchar("hometown", 256)
    val residence = varchar("residence", 256)
    val education = varchar("education", 256)
    val job = varchar("job", 256)
}
class UserEntity(id: EntityID<Long>): LongEntity(id) {
    companion object: IntEntityClass<UserEntity>(Users)
    var fullName by Users.fullName
    var hometown by Users.hometown
    var residence by Users.residence
    var education by Users.education
    var job by Users.job

    fun entity2dto(): UserDTO {
        val dto = UserDTO(
            id = id.value,
            fullName = fullName,
            hometown = hometown,
            residence = residence,
            education = education,
            job = job
        )
        return dto
    }
}

class UserDao: Dao<UserDTO> {

    override fun findAll(): List<UserDTO> {
        return transaction {
            return@transaction UserEntity.all().map { it.entity2dto() }
        }
    }

    override fun findById(id: Long): UserDTO {
        return transaction {
            val user = UserEntity.findById(id)?.entity2dto()
            return@transaction user ?: UserDTO()
        }
    }
}

