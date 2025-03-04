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

package io.github.ericmedvet.jgea.core.representation.graph.numeric.operatorgraph;

import io.github.ericmedvet.jgea.core.IndependentFactory;
import io.github.ericmedvet.jgea.core.representation.graph.Node;
import java.util.Objects;
import java.util.function.ToDoubleFunction;
import java.util.random.RandomGenerator;

public class OperatorNode extends Node implements ToDoubleFunction<double[]> {

  private final BaseOperator operator;

  public OperatorNode(int index, BaseOperator operator) {
    super(index);
    this.operator = operator;
  }

  public static IndependentFactory<OperatorNode> limitedIndexFactory(int limit, BaseOperator... operators) {
    return random -> new OperatorNode(random.nextInt(limit), operators[random.nextInt(operators.length)]);
  }

  public static IndependentFactory<OperatorNode> sequentialIndexFactory(BaseOperator... operators) {
    return new IndependentFactory<>() {
      int index = 0;

      @Override
      public OperatorNode build(RandomGenerator random) {
        index = index + 1;
        return new OperatorNode(index, operators[random.nextInt(operators.length)]);
      }
    };
  }

  @Override
  public double applyAsDouble(double... input) {
    return operator.applyAsDouble(input);
  }

  public BaseOperator getOperator() {
    return operator;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), operator);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    OperatorNode that = (OperatorNode) o;
    return operator == that.operator;
  }

  @Override
  public String toString() {
    return String.format("op%d[%s]", getIndex(), operator.toString().toLowerCase());
  }
}
