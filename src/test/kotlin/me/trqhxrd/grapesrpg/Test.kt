package me.trqhxrd.grapesrpg

import be.seeseemelk.mockbukkit.MockBukkit
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

open class Test {
    @BeforeEach
    fun setup() {
        MockBukkit.mock();
        MockBukkit.load(Main::class.java)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }
}
