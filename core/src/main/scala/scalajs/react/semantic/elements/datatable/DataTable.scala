package scalajs.react.semantic.elements.datatable

import japgolly.scalajs.react.{ReactComponentB, ReactElement, Callback, ReactEventI}
import japgolly.scalajs.react.vdom.prefix_<^._


import scala.collection.mutable.Map
import scala.collection.mutable.Seq

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
                    data: Array[Map[String, String]]
                  )

  /*
  <table class="ui celled table">
  <thead>
    <tr><th>标题</th>
    <th>标题</th>
    <th>标题</th>
  </tr></thead>
  <tbody>
    <tr>
      <td>
        <div class="ui ribbon label">First</div>
      </td>
      <td>Cell</td>
      <td>Cell</td>
    </tr>
    <tr>
      <td>Cell</td>
      <td>Cell</td>
      <td>Cell</td>
    </tr>
    <tr>
      <td>Cell</td>
      <td>Cell</td>
      <td>Cell</td>
    </tr>
  </tbody>
  <tfoot>
    <tr><th colspan="3">
      <div class="ui right floated pagination menu">
        <a class="icon item">
          <i class="left chevron icon"></i>
        </a>
        <a class="item">1</a>
        <a class="item">2</a>
        <a class="item">3</a>
        <a class="item">4</a>
        <a class="icon item">
          <i class="right chevron icon"></i>
        </a>
      </div>
    </th>
  </tr></tfoot>
</table>
   */
  /*
      head
      body
      foot
   */
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

  def addClick(e: ReactEventI): Callback = {

    Callback{
      val addIcon = "add"
      val minusIcon = "minus"
      if(e.target.className == s"$addIcon square icon"){
        e.target.className = s"$minusIcon square icon"
      }else{
        e.target.className = s"$addIcon square icon"
      }

    }

  }


  def genBody(datas: Array[Map[String, String]]): ReactElement = {
    val cellDoms = datas.map { row =>
      val columnDoms = this.header.map {
        header =>
          println(header)
          if (header == "name") {
            <.td(
              <.i(
                ^.cls := "add square icon",
                ^.onClick ==> this.addClick
              ),
              row.getOrElse("name", "").toString
            )
          } else {
            <.td(
              row.getOrElse(header, "").toString
            )
          }
      }.toArray
      <.tr(
        columnDoms: _*
      )
    }

    <.tbody(
      cellDoms: _*
    )

  }

  def component = ReactComponentB[Props]("DataTable")
    .renderP((_, cfgs) =>
      <.table(
        ^.cls := "ui celled table",
        this.genHeader(cfgs.header),
        this.genBody(cfgs.data)
      )
    ).build

  def apply(p: Props) = component(p)
}
