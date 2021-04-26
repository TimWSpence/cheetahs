/*
 * Copyright 2020 TimWSpence
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.timwspence.cheetahs

import shapeless3.deriving.*

trait Show[A] {

  extension (a: A)
    def show: String
}

object Show:
  inline def apply[A](using A: Show[A]): A.type = A

  given Show[Unit] with
    extension (u: Unit)
      def show = "()"

  given Show[String] with
    extension (s: String)
      def show = s

  given Show[Int] with
    extension (i: Int)
      def show = i.toString

  given Show[Boolean] with
    extension (b: Boolean)
      def show = b.toString

  given productShow[A](using inst : => K0.ProductInstances[Show, A], l: Labelling[A]): Show[A] with
    extension (a: A)
      def show = {
        val elems = inst.foldLeft[List[String]](a)(Nil: List[String])(
              [t] => (acc: List[String], sh: Show[t], t0: t) => Continue(sh.show(t0) :: acc)
            ).reverse
        s"${l.label}(${l.elemLabels.zip(elems).map((name, value) => s"$name=$value").mkString(",")})"
      }

  given coproductShow[A](using inst: => K0.CoproductInstances[Show, A]): Show[A] with
    extension(a: A)
      def show = inst.fold[String](a)(
        [t] => (sh: Show[t], t0: t) => sh.show(t0)
      )

  inline def derived[A](using gen: K0.Generic[A]): Show[A] = gen.derive(productShow, coproductShow)
