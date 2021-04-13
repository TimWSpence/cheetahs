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

private[data] object OptionTScope:

  opaque type OptionT[F[_], A] = F[Option[A]]

  object OptionT:

    def liftF[F[_], A](fa: F[A])(using F: Functor[F]): OptionT[F, A] =
      F.map(fa)(Some(_))

    given [F[_], A]: Coerce[F[Option[A]], OptionT[F, A]] with {}
