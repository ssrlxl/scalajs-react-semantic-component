package scalajs.react.semantic.elements.button

/**
  * Created by shaoshengrong on 16/7/20.
  */

import japgolly.scalajs.react.{Callback, ReactComponentB, ReactNode}
import japgolly.scalajs.react.vdom.prefix_<^._




object Button {

  sealed trait ButtonState

  case object Active extends ButtonState

  case object InActive extends ButtonState

  sealed trait Emphasis

  case object NoEmphasis extends Emphasis

  case object Primary extends Emphasis

  case object Secondary extends Emphasis

  sealed trait Animated

  case object NotAnimated extends Animated

  case object Animated extends Animated

  case object Vertical extends Animated

  case object Fade extends Animated

  case class Props(state: ButtonState = InActive,
                   emphasis: Emphasis = NoEmphasis,
                   animated: Animated = NotAnimated,
                   icon: Boolean = false,
                   basic: Boolean = false,
                   inverted: Boolean = false,
                   tabIndex: Option[Int] = None,
                   color: Option[String] = None,
                   onClick: Callback = Callback.empty)

  def classSet(p: Props) =
    ^.classSet(
      "active" -> (p.state == Active),
      "primary" -> (p.emphasis == Primary),
      "secondary" -> (p.emphasis == Secondary),
      "animated" -> (p.animated != NotAnimated),
      "vertical" -> (p.animated == Vertical),
      "fade" -> (p.animated == Fade),
      "icon" -> p.icon,
      "basic" -> p.basic,
      "inverted" -> p.inverted
    )

  def component = ReactComponentB[Props]("Button")
    .renderPC((_, p, c) =>
      if (p.animated == NotAnimated)
        <.button(
          ^.cls := "ui button",
          p.color.map(u => ^.cls := u),
          ^.tabIndex := p.tabIndex,
          classSet(p),
          ^.onClick --> p.onClick,
          c
        )
      else {
        <.div(
          ^.cls := "ui button",
          ^.tabIndex := p.tabIndex,
          classSet(p),
          ^.onClick --> p.onClick,
          c
        )
      }
    ).build

  def apply(p: Props, children: ReactNode*) = component(p, children: _*)

  def apply(text: String) = component(Props(), text)
}


