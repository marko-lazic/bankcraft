package net.bankcraft

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector

class BankCraftGame : ApplicationAdapter() {
    private lateinit var injector: Injector
    val engine = Engine()

    lateinit var modelBatch: ModelBatch

    lateinit var collisionConfig: btCollisionConfiguration
    lateinit var dispatcher: btDispatcher
    lateinit var contactListener: MyContactListener
    lateinit var broadphase: btBroadphaseInterface
    lateinit var dynamicsWorld: btDynamicsWorld
    lateinit var constraintSolver: btConstraintSolver

    override fun create() {
        modelBatch = ModelBatch()

        Bullet.init()
        collisionConfig = btDefaultCollisionConfiguration()
        dispatcher = btCollisionDispatcher(collisionConfig)
        broadphase = btDbvtBroadphase()
        constraintSolver = btSequentialImpulseConstraintSolver()
        dynamicsWorld = btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig)
        dynamicsWorld.gravity = Vector3(0F, -10F, 0F)
        contactListener = MyContactListener(engine)

        injector = Guice.createInjector(GameModule(this))
        initEntitySystems()
        createPlatformBox()
    }

    private fun initEntitySystems() {
        injector.getInstance(Systems::class.java).list.map {
            injector.getInstance(it)
        }.forEach { system ->
            engine.addSystem(system)
        }
    }

    private fun createPlatformBox() {
        engine.addEntity(Entity().apply {
            add(GameObjectComponent(GameObjectFactory.constructors.get("ground").construct().apply {
                dynamicsWorld.addRigidBody(body, GROUND_FLAG, ALL_FLAG)
            }))
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

        for (ctor in GameObjectFactory.constructors) {
            ctor.value.dispose()
        }
        GameObjectFactory().dispose()
    }

    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}

    companion object {
        const val GROUND_FLAG = (1 shl 8)
        const val OBJECT_FLAG = (1 shl 9)
        const val ALL_FLAG: Int = -1
    }
}

class MyContactListener @Inject constructor(val engine: Engine) : ContactListener() {
    override fun onContactAdded(userValue0: Int, partId0: Int, index0: Int, userValue1: Int, partId1: Int, index1: Int): Boolean {
        val instances = engine.getEntitiesFor(Family.all(GameObjectComponent::class.java).get())
        if (userValue1 == 0) {
            instances.get(userValue0).gameObject.gameObject.apply {
                (materials.get(0).get(ColorAttribute.Diffuse) as ColorAttribute).color.set(Color.WHITE)
            }
        } else if (userValue0 == 0) {
            instances.get(userValue1).gameObject.gameObject.apply {
                (materials.get(0).get(ColorAttribute.Diffuse) as ColorAttribute).color.set(Color.WHITE)
            }
        }
        return true
    }
}