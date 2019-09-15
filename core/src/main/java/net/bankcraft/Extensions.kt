package net.bankcraft

import com.badlogic.gdx.math.Vector2

val Int.pixelsToMeters: Float
    get() = this / 32F

operator fun Vector2.component1(): Float = this.x

operator fun Vector2.component2(): Float = this.y

infix fun Number.v2(other: Number): Vector2 {
    return Vector2(this.toFloat(), other.toFloat())
}