package net.bankcraft

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld
import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Provides
import com.google.inject.Singleton

data class Systems(val list : List<Class<out EntitySystem>>)

class GameModule(private val bankCraftGame: BankCraftGame) : Module {

    override fun configure(binder: Binder) {
        binder.requireAtInjectOnConstructors()
        binder.requireExactBindingAnnotations()
        binder.bind(ModelBatch::class.java).toInstance(bankCraftGame.modelBatch)
    }

    @Provides
    @Singleton
    fun systems() : Systems {
        return Systems(listOf(
                SpawnSystem::class.java,
                PhysicsSystem::class.java,
                CollisionSystem::class.java,
                RenderingSystem::class.java
        ))
    }

    @Provides
    @Singleton
    fun camera(): PerspectiveCamera {
        return PerspectiveCamera(67F, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).apply {
            position.set(3f, 7f, 15f)
            lookAt(0F, 4f, 0F)
            update()
        }
    }

    @Provides
    @Singleton
    fun environment(): Environment {
        return Environment().apply {
            set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
            add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
        }
    }

    @Provides
    @Singleton
    fun engine() : Engine = bankCraftGame.engine

    @Provides
    @Singleton
    fun collisionWorld(): btCollisionWorld = bankCraftGame.collisionWorld
}