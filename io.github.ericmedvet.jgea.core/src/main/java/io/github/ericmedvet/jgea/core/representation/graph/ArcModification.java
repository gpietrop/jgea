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

import io.github.ericmedvet.jgea.core.operator.Mutation;
import java.util.random.RandomGenerator;

public class ArcModification<N, A> implements Mutation<Graph<N, A>> {
  private final Mutation<A> arcMutation;
  private final double rate;

  public ArcModification(Mutation<A> arcMutation, double rate) {
    this.arcMutation = arcMutation;
    this.rate = rate;
  }

  @Override
  public Graph<N, A> mutate(Graph<N, A> parent, RandomGenerator random) {
    Graph<N, A> child = LinkedHashGraph.copyOf(parent);
    for (Graph.Arc<N> arc : child.arcs()) {
      if (random.nextDouble() < rate) {
        A arcValue = child.getArcValue(arc);
        child.setArcValue(arc, arcMutation.mutate(arcValue, random));
      }
    }
    return child;
  }
}
