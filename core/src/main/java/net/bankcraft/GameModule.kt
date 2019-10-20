package net.bankcraft

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld
import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Provides
import com.google.inject.Singleton

data class Systems(val list : List<Class<out EntitySystem>>)

data class Listeners(val list: List<Pair<Family, out EntityListener>>)

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
                RenderingSystem::class.java,
                LiftMovingSystem::class.java,
                DeathSystem::class.java
        ))
    }

    @Provides
    @Singleton
    fun listeners(): Listeners {
        return Listeners(listOf(
                Pair(Family.all(ShapeComponent::class.java).get(), ShapeListener())
        ))
    }

    @Provides
    @Singleton
    fun camera(): PerspectiveCamera {
        return PerspectiveCamera(67F, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).apply {
            position.set(3F, 7F, 15F)
            lookAt(0F, 4F, 0F)
            near = 1F
            far = 300F
            update()
        }
    }

    @Provides
    @Singleton
    fun environment(): Environment {
        return Environment().apply {
            set(ColorAttribute(ColorAttribute.AmbientLight, 0.4F, 0.4F, 0.4F, 1F))
            add(DirectionalLight().set(0.8F, 0.8F, 0.8F, -1F, -0.8F, -0.2F))
        }
    }

    @Provides
    @Singleton
    fun engine() : Engine = bankCraftGame.engine

    @Provides
    @Singleton
    fun dynamicsWorld(): btDynamicsWorld = bankCraftGame.dynamicsWorld
}