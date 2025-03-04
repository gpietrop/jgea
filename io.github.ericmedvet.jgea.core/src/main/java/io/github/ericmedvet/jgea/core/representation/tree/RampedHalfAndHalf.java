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

package io.github.ericmedvet.jgea.core.representation.tree;

import io.github.ericmedvet.jgea.core.Factory;
import io.github.ericmedvet.jgea.core.IndependentFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.random.RandomGenerator;

public class RampedHalfAndHalf<N> implements Factory<Tree<N>> {
  private final int minHeight;
  private final int maxHeight;
  private final FullTreeBuilder<N> fullTreeFactory;
  private final GrowTreeBuilder<N> growTreeBuilder;

  public RampedHalfAndHalf(
      int minHeight,
      int maxHeight,
      ToIntFunction<N> arityFunction,
      IndependentFactory<N> nonTerminalFactory,
      IndependentFactory<N> terminalFactory) {
    this.minHeight = minHeight;
    this.maxHeight = maxHeight;
    fullTreeFactory = new FullTreeBuilder<>(arityFunction, nonTerminalFactory, terminalFactory);
    growTreeBuilder = new GrowTreeBuilder<>(arityFunction, nonTerminalFactory, terminalFactory);
  }

  @Override
  public List<Tree<N>> build(int n, RandomGenerator random) {
    List<Tree<N>> trees = new ArrayList<>();
    // full
    int height = minHeight;
    while (trees.size() < n / 2) {
      Tree<N> tree = fullTreeFactory.build(random, height);
      if (tree != null) {
        trees.add(tree);
      }
      height = height + 1;
      if (height > maxHeight) {
        height = minHeight;
      }
    }
    // grow
    while (trees.size() < n) {
      Tree<N> tree = growTreeBuilder.build(random, height);
      if (tree != null) {
        trees.add(tree);
      }
      height = height + 1;
      if (height > maxHeight) {
        height = minHeight;
      }
    }
    return trees;
  }
}
