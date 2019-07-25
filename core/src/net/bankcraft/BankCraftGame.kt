package net.bankcraft

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

class BankCraftGame : ApplicationAdapter() {
    internal lateinit var batch: SpriteBatch
    internal lateinit var img: Texture
    internal lateinit var shapeRenderer: ShapeRenderer

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        shapeRenderer = ShapeRenderer()
    }
    private val position: Vector2 = Vector2(300f, 220f)
    private var time: Float = 0f

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(img, 0f, 0f)
        batch.end()
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
