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

// import Coerce.*
// import data.*

// case class Product(x: String, y: Boolean) derives Eq

// enum Coproduct derives Eq:
//   case Left(x: String)
//   case Right(y: Boolean)

// trait Test[A]:
//   def foo: String

// object Test:
//   trait TestImpl[A] extends Test[A]:
//     def foo = "foo"

//   given [A]: Test[A] = new TestImpl[A] {}

sealed trait Maybe[A] derives Show, Functor
case class Just[A](value: A) extends Maybe[A]
case class Nufin[A]() extends Maybe[A]
