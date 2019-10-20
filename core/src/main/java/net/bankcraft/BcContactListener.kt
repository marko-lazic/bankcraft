package net.bankcraft

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.physics.bullet.collision.ContactListener
import com.google.inject.Inject

class BcContactListener @Inject constructor(private val engine: Engine) : ContactListener() {

    override fun onContactAdded(userValue0: Int, partId0: Int, index0: Int, match0: Boolean,
                                userValue1: Int, partId1: Int, index1: Int, match1: Boolean): Boolean {
        val instances = engine.getEntitiesFor(Family.all(ShapeComponent::class.java).exclude(LiftComponent::class.java).get())
        if (match0 or match1) {
            for (instance in instances) {
                if (instance.shape.gameObject.body.userValue == userValue0) {
                    instance.shape.gameObject.apply {
                        (materials.get(0).get(ColorAttribute.Diffuse) as ColorAttribute).color.set(Color.WHITE)
                    }
                }

                if (instance.shape.gameObject.body.userValue == userValue1) {
                    instance.shape.gameObject.apply {
                        (materials.get(0).get(ColorAttribute.Diffuse) as ColorAttribute).color.set(Color.WHITE)
                    }
                }
            }
        }
        return true
    }
}