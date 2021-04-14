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
import scala.deriving.Mirror

trait Order[A] extends Eq[A]:
  def compare(l: A, r: A): Int

object Order:
  def apply[A](using Order: Order[A]): Order.type = Order

  given Order[Boolean] with
    import Eq.{given_Eq_Boolean as delegate}

    def eqv(l: Boolean, r: Boolean): Boolean = delegate.eqv(l, r)

    def compare(l: Boolean, r: Boolean): Int =
      if (l == r) 0 else if (l) 1 else -1

  given Order[Int] with
    import Eq.{given_Eq_Int as delegate}

    def eqv(l: Int, r: Int): Boolean = delegate.eqv(l, r)

    def compare(l: Int, r: Int): Int = l.compare(r)

  given Order[String] with
    import Eq.{given_Eq_String as delegate}

    def eqv(l: String, r: String): Boolean = delegate.eqv(l, r)

    def compare(l: String, r: String): Int = l.compare(r)

  given orderGen[A](using eqInt: K0.ProductInstances[Eq, A], inst: K0.ProductInstances[Order, A]): Order[A] with
    val delegate = Eq.eqGen[A]

    def eqv(l: A, r: A): Boolean = delegate.eqv(l, r)

    def compare(l: A, r: A): Int = inst.foldLeft2(l, r)(0: Int)(
      [t] => (acc: Int, o: Order[t], t0: t, t1: t) => {
        val cmp: Int = o.compare(t0, t1)
        Complete(cmp != 0)(cmp)(acc)
      }
    )

  given orderGenC[A](using eqInt: K0.CoproductInstances[Eq, A], inst: K0.CoproductInstances[Order, A]): Order[A] with
    val delegate = Eq.eqGenC[A]

    def eqv(l: A, r: A): Boolean = delegate.eqv(l, r)

    def compare(l: A, r: A): Int = inst.fold2(l, r)(0: Int)(
      [t] => (o: Order[t], t0: t, t1: t) => o.compare(t0, t1)
    )

  inline def derived[A](using gen: K0.Generic[A]): Eq[A] =
    gen.derive(orderGen, orderGenC)
