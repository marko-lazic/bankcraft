package net.bankcraft

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.World
import com.google.inject.Inject

/**
 * https://github.com/libgdx/libgdx/wiki/Box2d#stepping-the-simulation
 * https://www.youtube.com/watch?v=1gEsFolfwvg
 */
class PhysicsSystem @Inject constructor(private val world: World) : EntitySystem() {
    private var accumulator = 0F

    override fun update(deltaTime: Float) {
        val frameTime = Math.min(deltaTime, 0.25F)
        accumulator += frameTime
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS)
            accumulator -= TIME_STEP
        }
    }

    companion object {
        const val TIME_STEP = 1.0F / 300F
        const val VELOCITY_ITERATIONS = 6
        const val POSITION_ITERATIONS = 2
    }
}