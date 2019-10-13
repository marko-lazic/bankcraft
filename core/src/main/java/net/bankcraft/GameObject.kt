package net.bankcraft

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Disposable

class GameObject(model: Model, node: String, constructionInfo: btRigidBody.btRigidBodyConstructionInfo) : ModelInstance(model, node), Disposable {
    val body: btRigidBody = btRigidBody(constructionInfo)

    override fun dispose() {
        body.dispose()
    }

    class Constructor(private val model: Model,
                      private val node: String,
                      private val shape: btCollisionShape,
                      mass: Mass) : Disposable {
        private val constructionInfo: btRigidBody.btRigidBodyConstructionInfo

        companion object {
            private val localInertia = Vector3()
        }

        init {
            if (mass is Mass.Dynamic) {
                shape.calculateLocalInertia(mass.toFloat(), localInertia)
            } else {
                localInertia.set(0F, 0F, 0F)
            }

            this.constructionInfo = btRigidBody.btRigidBodyConstructionInfo(mass.toFloat(), null, shape, localInertia)
        }

        fun construct(): GameObject {
            return GameObject(model, node, constructionInfo)
        }

        override fun dispose() {
            shape.dispose()
            constructionInfo.dispose()
        }
    }
}

sealed class Mass {
    object Static : Mass()
    class Dynamic(val kilograms: Float) : Mass()
}

fun Mass.toFloat(): Float {
    return when (this) {
        is Mass.Static -> 0F
        is Mass.Dynamic -> this.kilograms
    }
}