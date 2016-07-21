package dataTable

/**
  * Created by shaoshengrong on 16/7/21.
  */

import japgolly.scalajs.react.vdom.prefix_<^._

import scalajs.react.semantic.elements.datatable._
import japgolly.scalajs.react.{Callback, ReactComponentB}

import scala.collection.mutable.Map

object BasicDataTable {

  def genTestHeaderConfig(): Array[DataTable.HeaderConfig] = {
    val titleArr = for (i <- 0 to 10) yield "标题" + i
    titleArr.map(i =>
      if (i == "标题0") {
        DataTable.HeaderConfig(
          id = "11",
          header = "name",
          width = 100,
          sort = "asc"
        )
      } else {
        DataTable.HeaderConfig(
          id = "11",
          header = i,
          width = 100,
          sort = "asc"
        )
      }
    ).toArray

  }

  def genTestData(): Array[Map[String, String]] = {
    val doms = for (i <- 0 to 1) yield {
      val cols = Map[String, String]()
      for (i <- 1 to 10) {
        cols("标题" + i) = "11"
      }
      cols("name") = "1"
      cols
    }
    doms.toArray
  }


  def component = ReactComponentB[Unit]("BasicDataTable")
    .render(_ =>
      <.div(
        DataTable(
          DataTable.Props(
            header = this.genTestHeaderConfig,
            data = this.genTestData
          ))
      )
    ).build()

  def apply() = component

}
