import button.BasicButton
import dataTable.BasicDataTable
import japgolly.scalajs.react.ReactDOM
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom

import scalaz._
import Scalaz._

/**
  * Created by shaoshengrong on 16/7/20.
  */

@JSExport
object ExampleApp extends JSApp {

  override def main(): Unit = {
    val tree: Tree[Int] =
      1.node(
        11.leaf
      )

    val l = for {
      l1 <- tree.loc.some
      l2 <- l1.firstChild
    } yield (l1, l2)
    var index = 0



    Seq(l.get._1.getLabel, l.get._2.getLabel).foreach { e =>
      println(e)
      val child = dom.document.createElement("div")
      child.setAttribute("id", "button" + index)
      dom.document.body.appendChild(child)
      ReactDOM.render(BasicButton(), dom.document.getElementById("button" + index))
      index += 1
    }

    val childDom = dom.document.createElement("p")
    childDom.innerHTML = "sssssxxxx"
    childDom.setAttribute("style", "color:red;")

    println(tree.drawTree)

    val dataTableDom = dom.document.createElement("div")
    dataTableDom.setAttribute("id", "basicDataTable")
    dom.document.body.appendChild(dataTableDom)

    ReactDOM.render(BasicDataTable(), dom.document.getElementById("basicDataTable"))
  }

}
