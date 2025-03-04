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

package io.github.ericmedvet.jgea.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.random.RandomGenerator;

public interface IndependentFactory<T> extends Factory<T> {

  T build(RandomGenerator random);

  @SafeVarargs
  static <K> IndependentFactory<K> oneOf(IndependentFactory<K>... factories) {
    return random -> factories[random.nextInt(factories.length)].build(random);
  }

  @SafeVarargs
  static <K> IndependentFactory<K> picker(K... ks) {
    return random -> ks[random.nextInt(ks.length)];
  }

  static <K> IndependentFactory<K> picker(List<? extends K> ks) {
    return random -> ks.get(random.nextInt(ks.size()));
  }

  @Override
  default List<T> build(int n, RandomGenerator random) {
    List<T> ts = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      ts.add(build(random));
    }
    return ts;
  }

  default <K> IndependentFactory<K> then(Function<T, K> f) {
    IndependentFactory<T> thisFactory = this;
    return random -> f.apply(thisFactory.build(random));
  }
}
