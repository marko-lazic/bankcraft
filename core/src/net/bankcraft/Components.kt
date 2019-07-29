package net.bankcraft

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

val Entity.transform : TransformComponent get() = TransformComponent[this]

class TransformComponent(val position: Vector2) : Component {
    companion object : ComponentResolver<TransformComponent>(TransformComponent::class.java)
}

val Entity.texture : TextureComponent get() = TextureComponent[this]

class TextureComponent(val texture: Texture) : Component {
    companion object : ComponentResolver<TextureComponent>(TextureComponent::class.java)
}