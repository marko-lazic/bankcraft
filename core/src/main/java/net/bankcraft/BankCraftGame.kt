package net.bankcraft

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver
import com.google.inject.Guice
import com.google.inject.Injector

class BankCraftGame : ApplicationAdapter() {
    private lateinit var ground: Entity
    private lateinit var injector: Injector
    val engine = Engine()

    lateinit var modelBatch: ModelBatch

    lateinit var collisionConfig: btCollisionConfiguration
    lateinit var dispatcher: btDispatcher
    lateinit var contactListener: BcContactListener
    lateinit var broadphase: btBroadphaseInterface
    lateinit var dynamicsWorld: btDynamicsWorld
    lateinit var constraintSolver: btConstraintSolver

    override fun create() {
        modelBatch = ModelBatch()
        injector = Guice.createInjector(GameModule(this))

        Bullet.init()
        collisionConfig = btDefaultCollisionConfiguration()
        dispatcher = btCollisionDispatcher(collisionConfig)
        broadphase = btDbvtBroadphase()
        constraintSolver = btSequentialImpulseConstraintSolver()
        dynamicsWorld = btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig)
        dynamicsWorld.gravity = Vector3(0F, -10F, 0F)
//        contactListener = injector.getInstance(BcContactListener::class.java)

        initEntitySystems()
        initEntityListeners()
        createPlatformBox()
    }

    private fun initEntitySystems() {
        injector.getInstance(Systems::class.java).list.map {
            injector.getInstance(it)
        }.forEach { system ->
            engine.addSystem(system)
        }
    }

    private fun initEntityListeners() {
        injector.getInstance(Listeners::class.java).list.map {
            engine.addEntityListener(it.first, it.second)
        }
    }

    private fun createPlatformBox() {
        engine.addEntity(Entity().apply {
            ground = add(ShapeComponent(GameObjectFactory.constructors.get("ground").construct().apply {
                body.collisionFlags = body.collisionFlags or btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT
                dynamicsWorld.addRigidBody(body)
                body.contactCallbackFlag = GROUND_FLAG
                body.contactCallbackFilter = 0
                body.activationState = Collision.DISABLE_DEACTIVATION
            })).add(LiftComponent())
        })
    }

    override fun render() {
        engine.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        dispatcher.dispose()
        collisionConfig.dispose()
        dynamicsWorld.dispose()
        constraintSolver.dispose()
        broadphase.dispose()
        contactListener.dispose()
        modelBatch.dispose()
        GameObjectFactory().dispose()
        engine.removeAllEntities()
    }

    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}

    companion object {
        const val GROUND_FLAG = (1 shl 8)
        const val OBJECT_FLAG = (1 shl 9)
        const val WALL_FLAG = (1 shl 10)
        const val ALL_FLAG: Int = -1
        var counter = 0
        fun nextCounter() = counter++
    }
}