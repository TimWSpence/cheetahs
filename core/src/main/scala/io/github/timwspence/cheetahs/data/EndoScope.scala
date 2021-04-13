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
package data

object EndoScope:
  opaque type Endo[A] = A => A

  object Endo:

    extension [A](endo: Endo[A])
      def apply(a: A): A = endo.apply(a)

      def andThen(o: Endo[A]): Endo[A] = endo.andThen(o)

      def compose(o: Endo[A]): Endo[A] = endo.compose(o)

    given [A]: Coerce[A => A, Endo[A]] with {}

    given [A]: Monoid[Endo[A]] with

      def empty: Endo[A] = identity

      def combine(l: Endo[A], r: Endo[A]): Endo[A] = l.andThen(r)
