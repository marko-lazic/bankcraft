package net.bankcraft

object WorldFactory {
    fun createGround(): GameObject {
        return GameObjectFactory.constructors.get("ground").construct()
    }
}