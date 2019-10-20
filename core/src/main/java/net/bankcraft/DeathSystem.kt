package net.bankcraft

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld
import com.google.inject.Inject

class DeathSystem @Inject constructor(private val dynamicWorld: btDynamicsWorld)
    : IteratingSystem(Family.all(ShapeComponent::class.java).exclude(LiftComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val translation = entity.shape.gameObject.body.centerOfMassPosition
        if (translation.y < -5) {
            dynamicWorld.removeRigidBody(entity.shape.gameObject.body)
            engine.removeEntity(entity)
        }
    }
}