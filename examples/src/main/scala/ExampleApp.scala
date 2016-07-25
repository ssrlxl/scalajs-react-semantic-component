import button.BasicButton
import dataTable.BasicDataTable
import tree.BasicTree
import japgolly.scalajs.react.ReactDOM

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom

import scalaz._
import Scalaz._


import scalajs.react.semantic.elements.datatable.RegisterStyle

/**
  * Created by shaoshengrong on 16/7/20.
  */

@JSExport
object ExampleApp extends JSApp {

  override def main(): Unit = {

    RegisterStyle.register()

    val dataTableDom = dom.document.createElement("div")
    dataTableDom.setAttribute("id", "basicDataTable")
    dom.document.body.appendChild(dataTableDom)

    AppCss.load()
    //ReactDOM.render(BasicTree(), dom.document.getElementById("basicDataTable"))
    ReactDOM.render(BasicDataTable(), dom.document.getElementById("button"))
  }

}
