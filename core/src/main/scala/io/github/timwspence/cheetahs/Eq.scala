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

import scala.deriving.Mirror
import shapeless3.deriving.*

trait Eq[A]:

  def eqv(l: A, r: A): Boolean

  def neqv(x: A, y: A): Boolean = !eqv(x, y)

  extension (x: A)
    infix def ===(o: A) = eqv(x, o)

    infix def =!=(o: A) = neqv(x, o)

object Eq:

  def apply[A](using Eq: Eq[A]): Eq.type = Eq

  given Eq[Int] with
    def eqv(l: Int, r: Int): Boolean = l == r

  given Eq[Boolean] with
    def eqv(l: Boolean, r: Boolean): Boolean = l == r

  given Eq[String] with
    def eqv(l: String, r: String): Boolean = l == r

  given eqGen[A](using inst: K0.ProductInstances[Eq, A]): Eq[A] with
    def eqv(l: A, r: A): Boolean = inst.foldLeft2(l,r)(true: Boolean)(
      [t] => (acc: Boolean, eq: Eq[t], t0: t, t1: t) => Complete(!eq.eqv(t0, t1))(false)(true)
    )

  given eqGenC[A](using inst: K0.CoproductInstances[Eq, A]): Eq[A] with
    def eqv(l: A, r: A): Boolean = inst.fold2(l, r)(false)(
      [t] => (eq: Eq[t], t0: t, t1: t) => eq.eqv(t0, t1)
    )

  inline def derived[A](using gen: K0.Generic[A]): Eq[A] =
    gen.derive(eqGen, eqGenC)
