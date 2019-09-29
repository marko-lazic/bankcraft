package net.bankcraft

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.google.gson.Gson
import com.google.inject.Guice
import com.google.inject.Injector
import ktx.ashley.get
import org.jboss.resteasy.client.jaxrs.ResteasyClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlType

class BankCraftGame : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    private lateinit var coinImg: Texture
    val engine = Engine()
    private lateinit var injector: Injector
    private var worldCoinEntities = mutableListOf<Entity>()

    companion object {
        internal lateinit var img: Texture
    }

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        coinImg = Texture("euro.png")

        injector = Guice.createInjector(GameModule(this))
        injector.getInstance(Systems::class.java).list.map {
            injector.getInstance(it)
        }.forEach { system ->
            engine.addSystem(system)
        }
        createPlatform()
        updateCoins()

        Gdx.input.inputProcessor = injector.getInstance(UserInputAdapter::class.java)
    }

    private fun updateCoins() {
        val path = "http://localhost:8080/world"
        val client = ClientBuilder.newClient() as ResteasyClient
        val target = client.target(path)

        try {
            val response = target.request().get()
            val values = response.readEntity(String::class.java)
            val gson = Gson()
            val serverCoins = gson.fromJson(values, Array<Coin>::class.java)

            for (coin in serverCoins) {
                when (worldCoinEntities.find { it.get<IdComponent>()!!.id == coin.id }) {
                    null -> {
                        createCoin(coin)
                    }
//                    else -> {updateCoinPosition(it, coin)}
                }
            }
        } catch (e: Exception) {
            removeCoins(worldCoinEntities)
        }

    }

    private fun removeCoins(worldCoins: MutableList<Entity>) {
        for (coin in worldCoins) {
            removeCoin(coin)
        }
    }

    private fun removeCoin(coin: Entity) {
        engine.removeEntity(coin)
    }

    private fun createCoin(coin: Coin) {
        val world = injector.getInstance(World::class.java)
        val coinEntity = Entity().apply {
            add(TextureRegionComponent(TextureRegion(coinImg)))
            val (id, x, y) = coin
            add(IdComponent(id))
            add(TransformComponent(Vector2(x.pixelsToMeters, y.pixelsToMeters), 0F, 1F))

            val body = world.createBody(BodyDef().apply {
                type = BodyDef.BodyType.DynamicBody
            })
            body.createFixture(CircleShape().apply {
                radius = coinImg.width.pixelsToMeters / 2.2F
                position = Vector2(-0.3F, 0.3F)
            }, 1.0F)
            body.setTransform(transform.position, 0F)
            add(PhysicsComponent(body))
        }
        engine.addEntity(coinEntity)
        worldCoinEntities.add(coinEntity)
    }

    private fun createPlatform() {
        val world = injector.getInstance(World::class.java)
        engine.addEntity(Entity().apply {
            add(TransformComponent(Vector2(0F, 0F)))

            val body = world.createBody(BodyDef().apply {
                type = BodyDef.BodyType.StaticBody
            })
            body.createFixture(PolygonShape().apply {
                setAsBox(25F, 1F)
            }, 1.0F)
        })
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        updateCoins()
        engine.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
        coinImg.dispose()
    }
}

data class Coin(val id: Int, val x: Int, val y: Int)