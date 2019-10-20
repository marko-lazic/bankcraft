package net.bankcraft

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.math.MathUtils
import com.google.inject.Inject

class RenderingSystem @Inject constructor(private val batch: ModelBatch,
                                          private val camera: PerspectiveCamera,
                                          private val environment: Environment)
    : IteratingSystem(Family.all(ShapeComponent::class.java).one().get()) {

    private var camController: CameraInputController = CameraInputController(camera)

    init {
        Gdx.input.inputProcessor = camController
    }

    override fun update(deltaTime: Float) {
        camController.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        batch.begin(camera)
        super.update(deltaTime)
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val modelInstance = entity.shape.gameObject
        batch.render(modelInstance, environment)
    }
}

val Float.toDegrees: Float
    get() = MathUtils.radiansToDegrees * this
