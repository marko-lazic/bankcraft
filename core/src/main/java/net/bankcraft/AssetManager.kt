package net.bankcraft

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.utils.Disposable

object AssetManager : Disposable {
    var manager: AssetManager = AssetManager()
        private set


    fun loadSplashAssets() {
        manager.load(Resources.SPLASH_IMAGE_PATH, Texture::class.java)
        manager.finishLoading()
    }

    fun loadAssets() {
//        manager.load(Resources.BACKGROUND_MUSIC, Music::class.java)
//        manager.load(Resources.SOUND_BLOP, Sound::class.java)
//        manager.load(Resources.SOUND_BUZZ, Sound::class.java)
//        manager.load(Resources.SPRITES_ATLAS_PATH, TextureAtlas::class.java)
//        manager.load(Resources.FONT_PATH, BitmapFont::class.java)
//        manager.load(Resources.GAME_OVER_FONT_PATH, BitmapFont::class.java)
//        manager.load(Resources.GAME_OVER_SCORE_FONT_PATH, BitmapFont::class.java)
//        manager.finishLoading()
//        loadAtlas()
    }

    fun loadModels() {
        manager.load(Resources.ENERGY_NODE, Model::class.java)
        manager.load(Resources.SHIP, Model::class.java)
        manager.finishLoading()
    }

    override fun dispose() {
        manager.dispose()
    }
}