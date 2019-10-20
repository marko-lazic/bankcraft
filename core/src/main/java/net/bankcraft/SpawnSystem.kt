package net.bankcraft

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld
import com.google.inject.Inject

class SpawnSystem @Inject constructor(private val dynamicsWorld: btDynamicsWorld) : EntitySystem() {

    var spawnTimer: Float = 0f

    override fun update(deltaTime: Float) {
        spawnTimer = spawnTimer.minus(deltaTime)
        if ((spawnTimer) < 0) {
            spawn()
            spawnTimer = 0.5f
        }
    }

    private fun spawn() {
        val constructors = GameObjectFactory.constructors
        val obj = constructors.values[1 + MathUtils.random(constructors.size - 2)].construct()
        engine.addEntity(Entity().apply {
            add(ShapeComponent(obj.apply {
                transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f))
                transform.trn(MathUtils.random(-2.5f, 2.5f), 9f, MathUtils.random(-2.5f, 2.5f))
                body.proceedToTransform(transform)
                body.userValue = BankCraftGame.nextCounter()
                body.collisionFlags = body.collisionFlags or btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK
                dynamicsWorld.addRigidBody(body)
                body.contactCallbackFlag = BankCraftGame.OBJECT_FLAG
                body.contactCallbackFilter = BankCraftGame.GROUND_FLAG or BankCraftGame.WALL_FLAG
            }))
        })
    }
}