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

package io.github.ericmedvet.jgea.core.solver;

import io.github.ericmedvet.jgea.core.Factory;
import io.github.ericmedvet.jgea.core.operator.Mutation;
import io.github.ericmedvet.jgea.core.order.PartialComparator;
import io.github.ericmedvet.jgea.core.problem.QualityBasedProblem;
import io.github.ericmedvet.jgea.core.solver.state.POSetPopulationState;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;

public class RandomWalk<P extends QualityBasedProblem<S, Q>, G, S, Q>
    extends AbstractPopulationBasedIterativeSolver<
        POSetPopulationState<Individual<G, S, Q>, G, S, Q>, P, Individual<G, S, Q>, G, S, Q> {

  private final Mutation<G> mutation;

  public RandomWalk(
      Function<? super G, ? extends S> solutionMapper,
      Factory<? extends G> genotypeFactory,
      Predicate<? super POSetPopulationState<Individual<G, S, Q>, G, S, Q>> stopCondition,
      Mutation<G> mutation) {
    super(solutionMapper, genotypeFactory, i -> i, stopCondition);
    this.mutation = mutation;
  }

  @Override
  public POSetPopulationState<Individual<G, S, Q>, G, S, Q> init(
      P problem, RandomGenerator random, ExecutorService executor) throws SolverException {
    return new RandomSearch.State<>(
        getAll(map(genotypeFactory.build(1, random), 0, problem.qualityFunction(), executor))
            .iterator()
            .next());
  }

  @Override
  public POSetPopulationState<Individual<G, S, Q>, G, S, Q> update(
      P problem,
      RandomGenerator random,
      ExecutorService executor,
      POSetPopulationState<Individual<G, S, Q>, G, S, Q> state)
      throws SolverException {
    Individual<G, S, Q> currentIndividual = state.population().firsts().iterator().next();
    Individual<G, S, Q> newIndividual =
        getAll(
                map(
                    List.of(mutation.mutate(currentIndividual.genotype(), random)),
                    state.nOfIterations(),
                    problem.qualityFunction(),
                    executor))
            .iterator()
            .next();
    if (comparator(problem)
        .compare(newIndividual, currentIndividual)
        .equals(PartialComparator.PartialComparatorOutcome.BEFORE)) {
      currentIndividual = newIndividual;
    }
    return RandomSearch.State.from(
        (RandomSearch.State<Individual<G, S, Q>, G, S, Q>) state,
        progress(state),
        1,
        1,
        currentIndividual);
  }
}
