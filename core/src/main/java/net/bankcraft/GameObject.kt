package net.bankcraft

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import com.badlogic.gdx.utils.Disposable

class GameObject(model: Model, node: String, shape: btCollisionShape) : ModelInstance(model, node), Disposable {
    val body: btCollisionObject
    var moving: Boolean = false

    init {
        body = btCollisionObject()
        body.collisionShape = shape
    }

    override fun dispose() {
        body.dispose()
    }

    class Constructor(val model: Model, val node: String, val shape: btCollisionShape) : Disposable {

        fun construct(): GameObject {
            return GameObject(model, node, shape)
        }

        override fun dispose() {
            shape.dispose()
        }
    }
}