package net.bankcraft

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld
import com.google.inject.Inject
import kotlin.math.min

class PhysicsSystem @Inject constructor(private val dynamicsWorld: btDynamicsWorld) : EntitySystem() {

    override fun update(deltaTime: Float) {
        val delta = min(1f / 30F, deltaTime)
        dynamicsWorld.stepSimulation(delta, 5, 1F / 60F)
    }
}