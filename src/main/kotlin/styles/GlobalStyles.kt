package styles

import kotlinx.css.*
import styled.StyleSheet

object GlobalStyles : StyleSheet("GlobalStyles") {

    private val styles = CSSBuilder().apply {
        body {
            /**
             * Gives the body the following css globally (there is only ever one body)
             *
             *  - Sets the maxWidth with to the size available for the content.
             *  - Adds Auto Margin to the body allowing everything to be centered.
             *  - Creates a padding of 100px all around the body
             */
            maxWidth = LinearDimension.maxContent
            margin(LinearDimension.auto)
            padding(100.px)
            /**
             * So we can visualise this much better we give the background a grey colour.
             */
            backgroundColor = Color.darkGray

            /**
             * When you want to apply a base CSS style to components within the
             * body its as simple as nesting that component within and setting
             * the CSS that you like.
             */
            img {
                +baseImg
            }
        }
    }

    /**
     * Im order to not overwhelm your fellow engineers, we can
     * extract styles into separate CSS components.
     *
     * This is particularly useful when you have quite a few different styles.
     */
    private val baseImg by css {
        maxWidth = 500.px
    }

    /**
     * By creating a wrapper function in our Global Styles, it gives
     * the global style sheet the capability to have the styles set as private.
     */
    fun applyGlobalStyle() {
        styled.injectGlobal(styles.toString())
    }

}