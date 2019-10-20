package net.bankcraft

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.google.inject.Inject
import kotlin.math.min

class LiftMovingSystem @Inject constructor()
    : IteratingSystem(Family.all(ShapeComponent::class.java, LiftComponent::class.java).get()) {

    var angle: Float = 0F
    var speed = 90F

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val delta = min(1f / 30f, Gdx.graphics.deltaTime)
        angle = (angle + delta * speed) % 360f
        entity.shape.gameObject.transform.setTranslation(0F, MathUtils.sinDeg(angle) * 2.5F, 0F)
    }
}