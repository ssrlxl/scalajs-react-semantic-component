package tree

/**
  * Created by shaoshengrong on 16/7/23.
  */

import japgolly.scalajs.react.vdom.prefix_<^._

import scalajs.react.semantic.elements.tree._
import japgolly.scalajs.react.{Callback, ReactComponentB, BackendScope}

import org.scalajs.dom

object BasicTree {

  object Style {
    def treeViewDemo = Seq(^.display := "flex")

    def selectedContent = Seq(^.alignSelf := "center", ^.margin := "0 40px")
  }


  val data = TreeItem("root",
    TreeItem("dude1",
      TreeItem("dude1c")),
    TreeItem("dude2"),
    TreeItem("dude3"),
    TreeItem("dude4",
      TreeItem("dude4c",
        TreeItem("dude4cc")))
  )

  case class State(content: String = "")

  class Backend(t: BackendScope[_, _]) {

    def onItemSelect(item: String, parent: String, depth: Int): Callback = {
      val content =
        s"""Selected Item: $item <br>
            |Its Parent : $parent <br>
            |Its depth:  $depth <br>
        """.stripMargin
      Callback(dom.document.getElementById("treeviewcontent").innerHTML = content)
    }

    def render = {
      <.div(


        <.div(Style.treeViewDemo)(
          Tree(
            root = data,
            openByDefault = true,
            onItemSelect = onItemSelect _,
            showSearchBox = true
          ),
          <.strong(^.id := "treeviewcontent", Style.selectedContent)
        )

      )
    }
  }

  val component = ReactComponentB[Unit]("ReactTreeViewDemo")
    .initialState(State())
    .renderBackend[Backend]
    .build()

  def apply() = component
}
