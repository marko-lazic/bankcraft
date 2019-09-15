package net.bankcraft

import com.badlogic.gdx.math.Vector2
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ExtensionsKtTest {

    @Test
    fun infixVector2Test() {
        // given x = 8 and y = 9 are integers

        // when
        val infixValue = 8 v2 9 // they are converted to float with v2

        // then
        assertEquals(infixValue, Vector2(8F, 9F))
    }
}