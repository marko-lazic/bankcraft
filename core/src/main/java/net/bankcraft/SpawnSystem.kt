package net.bankcraft

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld
import com.google.inject.Inject

class SpawnSystem @Inject constructor(private val collisionWorld: btCollisionWorld) : EntitySystem() {

    var spawnTimer: Float = 0f

    override fun update(deltaTime: Float) {
        spawnTimer = spawnTimer.minus(deltaTime)
        if ((spawnTimer) < 0) {
            spawn()
            spawnTimer = 1.5f
        }
    }

    private fun spawn() {
        val constructors = GameObjectFactory.constructors
        val obj = constructors.values[1 + MathUtils.random(constructors.size - 2)].construct()
        engine.addEntity(Entity().apply {
            add(GameObjectComponent(obj.apply {
                moving = true
                transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f))
                transform.trn(MathUtils.random(-2.5f, 2.5f), 9f, MathUtils.random(-2.5f, 2.5f))
                body.worldTransform = transform
                body.userValue = engine.entities.size()
                body.collisionFlags = body.collisionFlags or btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK
                collisionWorld.addCollisionObject(body, BankCraftGame.OBJECT_FLAG, BankCraftGame.GROUND_FLAG)
            }))
        })
    }
}