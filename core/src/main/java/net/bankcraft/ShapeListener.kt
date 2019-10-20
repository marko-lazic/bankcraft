package net.bankcraft

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener

class ShapeListener : EntityListener {

    override fun entityRemoved(entity: Entity) {
        entity.shape.gameObject.dispose()
    }

    override fun entityAdded(entity: Entity?) {
    }
}