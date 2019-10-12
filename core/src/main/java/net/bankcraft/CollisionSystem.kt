package net.bankcraft

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld
import com.google.inject.Inject

class CollisionSystem @Inject constructor(private val collisionWorld: btCollisionWorld) : EntitySystem() {

    override fun update(deltaTime: Float) {
        collisionWorld.performDiscreteCollisionDetection()
    }
}