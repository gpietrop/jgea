/*-
 * ========================LICENSE_START=================================
 * jgea-core
 * %%
 * Copyright (C) 2018 - 2023 Eric Medvet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

package io.github.ericmedvet.jgea.core.operator;

import java.util.List;
import java.util.random.RandomGenerator;

public interface GeneticOperator<G> {

  List<? extends G> apply(List<? extends G> parents, RandomGenerator random);

  int arity();

  default GeneticOperator<G> andThen(GeneticOperator<G> other) {
    final GeneticOperator<G> thisOperator = this;
    return new GeneticOperator<>() {
      @Override
      public List<? extends G> apply(List<? extends G> parents, RandomGenerator random) {
        List<? extends G> intermediate = thisOperator.apply(parents, random);
        if (intermediate.size() < other.arity()) {
          throw new IllegalArgumentException(String.format(
              "Cannot apply composed operator: 2nd operator expected %d parents and found %d",
              other.arity(), intermediate.size()));
        }
        return other.apply(intermediate, random);
      }

      @Override
      public int arity() {
        return thisOperator.arity();
      }
    };
  }
}
