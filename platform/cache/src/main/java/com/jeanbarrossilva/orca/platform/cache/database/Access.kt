package com.jeanbarrossilva.orca.platform.cache.database

import androidx.annotation.Discouraged
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Holds the [key] to which a value has been associated and the moment in which it was last
 * accessed.
 *
 * @param id Unique identifier of this [Access].
 * @param key Unique identifier of the stored value.
 * @param type [Type] that indicates how the value was accessed.
 * @param time Time that's been elapsed when the access occurred.
 **/
@Entity(tableName = "accesses")
internal class Access
@Discouraged(message = "Use `Access.Companion.of` to create an Access instance.")
constructor(@PrimaryKey val id: String, val key: String, val type: Type, val time: Long) {
    /** Indicates how a value was accessed. **/
    enum class Type {
        /** Indicates that a value was written and read. **/
        IDLE,

        /** Indicates that a value was written (created or updated). **/
        ALIVE
    }

    override fun equals(other: Any?): Boolean {
        return other is Access && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Access(id=$id, key=$key, type=$type, time=$time)"
    }

    companion object {
        /**
         * Creates an [Access] instance.
         *
         * @param key Unique identifier of the stored value.
         * @param type [Type] that indicates how the value was accessed.
         * @param time Time that's been elapsed when the access occurred.
         **/
        fun of(key: String, type: Type, time: Long): Access {
            val id = "$type:$key"

            @Suppress("DiscouragedApi")
            return Access(id, key, type, time)
        }
    }
}
