package scalajs.react.semantic.elements

/**
  * Created by shaoshengrong on 16/7/23.
  */

import japgolly.scalajs.react._

import scala.reflect.ClassTag
import scala.scalajs.js
import scala.scalajs.js.`|`

package object tree {

  type CssProperties = js.Any

  /* this works here, but not in the general case!
   * (see https://github.com/scala-js/scala-js/pull/2070 )
   */
  @deprecated("We need to find a better solution here")
  private[tree] implicit def UnionEvidence[A: ClassTag, B: ClassTag](ab: A | B)(implicit eva: A => js.Any, evb: B => js.Any): js.Any =
    ab match {
      case a: A => eva(a)
      case b: B => evb(b)
    }

  private[tree] implicit final class UCB[R](private val uc: js.UndefOr[CallbackTo[R]]) extends AnyVal {
    @inline def asCbo: CallbackOption[R] =
      CallbackOption.liftOption(uc.toOption.map(_.runNow()))
  }

  private[tree] implicit final class UF1CB[T1, R](private val uc: js.UndefOr[T1 => CallbackTo[R]]) extends AnyVal {
    @inline def asCbo(t1: T1): CallbackOption[R] =
      CallbackOption.liftOptionLike(uc).flatMap(_.apply(t1).toCBO)
  }

  private[tree] implicit final class UF2CB[T1, T2, R](private val uc: js.UndefOr[(T1, T2) => CallbackTo[R]]) extends AnyVal {
    @inline def asCbo(t1: T1, t2: T2): CallbackOption[R] =
      CallbackOption.liftOptionLike(uc).flatMap(_.apply(t1, t2).toCBO)
  }

  private[tree] implicit final class UF3CB[T1, T2, T3, R](private val uc: js.UndefOr[(T1, T2, T3) => CallbackTo[R]]) extends AnyVal {
    @inline def asCbo(t1: T1, t2: T2, t3: T3): CallbackOption[R] =
      CallbackOption.liftOptionLike(uc).flatMap(_.apply(t1, t2, t3).toCBO)
  }

}
