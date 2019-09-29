package net.bankcraft

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.google.inject.Inject

class UserInputAdapter @Inject constructor(private val camera: OrthographicCamera,
                                           private val engine: Engine,
                                           private val world: World) : InputAdapter() {
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
         val worldPosition = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0F))

        engine.addEntity(Entity().apply {
            add(TextureRegionComponent(TextureRegion(BankCraftGame.img)))
            add(TransformComponent(Vector2(worldPosition.x, worldPosition.y), 0F, 0.25F))

            val body = world.createBody(BodyDef().apply {
                type = BodyDef.BodyType.DynamicBody
            })
            body.createFixture(PolygonShape().apply {
                setAsBox(1F,1F)
            }, 1F)
            body.setTransform(transform.position, 0F)
            add(PhysicsComponent(body))
        })
        return true

    }
}