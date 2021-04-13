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

trait Eq[A]:

  def eqv(l: A, r: A): Boolean

  def neqv(x: A, y: A): Boolean = !eqv(x, y)

object Eq:

  def apply[A](using Eq: Eq[A]): Eq.type = Eq

  given Eq[Int] with
    def eqv(l: Int, r: Int): Boolean = l == r

  given Eq[Boolean] with
    def eqv(l: Boolean, r: Boolean): Boolean = l == r

  given Eq[String] with
    def eqv(l: String, r: String): Boolean = l == r

  given Eq[EmptyTuple] with
    def eqv(l: EmptyTuple, r: EmptyTuple): Boolean = true

  given [H, T <: Tuple](using H: Eq[H], T: Eq[T]): Eq[H *: T] with
    def eqv(l: H *: T, r: H *: T): Boolean = H.eqv(l.head, r.head) && T.eqv(l.tail, r.tail)

  def derived[A](using M: Mirror.Of[A]): Eq[A] = ???
