import kotlinx.browser.document
import kotlinx.browser.window
import react.dom.img
import react.dom.render
import styles.GlobalStyles


fun main() {
    /**
     * Using our global styles object we
     * can call our wrapper function to apply
     * the global styles to our project.
     */
    GlobalStyles.applyGlobalStyle()
    window.onload = {
        render(document.getElementById("root")) {
            /**
             * Load our image in the body without any CSS applied directly
             */
            img(
                alt = "Skalable Logo",
                src = "https://storage.googleapis.com/skalable.appspot.com/Kotlin%20JS%20Global%20Styles/SkalableDev_SkalableDev.png"
            ) {}
        }
    }
}