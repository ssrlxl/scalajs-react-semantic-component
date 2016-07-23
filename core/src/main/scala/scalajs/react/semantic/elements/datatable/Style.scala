package scalajs.react.semantic.elements.datatable

/**
  * Created by shaoshengrong on 16/7/22.
  */

import scalacss.ScalaCssReact._
import scalacss.Defaults._


/*
loat: left;
    width: 20px;
    height: 100%;
    cursor: pointer;
    margin: 0;
 */
object MyStyle extends StyleSheet.Inline {
  import  dsl._
  val dataTableNone = style(
    float.left,
      width(20 px),
    height(14 px),
    cursor.pointer,
    margin(0 px)
  )

  val dataTableCell = style(
    fontSize(12 px),
    height(39 px),
    lineHeight(32 px)

  )
}

object RegisterStyle {
  def register(): Unit ={
    MyStyle.addToDocument()
  }

}
