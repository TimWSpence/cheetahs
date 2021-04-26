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

trait Applicative[F[_]] extends Functor[F]:
  def pure[A](a: A): F[A]

  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]

  def liftA2[A, B, C](f: (A, B) => C)(fa: F[A])(fb: F[B]): F[C] =
    ap(map(fa)(a => (b: B) => f(a, b)))(fb)

object Applicative:
  inline def apply[F[_]](using F: Applicative[F]): F.type = F
