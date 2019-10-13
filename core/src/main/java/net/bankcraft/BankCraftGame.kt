package net.bankcraft

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.*
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
    lateinit var collisionWorld: btCollisionWorld

    override fun create() {
        modelBatch = ModelBatch()
        injector = Guice.createInjector(GameModule(this))

        Bullet.init()
        collisionConfig = btDefaultCollisionConfiguration()
        dispatcher = btCollisionDispatcher(collisionConfig)
        broadphase = btDbvtBroadphase()
        collisionWorld = btCollisionWorld(dispatcher, broadphase, collisionConfig)
        contactListener = MyContactListener(engine)

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
                collisionWorld.addCollisionObject(body, GROUND_FLAG, ALL_FLAG)
            }))
        })
    }

    override fun render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        engine.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        dispatcher.dispose()
        collisionConfig.dispose()
        collisionWorld.dispose()
        broadphase.dispose()
        contactListener.dispose()
        modelBatch.dispose()
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
            instances.get(userValue0).gameObject.gameObject.apply { moving = false }
        } else if (userValue0 == 0) {
            instances.get(userValue1).gameObject.gameObject.apply { moving = false }
        }
        return true
    }
}