package scalajs.react.semantic.elements.tree

/**
  * Created by shaoshengrong on 16/7/23.
  */

import scalaz._

import scalajs.react.semantic.elements.search._

import japgolly.scalajs.react.CompScope._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js

import scala.collection.mutable.Map


object TreeDataTable {


  trait Style {

    def reactTreeView = Seq[TagMod]()

    def treeGroup = Seq(^.margin := "0 px", ^.padding := "0 0 0 14 px ")

    def treeItem = Seq(^.listStyleType := "none")

    def selectedTreeItemContent = Seq(^.backgroundColor := "#1B8EB0",
      ^.color := "white", ^.fontWeight := 400,
      ^.padding := "0 7px")

    def treeItemBefore = Seq(
      ^.display := "inline-block",
      ^.fontSize := "11px",
      ^.color := "grey",
      ^.margin := "3px 7px 0 0",
      ^.textAlign := "center",
      ^.width := "11px"
    )

    def treeItemHasChildrenClosed = Seq(^.contentStyle := "▶")

    def treeItemHasChildrenOpened = Seq(^.contentStyle := "▼")

  }


  def getChild(loc: TreeLoc[Map[String, String]], index: Int): Seq[TreeLoc[Map[String, String]]] = {
    println(index)
    val subLoc = loc.getChild(index).get
    subLoc.isLast match {
      case true =>
        Seq(subLoc)
      case false =>
        subLoc +: this.getChild(loc, index + 1)
    }
  }

  type NodeC = DuringCallbackU[NodeProps, NodeState, NodeBackend]

  case class State(filterText: String,
                   filterMode: Boolean,
                   selectedNode: js.UndefOr[NodeC])

  class Backend($: BackendScope[Props, State]) {

    def onNodeSelect(P: Props)(selected: NodeC): Callback = {
      val removeSelection: Callback =
        $.state.flatMap(
          _.selectedNode
            .filterNot(_ == selected)
            .filter(_.isMounted())
            .fold(Callback.empty)(_.modState(_.copy(selected = false)))
        )

      val updateThis: Callback =
        $.modState(_.copy(selectedNode = selected, filterMode = false))

      val setSelection: Callback =
        selected.modState(_.copy(selected = true))


      val tell: Callback =
        P.onItemSelect.asCbo(
          selected.props.root.getLabel.toString,
          selected.props.parent,
          selected.props.depth
        )

      removeSelection >> updateThis >> setSelection >> tell
    }

    def onTextChange(text: String): Callback =
      $.modState(_.copy(filterText = text, filterMode = true))

    def render(P: Props, S: State) =
      <.div(P.style.reactTreeView)(
        P.showSearchBox ?= Search(onTextChange = onTextChange),
        <.table(
          ^.cls := "ui celled  selectable table ",
          <.tbody(
            TreeNode.withKey("root")(NodeProps(
              root = P.root.loc,
              open = if (S.filterText.nonEmpty) true else P.open,
              onNodeSelect = onNodeSelect(P),
              filterText = S.filterText,
              style = P.style,
              filterMode = S.filterMode
            ))
          )
        )
      )
  }

  case class NodeBackend($: BackendScope[NodeProps, NodeState]) {

    def onItemSelect(P: NodeProps)(e: ReactEventH): Callback =
      P.onNodeSelect($.asInstanceOf[NodeC]) >> e.preventDefaultCB >> e.stopPropagationCB

    def childrenFromProps(P: NodeProps): CallbackTo[Option[Unit]] =
      $.modState(S => S.copy(children = if (S.children.isEmpty) getChild(P.root, P.depth) else Nil))
        .when(P.root.hasChildren)

    def onTreeMenuToggle(P: NodeProps)(e: ReactEventH): Callback =
      childrenFromProps(P) >> e.preventDefaultCB >> e.stopPropagationCB

    def isFilterTextExist(filterText: String, data: Tree[Map[String, String]]): Boolean = {
      filterText == filterText
    }

    def getChild(loc: TreeLoc[Map[String, String]], index: Int): List[TreeLoc[Map[String, String]]] = {
      val subLoc = loc.getChild(index).get
      subLoc.isLast match {
        case true =>
          List(subLoc)
        case false =>
          subLoc :: this.getChild(loc, index + 1)
      }
    }


    def render(P: NodeProps, S: NodeState): ReactTag = {
      val depth = P.depth + 1
     // val parent = if (P.parent.isEmpty) P.root.pa.toString
      //else s"${P.parent}<-${P.root.item.toString}"

      val treeMenuToggle: TagMod =
        if (S.children.nonEmpty)
          <.span(
            ^.onClick ==> onTreeMenuToggle(P),
            ^.key := "arrow",
            P.style.treeItemBefore,
            "▼"
          )
        else if (P.root.hasChildren && S.children.isEmpty)
          <.span(
            ^.onClick ==> onTreeMenuToggle(P),
            ^.key := "arrow",
            P.style.treeItemBefore,
            "▶"
          )
        else ""

     val rowDom = S.children.map{
       loc =>
           <.td(
             treeMenuToggle,
             ^.cursor := "pointer",
             <.span(
               S.selected ?= P.style.selectedTreeItemContent,
               ^.onClick ==> onItemSelect(P)
             )
           )
     }
      <.tr(
        rowDom: _*
        /*<.td(P.style.treeGroup)(
          S.children.map(child =>
            isFilterTextExist(P.filterText, child) ?=
              TreeNode.withKey(s"$parent$depth${child.item}")(P.copy(
                root = child,
                open = !P.filterText.trim.isEmpty,
                depth = depth,
                parent = parent,
                filterText = P.filterText
              ))
          ))*/
      )
    }
  }

  case class NodeState(children: Seq[TreeLoc[Map[String, String]]] = null, selected: Boolean = false)

  case class NodeProps(root: TreeLoc[Map[String, String]],
                       open: Boolean,
                       depth: Int = 0,
                       parent: String = "",
                       onNodeSelect: (NodeC) => Callback,
                       filterText: String,
                       style: Style,
                       filterMode: Boolean)

  lazy val TreeNode = ReactComponentB[NodeProps]("ReactTreeNode")
    .initialState_P(P => if (P.open) NodeState(getChild(P.root, 1)) else NodeState())
    .renderBackend[NodeBackend]
    .componentWillReceiveProps {
      case ComponentWillReceiveProps(_$, newProps) =>
        _$.modState(_.copy(children = if (newProps.open) getChild(newProps.root, newProps.depth) else null))
          .when(newProps.filterMode)
          .void
    }
    .build

  val component = ReactComponentB[Props]("ReactTreeView")
    .initialState(State("", false, js.undefined))
    .renderBackend[Backend]
    .build

  case class Props(root: Tree[Map[String, String]],
                   open: Boolean,
                   onItemSelect: js.UndefOr[(String, String, Int) => Callback],
                   showSearchBox: Boolean,
                   style: Style)

  def apply(root: Tree[Map[String, String]],
            openByDefault: Boolean = false,
            onItemSelect: js.UndefOr[(String, String, Int) => Callback] = js.undefined,
            showSearchBox: Boolean = false,
            ref: js.UndefOr[String] = js.undefined,
            key: js.UndefOr[js.Any] = js.undefined,
            style: Style = new Style {}) =
    component.set(key, ref)(Props(root, openByDefault, onItemSelect, showSearchBox, style))

}
