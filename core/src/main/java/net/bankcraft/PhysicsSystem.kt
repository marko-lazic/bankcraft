package net.bankcraft

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.google.inject.Inject

class PhysicsSystem @Inject constructor() : IteratingSystem(Family.all(GameObjectComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val delta = Math.min(1f / 30f, Gdx.graphics.deltaTime)

        val obj = entity.gameObject.gameObject
        if (obj.moving) {
            obj.transform.trn(0f, -delta, 0f)
            obj.body.worldTransform = obj.transform
        }
    }
}