![image](https://storage.googleapis.com/skalable.appspot.com/logo.png)

[![Kotlin JS IR supported](https://img.shields.io/badge/Kotlin%2FJS-IR%20supported-yellow)](https://kotl.in/jsirsupported)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.txt)

# Global Applying Kotlin Styled CSS

Here at sKalable we love all things Kotlin. We use it for working on the FrontEnd, BackEnd and everywhere in between.

We are particularly excited about making code as clean and easy to use as possible within the Kotlin ecosystem. Enabling the formation of
an environment that encourages engineers to develop fast, all while applying effective and understandable patterns. We are very passionate about that @sKalable.

Today we will be covering Global Styles in CSS in a KotlinJS project.

_To begin we need to see what the outlook of what we would like to achieve feels like._

![](https://storage.googleapis.com/skalable.appspot.com/Kotlin%20JS%20Global%20Styles/Global%20CSS%20Overview_Global%20CSS%20KotlinJS.png)

When looking at the above diagram it shows that we are looking to apply the `Img CSS` to every image living in the body of the project. 

## Getting a base Setup

Sometimes the best place to start is with the basics. For this example we will create a base component of just an `img` within our document.

```kotlin
fun main() {
    window.onload = {
        render(document.getElementById("root")) {
            /**
             * Load our image in the body without any CSS applied directly
             */
            img(
                alt = "Skalable Logo",
                src = "YOUR IMAGE URL"
            ) {}
        }
    }
}
```
Running the above the following is rendered. 

![](https://storage.googleapis.com/skalable.appspot.com/Kotlin%20JS%20Global%20Styles/Global%20CSS-01%20.png)

A little on the large side to say the least. We can see it takes up the entirety of the main page. While this might be what you're looking for in some cases it definitely shouldn't be the default. 

_So how do we address this for all images?_

## Going Global

Applying global styles can have a huge range of benefits for your project. You can decide default styles and fonts for your basic tags such as `p` or `h1` `h2` etc.

To add a global style for your project there are a few approaches. What we tend to prefer @sKalable is [SRP](https://en.wikipedia.org/wiki/Single-responsibility_principle) - _"A class should have only one reason to change"_

The message here is to group your logic into separate areas of concern. To achieve this with styles we can create a `GlobalStyles` object that manages all our global styles in the project. 

```kotlin
object GlobalStyles : StyleSheet("GlobalStyles") {}
```

While currently bare, we need to look to the ever brilliant [Kotlin-Styled](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-styled) library for KotlinJS.

**Note:** _at the time of writing the global style approach is non-functional with the listed approach within the README.md_

Populating our stylesheet with our global style is easy as [`3.14`](https://en.wikipedia.org/wiki/Pi). 

We need to begin by declaring our initial style for the body of our page.

```kotlin
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
             * We can visualise this much better if we give the background a grey colour.
             */
            backgroundColor = Color.darkGray
        }
    }
}
```
This won't apply the style _just yet_, first we need to inject it into the [Kotlin-Styled](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-styled) global setter. Without causing confusion and to abide by [SRP](https://en.wikipedia.org/wiki/Single-responsibility_principle) we can create the handler function for this directly in the stylesheet. Doing so will allow for all the styles that are in the `GlobalStyles` to remain private. 

```kotlin
//Inside GlobalStyles at the bottom 

    /**
     * By creating a wrapper function in our Global Styles, it gives
     * the global style sheet the capability to have the styles set as private.
     */
    fun applyGlobalStyle() {
        styled.injectGlobal(styles.toString())
    }
```

So when do we use this new `applyGlobalStyle` function? Directly in our `client` class is where! 

```kotlin
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
                src = "YOUR IMAGE URL"
            ) {}
        }
    }
}
```

Applying and running the above gives us the following result.

![](https://storage.googleapis.com/skalable.appspot.com/Kotlin%20JS%20Global%20Styles/Global%20CSS-02.png)

I know, yuck. What the hell is going on here. There is one huge positive in all of this, the style was _applied_. We haven't done anything with the image in particular in all of this. Time to get specific. 

## Applying Global CSS to individual components

Back in our `GlobalStyles` object we are going to add an `img` tag directly in the `body` and apply some `css` to it.

```kotlin
body {
    ... // CSS for Body Tag

    /**
     * When you want to apply a base CSS style to components within the
     * body its as simple as nesting that component within and setting
     * the CSS that you like.
     */
    img {
        maxWidth = 500.px
    }
}
```

Now where getting somewhere. We have applied the image css to all `img` tags within our project. Awesome! 

![](https://storage.googleapis.com/skalable.appspot.com/Kotlin%20JS%20Global%20Styles/Global%20CSS-03.png)

## Optimise for scalability

Every web engineer knows how important it is to keep you're `css` organised and clean. The best way to apply this philosophy is by _starting_ with it. Since we have everything working correctly lets abstract the individual `css` for each component to keep readability high and keep things `sKalable` _(see what I did there?)_


Back in our `GlobalStyles` we are going to abstract the image style into its own `RuleSet`. After we will apply it back into the `img` tag within the `body`.

```kotlin
// Inside GlobalStyles below styles val.

    /**
     * Im order to not overwhelm your fellow engineers, we can
     * extract styles into separate CSS components.
     *
     * This is particularly useful when you have quite a few different styles.
     */
    private val baseImg by css {
        maxWidth = 500.px
    }
```

Now that we've abstracted the `img` css we can add it to the `body` with a simple `+` operator also known as [UnaryPlus](https://kotlinlang.org/docs/operator-overloading.html#unary-prefix-operators).  

```kotlin
body {
    ... // CSS for Body Tag

    /**
     * When you want to apply a base CSS style to components within the
     * body its as simple as nesting that component within and setting
     * the CSS that you like.
     */
    img {
        +baseImg
    }
}
```

There we are, a global stylesheet that will grow with your projects needs.

Feel free to comment or get in touch. @sKalable we are a Kotlin agency that builds code the way it should be built to ensure it is _Maintainable_, _Customisable_, _Flexible_ and `sKalable` _(I did it again.)_ 

Follow us on [Twitter](https://twitter.com/skalable_dev) or [Dev.to](https://dev.to/skalabledev) to get the latest on Kotlin Multi Platform for your businesses needs. 