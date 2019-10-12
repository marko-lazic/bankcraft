package net.bankcraft

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3

val Entity.transform : TransformComponent get() = TransformComponent[this]

class TransformComponent(val position: Vector3) : Component {
    companion object : ComponentResolver<TransformComponent>(TransformComponent::class.java)
}

val Entity.modelInstance: ModelInstanceComponent get() = ModelInstanceComponent[this]

class ModelInstanceComponent(var modelInstance: ModelInstance) : Component {
    companion object : ComponentResolver<ModelInstanceComponent>(ModelInstanceComponent::class.java)
}

val Entity.gameObject: GameObjectComponent get() = GameObjectComponent[this]

class GameObjectComponent(var gameObject: GameObject) : Component {
    companion object : ComponentResolver<GameObjectComponent>(GameObjectComponent::class.java)
}

open class ComponentResolver<T : Component>(componentCLass: Class<T>) {
    var MAPPER = ComponentMapper.getFor(componentCLass)
    operator fun get(entity: Entity) = MAPPER.get(entity)
}

fun <T : Component> Entity.tryGet(componentResolver: ComponentResolver<T>): T? {
    return componentResolver.MAPPER.get(this)
}