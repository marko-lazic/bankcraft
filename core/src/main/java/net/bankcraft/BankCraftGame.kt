package net.bankcraft

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btSphereShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.collision.btDispatcher
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper

class BankCraftGame : ApplicationAdapter() {
    lateinit var cam: PerspectiveCamera
    lateinit var camController: CameraInputController
    lateinit var modelBatch: ModelBatch
    lateinit var instances: MutableList<ModelInstance>
    lateinit var environment: Environment
    lateinit var model: Model
    lateinit var ground: ModelInstance
    lateinit var ball: ModelInstance
    var collision: Boolean = false
    lateinit var groundShape: btCollisionShape
    lateinit var ballShape: btCollisionShape
    lateinit var groundObject: btCollisionObject
    lateinit var ballObject: btCollisionObject
    lateinit var collisionConfig: btCollisionConfiguration
    lateinit var dispatcher: btDispatcher

    override fun create() {
        Bullet.init();

        collisionConfig = btDefaultCollisionConfiguration()
        dispatcher = btCollisionDispatcher(collisionConfig)

        modelBatch = ModelBatch()

        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))

        cam = PerspectiveCamera(67F, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.position.set(3f, 7f, 10f)
        cam.lookAt(0F, 4f, 0F)
        cam.update()

        camController = CameraInputController(cam)

        val mb = ModelBuilder()
        mb.begin()
        mb.node().id = "ground"
        mb.part("box", GL20.GL_TRIANGLES, (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong(), Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(5f, 1f, 5f)
        mb.node().id = "ball"
        mb.part("sphere", GL20.GL_TRIANGLES, (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong(), Material(ColorAttribute.createDiffuse(Color.GREEN)))
                .sphere(1f, 1f, 1f, 10, 10)
        model = mb.end()

        ground = ModelInstance(model, "ground")
        ball = ModelInstance(model, "ball")
        ball.transform.setToTranslation(0f, 9f, 0f)

        ballShape = btSphereShape(0.5f)
        groundShape = btBoxShape(Vector3(2.5f, 0.5f, 2.5f))

        groundObject = btCollisionObject()
        groundObject.collisionShape = groundShape
        groundObject.worldTransform = ground.transform

        ballObject = btCollisionObject()
        ballObject.collisionShape = ballShape
        ballObject.worldTransform = ball.transform

        instances = arrayListOf()
        instances.add(ground)
        instances.add(ball)
    }

    fun checkCollision(): Boolean {
        val co0 = CollisionObjectWrapper(ballObject)
        val co1 = CollisionObjectWrapper(groundObject)

        val ci = btCollisionAlgorithmConstructionInfo()
        ci.dispatcher1 = dispatcher
        val algorithm = btSphereBoxCollisionAlgorithm(null, ci, co0.wrapper, co1.wrapper, false)

        val info = btDispatcherInfo()
        val result = btManifoldResult(co0.wrapper, co1.wrapper)

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result)

        val r = result.persistentManifold.numContacts > 0

        result.dispose()
        info.dispose()
        algorithm.dispose()
        ci.dispose()
        co1.dispose()
        co0.dispose()

        return r
    }

    override fun render() {
        val delta = Math.min(1f/30f, Gdx.graphics.deltaTime);

        if (!collision) {
            ball.transform.translate(0f, -delta, 0f);
            ballObject.worldTransform = ball.transform;

            collision = checkCollision();
        }

        camController.update()

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch.begin(cam)
        modelBatch.render(instances, environment)
        modelBatch.end()
    }

    override fun dispose() {
        groundObject.dispose();
        groundShape.dispose();

        ballObject.dispose();
        ballShape.dispose();

        dispatcher.dispose();
        collisionConfig.dispose();

        modelBatch.dispose();
        model.dispose();
    }

    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}
}

data class Coin(val id: Int, val x: Int, val y: Int)