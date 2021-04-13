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

/**
 * Typeclass witnessing the safety of casts
 */
trait Coerce[From, To]:
  inline def coerce(from: From): To = from.asInstanceOf[To]

object Coerce extends CoerceInstances:
  def apply[From, To](using Coerce: Coerce[From, To]): Coerce.type = Coerce

  extension [From](x: From)
    inline def coerce[To](using Coerce: Coerce[From, To]): To =
      x.asInstanceOf[To]

  /**
   * Relation is symmetric
   */
  given[A, B](using Coerce[A, B]): Coerce[B, A] with {}

  /**
   * Relation is preserved by functors
   *
   * Equivalent to `Functor[F].map(fa)(coerce(_))` but O(1)
   */
  given [F[_], A, B](using Coerce[A, B], Functor[F]): Coerce[F[A], F[B]] with {}

trait CoerceInstances extends CoerceInstances1:
  /**
   * Relation is transitive
   */
  given [A, B, C](using Coerce[A, B], Coerce[B, C]): Coerce[A, C] with {}

trait CoerceInstances1:

  /**
   * Relation is reflexive
   */
  given [A]: Coerce[A, A] with {}
