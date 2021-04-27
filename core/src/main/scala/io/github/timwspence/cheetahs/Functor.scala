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

import shapeless3.deriving.K1

trait Functor[F[_]]:
  extension [A](fa: F[A])
    def map[B](f: A => B): F[B]

object Functor:
  inline def apply[F[_]](using F: Functor[F]): F.type = F

  given Functor[List] with
    extension [A](fa: List[A])
      def map[B](f: A => B): List[B] =
        fa.map(f)

  given Functor[Option] with
    extension [A](fa: Option[A])
      def map[B](f: A => B): Option[B] =
        fa.map(f)

  given Functor[Id] with
    extension [A](fa: Id[A])
      def map[B](f: A => B): Id[B] = f(fa)

  given [X]: Functor[Const[X]] with
    extension [A](fa: Const[X][A])
      def map[B](f: A => B): Const[X][B] = fa

  given functorGen[F[_]](using inst: => K1.Instances[Functor, F]): Functor[F] with
    extension [A](fa: F[A])
      def map[B](f: A => B): F[B] =
        K1.map[Functor, F](inst)(fa : F[A])(
          [t[_]] => (func: Functor[t], t0: t[A]) => func.map(t0)(f)
        )

  inline def derived[F[_]](using gen: K1.Generic[F]): Functor[F] = functorGen[F]
