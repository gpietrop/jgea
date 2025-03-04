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

package io.github.ericmedvet.jgea.core.representation.graph;

import io.github.ericmedvet.jgea.core.IndependentFactory;
import io.github.ericmedvet.jgea.core.operator.Mutation;
import io.github.ericmedvet.jgea.core.util.Misc;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class ArcAddition<N, A> implements Mutation<Graph<N, A>> {
  private final IndependentFactory<A> arcFactory;
  private final boolean allowCycles;

  public ArcAddition(IndependentFactory<A> arcFactory, boolean allowCycles) {
    this.arcFactory = arcFactory;
    this.allowCycles = allowCycles;
  }

  @Override
  public Graph<N, A> mutate(Graph<N, A> parent, RandomGenerator random) {
    Graph<N, A> child = LinkedHashGraph.copyOf(parent);
    if (!parent.nodes().isEmpty()) {
      List<N> fromNodes = Misc.shuffle(new ArrayList<>(child.nodes()), random);
      List<N> toNodes = Misc.shuffle(new ArrayList<>(child.nodes()), random);
      boolean added = false;
      for (N fromNode : fromNodes) {
        for (N toNode : toNodes) {
          if (!fromNode.equals(toNode) && !child.hasArc(fromNode, toNode)) {
            child.setArcValue(fromNode, toNode, arcFactory.build(random));
            if (!allowCycles && child.hasCycles()) {
              child.removeArc(fromNode, toNode);
            } else {
              added = true;
              break;
            }
          }
        }
        if (added) {
          break;
        }
      }
    }
    return child;
  }
}
