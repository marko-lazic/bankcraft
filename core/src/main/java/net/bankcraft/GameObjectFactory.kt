package net.bankcraft

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.utils.ArrayMap
import com.badlogic.gdx.utils.Disposable

class GameObjectFactory : Disposable {

    companion object {
        var model: Model = initModel()
        var constructors: ArrayMap<String, GameObject.Constructor> = initConstructors()

        private fun initModel(): Model {
            val mb = ModelBuilder()
            mb.begin()
            mb.node().id = "ground"
            mb.part("ground", GL20.GL_TRIANGLES, (Usage.Position or Usage.Normal).toLong(), Material(ColorAttribute.createDiffuse(Color.RED)))
                    .box(5f, 1f, 5f)
            mb.node().id = "sphere"
            mb.part("sphere", GL20.GL_TRIANGLES, (Usage.Position or Usage.Normal).toLong(), Material(ColorAttribute.createDiffuse(Color.GREEN)))
                    .sphere(1f, 1f, 1f, 10, 10)
            mb.node().id = "box"
            mb.part("box", GL20.GL_TRIANGLES, (Usage.Position or Usage.Normal).toLong(), Material(ColorAttribute.createDiffuse(Color.BLUE)))
                    .box(1f, 1f, 1f)
            mb.node().id = "cone"
            mb.part("cone", GL20.GL_TRIANGLES, (Usage.Position or Usage.Normal).toLong(), Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                    .cone(1f, 2f, 1f, 10)
            mb.node().id = "capsule"
            mb.part("capsule", GL20.GL_TRIANGLES, (Usage.Position or Usage.Normal).toLong(), Material(ColorAttribute.createDiffuse(Color.CYAN)))
                    .capsule(0.5f, 2f, 10)
            mb.node().id = "cylinder"
            mb.part("cylinder", GL20.GL_TRIANGLES, (Usage.Position or Usage.Normal).toLong(),
                    Material(ColorAttribute.createDiffuse(Color.MAGENTA))).cylinder(1f, 2f, 1f, 10)
            return mb.end()
        }

        private fun initConstructors(): ArrayMap<String, GameObject.Constructor> {
            val constructors: ArrayMap<String, GameObject.Constructor> = ArrayMap(String::class.java, GameObject.Constructor::class.java)
            constructors.put("ground", GameObject.Constructor(model, "ground", btBoxShape(Vector3(2.5f, 0.5f, 2.5f)), Mass.Static))
            constructors.put("sphere", GameObject.Constructor(model, "sphere", btSphereShape(0.5f), Mass.Dynamic(1F)))
            constructors.put("box", GameObject.Constructor(model, "box", btBoxShape(Vector3(0.5f, 0.5f, 0.5f)), Mass.Dynamic(1F)))
            constructors.put("cone", GameObject.Constructor(model, "cone", btConeShape(0.5f, 2f), Mass.Dynamic(1F)))
            constructors.put("capsule", GameObject.Constructor(model, "capsule", btCapsuleShape(.5f, 1f), Mass.Dynamic(1F)))
            constructors.put("cylinder", GameObject.Constructor(model, "cylinder", btCylinderShape(Vector3(.5f, 1f, .5f)), Mass.Dynamic(1F)))
            return constructors
        }
    }

    override fun dispose() {
        model.dispose()
        constructors.forEach {
            it.value.dispose()
        }
    }
}
