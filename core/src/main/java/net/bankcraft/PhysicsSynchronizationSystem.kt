package net.bankcraft

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.google.inject.Inject

class PhysicsSynchronizationSystem @Inject constructor()
    : IteratingSystem(Family.all(GameObjectComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.gameObject.gameObject.apply {
            body.getWorldTransform(transform)
        }
    }
}