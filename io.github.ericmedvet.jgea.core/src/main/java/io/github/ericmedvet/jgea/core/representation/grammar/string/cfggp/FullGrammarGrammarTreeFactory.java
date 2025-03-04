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

package io.github.ericmedvet.jgea.core.representation.grammar.string.cfggp;

import io.github.ericmedvet.jgea.core.representation.grammar.string.StringGrammar;
import io.github.ericmedvet.jgea.core.representation.tree.Tree;
import io.github.ericmedvet.jgea.core.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FullGrammarGrammarTreeFactory<T> extends GrowGrammarTreeFactory<T> {

  public FullGrammarGrammarTreeFactory(int maxDepth, StringGrammar<T> grammar) {
    super(maxDepth, grammar);
  }

  public Tree<T> build(Random random, T symbol, int targetDepth) {
    if (targetDepth < 0) {
      return null;
    }
    Tree<T> tree = Tree.of(symbol);
    if (grammar.rules().containsKey(symbol)) {
      // a non-terminal
      List<List<T>> options = grammar.rules().get(symbol);
      List<List<T>> availableOptions = new ArrayList<>();
      // general idea: try the following
      // 1. choose expansion with min,max including target depth
      // 2. choose expansion
      for (List<T> option : options) {
        Pair<Double, Double> minMax = optionMinMaxDepth(option);
        if (((targetDepth - 1) >= minMax.first()) && ((targetDepth - 1) <= minMax.second())) {
          availableOptions.add(option);
        }
      }
      if (availableOptions.isEmpty()) {
        availableOptions.addAll(options);
      }
      int optionIndex = random.nextInt(availableOptions.size());
      for (int i = 0; i < availableOptions.get(optionIndex).size(); i++) {
        Tree<T> child = build(random, availableOptions.get(optionIndex).get(i), targetDepth - 1);
        if (child == null) {
          return null;
        }
        tree.addChild(child);
      }
    }
    return tree;
  }
}
