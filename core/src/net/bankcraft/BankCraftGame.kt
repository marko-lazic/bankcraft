package net.bankcraft

import com.badlogic.ashley.core.*
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.google.inject.*

class BankCraftGame : ApplicationAdapter() {
    internal lateinit var batch: SpriteBatch
    internal lateinit var img: Texture
    internal lateinit var shapeRenderer: ShapeRenderer
    internal val engine = Engine()
    private lateinit var injector : Injector

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        shapeRenderer = ShapeRenderer()
        injector = Guice.createInjector(GameModule(this))
        injector.getInstance(Systems::class.java).list.map { injector.getInstance(it) }.forEach{ system ->
            engine.addSystem(system)
        }

        createEntities()
    }

    private fun createEntities() {
        engine.addEntity(Entity().apply {
            add(TextureComponent(img))
            add(TransformComponent(Vector2(0F, 0F)))
        })
    }

    private val position: Vector2 = Vector2(300f, 220f)
    private var time: Float = 0f

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        engine.update(Gdx.graphics.deltaTime)



        val (x,y) = position
        time += Gdx.graphics.deltaTime * 10f
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.CHARTREUSE
        shapeRenderer.circle(x + MathUtils.cos(time), y, 60f)
        shapeRenderer.end()

    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
        shapeRenderer.dispose()
    }
}

open class ComponentResolver<T : Component>(componentCLass: Class<T>) {
    var MAPPER = ComponentMapper.getFor(componentCLass)
    operator fun get(entity: Entity) = MAPPER.get(entity)
}

//class SpamSystem @Inject constructor(private val spriteBatch: SpriteBatch): EntitySystem() {
//    override fun update(deltaTime: Float) {
//        println(deltaTime.toString() + "; " + spriteBatch)
//    }
//}

class RenderingSystem @Inject constructor(private val batch: SpriteBatch): IteratingSystem(Family.all(TransformComponent::class.java, TextureComponent::class.java).get()) {
    override fun update(deltaTime: Float) {
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val img = entity.texture.texture
        val position = entity.transform.position
        batch.draw(img, position.x,position.y)
    }
}

class GameModule(private val bankCraftGame: BankCraftGame) : Module {
    override fun configure(binder: Binder) {
        binder.bind(SpriteBatch::class.java).toInstance(bankCraftGame.batch)
    }

    @Provides @Singleton
    fun systems() : Systems {
        return Systems(listOf(
            RenderingSystem::class.java
        ))
    }
}

data class Systems(val list : List<Class<out EntitySystem>>)

private operator fun Vector2.component1(): Float = this.x

private operator fun Vector2.component2(): Float = this.y

fun infixMethod(value: Vector2) {
    // this method takes Vector2
}

fun infixPractice() {
    infixMethod(8 v2 9)
}

private infix fun Number.v2(other: Number): Vector2 {
    return Vector2(this.toFloat(), other.toFloat())
}
