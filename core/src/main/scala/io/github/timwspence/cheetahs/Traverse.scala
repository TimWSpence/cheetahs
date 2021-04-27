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

import shapeless3.deriving.{K1, Continue}

trait Traverse[F[_]] extends Functor[F]:
  extension [A](fa: F[A])
    def traverse[G[_]: Applicative, B](f: A => G[B]): G[F[B]]

object Traverse:
  inline def apply[F[_]](using F: Traverse[F]): F.type = F

  given Traverse[List] with
    extension [A](fa: List[A])
      def map[B](f: A => B): List[B] = fa.map(f)

      def traverse[G[_], B](f: A => G[B])(using G: Applicative[G]): G[List[B]] =
        fa match {
          case Nil => G.pure(Nil)
          case (h :: t) => G.liftA2((h: B, t: List[B]) => h :: t)(f(h))(t.traverse(f))
        }

    given Traverse[Id] with

      extension [A](fa: Id[A])
        def map[B](f: A => B): Id[B] = f(fa)

        def traverse[G[_] : Applicative, B](f: A => G[B]): G[Id[B]] = f(fa)

    given [X]: Traverse[Const[X]] with
      extension [A](fa: Const[X][A])
        def map[B](f: A => B): Const[X][B] = fa

        def traverse[G[_] : Applicative, B](f: A => G[B]): G[Const[X][B]] =
          Applicative[G].pure(fa)

    inline given [F[_]](using inst : => K1.ProductInstances[Traverse, F], gen: => K1.ProductGeneric[F]): Traverse[F] with

      import Functor.functorGen as delegate

      extension [A](fa: F[A])
        def map[B](f: A => B): F[B] = delegate[F].map(fa)(f)

        def traverse[G[_], B](f: A => G[B])(using G: Applicative[G]): G[F[B]] =
          G.map(
            inst.foldLeft[A, G[Tuple]](fa)(G.pure(EmptyTuple) : G[Tuple])(
              [t[_]] => (acc: G[Tuple], trav: Traverse[t], ta: t[A]) =>
                Continue(
                  G.ap(
                    G.map(acc)((t: Tuple) => (x: t[B]) => t ++ Tuple(x) : Tuple)
                  )(trav.traverse(ta)(f))
                )
            )
          )(t => gen.fromRepr(_))
