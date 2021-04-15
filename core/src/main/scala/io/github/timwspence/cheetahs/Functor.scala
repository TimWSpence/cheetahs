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

trait Functor[F[_]]:
  def map[A, B](fa: F[A])(f: A => B): F[B]

object Functor:
  inline def apply[F[_]](using F: Functor[F]): F.type = F

  given Functor[List] with
    def map[A, B](fa: List[A])(f: A => B): List[B] =
      fa.map(f)

  given Functor[Option] with
    def map[A, B](fa: Option[A])(f: A => B): Option[B] =
      fa.map(f)
