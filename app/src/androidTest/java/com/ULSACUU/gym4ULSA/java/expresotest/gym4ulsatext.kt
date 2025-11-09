package com.ULSACUU.gym4ULSA.java.expresotest

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val PACKAGE_NAME = "com.ULSACUU.gym4ULSA"

@RunWith(AndroidJUnit4::class)
class UiNavigationTest {

    private lateinit var device: UiDevice

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = context.packageManager.getLaunchIntentForPackage(PACKAGE_NAME)!!.apply {
            addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), 5000)
    }

    @Test
    fun loginAndNavigateTabs() {
        // Esperar a que el campo de email aparezca
        device.wait(Until.hasObject(By.textContains("Email")), 3000)

        // Escribir email y password
        device.findObject(By.textContains("Email"))?.text = "usuario@ulsacuu.com"
        device.findObject(By.textContains("Password"))?.text = "123456"

        // Clic en el botón de Login
        device.findObject(By.text("Login"))?.click()

        // Esperar a Home
        device.wait(Until.hasObject(By.textContains("Home")), 5000)

        // Navegar entre pestañas (según tus labels reales)
        device.findObject(By.text("Rutinas"))?.click()
        device.waitForIdle(1000)

        device.findObject(By.text("Perfil"))?.click()
        device.waitForIdle(1000)

        device.findObject(By.text("Configuración"))?.click()
        device.waitForIdle(1000)
    }
}
