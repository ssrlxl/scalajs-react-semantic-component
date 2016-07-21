package button

/**
  * Created by shaoshengrong on 16/7/20.
  */
import japgolly.scalajs.react.vdom.prefix_<^._

import scalajs.react.semantic.elements.button._
import japgolly.scalajs.react.{Callback, ReactComponentB}


object BasicButton {

  def component = ReactComponentB[Unit]("BasicButton")
    .render(_=>
      <.div(
        Button(
          Button.Props(animated = Button.Animated, tabIndex = Some(0)),
          <.div(
            ^.cls := "visible content",
            "Next"
          ),
          <.div(
            ^.cls := "hidden content",
            "hello"
          )
        )
      )
    )
    .build()

  def apply() = component
}
