package scalajs.react.semantic.elements.datatable

import japgolly.scalajs.react.{React, ReactDOM, ReactComponentB, ReactElement, Callback, ReactEventI}
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.collection.mutable.Map
import scala.collection.mutable.Seq

import scalacss.ScalaCssReact._
import scalacss.Defaults._

import scala.scalajs.js.Date

import scalaz._

import org.scalajs.dom


/**
  * Created by shaoshengrong on 16/7/21.
  */

object DataTable {

  //header config
  /*
      1. 支持
   */
  private var header = List[String]()

  case class HeaderConfig(
                           id: String,
                           header: String,
                           sort: String,
                           width: Int
                         )

  case class Props(
                    header: Array[HeaderConfig],
                    data: Tree[Map[String, String]]
                  )


  def genHeader(headers: Array[HeaderConfig]): ReactElement = {
    val columns = headers.map { cfgs =>
      this.header = this.header :+ cfgs.header
      <.th(
        cfgs.header
      )
    }

    <.thead(
      <.tr(
        columns: _*
      )
    )
  }

  def genId() = {
    Date.now().toString
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

  def addClick(dataTree: Tree[Map[String, String]])(e: ReactEventI): Callback = {
    Callback {
      println("click Id : ", e.target.id)
      val addIcon = "add"
      val minusIcon = "minus"

      if (e.target.className == s"$addIcon square icon") {
        e.target.className = s"$minusIcon square icon"

        val childLocs = this.getChild(dataTree.loc, 1)

        childLocs.foreach {
          loc =>
            this.addRow(loc.tree, 1)
        }


      } else {
        e.target.className = s"$addIcon square icon"
      }
    }
  }

  def addRow(dataTree: Tree[Map[String, String]], index: Int) = {
    val data = dataTree.loc.getLabel
    var iconName = "minus"
    if (dataTree.loc.hasChildren) {
      iconName = "add"
    }
    val rowDom = dom.document.createElement("tr")

    this.header.foreach {
      name =>
        var id = this.genId()
        data("id") = id
        dataTree.loc.setLabel(data)
        val colDom = dom.document.createElement("td")

        if (name == "name") {
          colDom.innerHTML = data.getOrElse("name", "")
          val divDom = dom.document.createElement("div")
          divDom.setAttribute("style", "float:left;width:20px;height:15px;cursor:pointer;margin:0px;")
          (0 to index).foreach { i =>
            colDom.appendChild(divDom)
            println(i)
          }


          if (dataTree.loc.hasChildren) {
            val spanDom = dom.document.createElement("span")
            val iDom = dom.document.createElement("i")
            iDom.setAttribute("class", s"$iconName square icon")
            iDom.setAttribute("style", "float:left;")

            def onButtonPressed: Callback =
              Callback.alert("The button was pressed!")




            iDom.setAttribute("id", id)
            //spanDom.innerHTML = data.getOrElse("name", "")
            spanDom.appendChild(iDom)
            colDom.appendChild(spanDom)
          }
        } else {
          colDom.innerHTML = data.getOrElse(name, "")
        }
        rowDom.appendChild(colDom)
        println(name)
    }

    dom.document.getElementById("bodyId").appendChild(rowDom)
  }


  def genRow(dataTree: Tree[Map[String, String]]) = {
    val data = dataTree.loc.getLabel
    var iconName = "minus"
    if (dataTree.loc.hasChildren) {
      iconName = "add"
    }
    val columnDoms = this.header.map {
      header =>
        var id = this.genId()
        data("id") = id
        dataTree.loc.setLabel(data)
        if (header == "name") {
          val tdTags = Array(<.span(
            <.i(
              ^.cls := s"$iconName square icon",
              ^.onClick ==> this.addClick(dataTree),
              ^.id := id
            ), data.getOrElse("name", "").toString))

          <.td(
            tdTags: _*
          )

        } else {
          <.td(
            data.getOrElse(header, "").toString
          )
        }
    }.toArray
    <.tr(
      columnDoms: _*
    )
  }

  def genBody(dataTree: Tree[Map[String, String]]): ReactElement = {
    val rowDom = this.genRow(dataTree)
    <.tbody(
      ^.id := "bodyId",
      rowDom
    )
  }

  def component = ReactComponentB[Props]("DataTable")
    .renderP((_, cfgs) =>
      <.table(
        ^.cls := "ui celled  selectable table ",
        this.genHeader(cfgs.header),
        this.genBody(cfgs.data)
      )
    ).build

  def apply(p: Props) = component(p)
}
