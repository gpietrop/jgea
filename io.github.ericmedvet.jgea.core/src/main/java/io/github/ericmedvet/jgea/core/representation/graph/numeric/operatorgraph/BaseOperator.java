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

import java.util.Arrays;
import java.util.function.ToDoubleFunction;

public enum BaseOperator implements ToDoubleFunction<double[]> {
  ADDITION("+", x -> Arrays.stream(x).sum(), 1, Integer.MAX_VALUE),
  SUBTRACTION("-", x -> x[0] - (x.length == 1 ? 0d : x[1]), 1, 2),
  DIVISION("/", x -> x[0] / (x.length == 1 ? 1d : x[1]), 1, 2),
  PROT_DIVISION("p/", x -> x.length == 1 ? x[0] : ((x[1] != 0d) ? (x[0] / x[1]) : 1d), 1, 2),
  MULTIPLICATION("*", x -> Arrays.stream(x).reduce(1d, (v1, v2) -> v1 * v2), 1, Integer.MAX_VALUE),
  LOG("log", x -> Math.log(x[0]), 1, 1),
  PROT_LOG("plog", x -> (x[0] > 0d) ? Math.log(x[0]) : 0d, 1, 1),
  EXP("exp", x -> Math.exp(x[0]), 1, 1),
  SIN("sin", x -> Math.sin(x[0]), 1, 1),
  COS("cos", x -> Math.cos(x[0]), 1, 1),
  INVERSE("1/", x -> 1d / x[0], 1, 1),
  OPPOSITE("_", x -> 0d - x[0], 1, 1),
  SQRT("√", x -> Math.sqrt(x[0]), 1, 1),
  SQ("²", x -> Math.pow(x[0], 2d), 1, 1);

  private final String string;
  private final ToDoubleFunction<double[]> function;
  private final int minArity;
  private final int maxArity;

  BaseOperator(String string, ToDoubleFunction<double[]> function, int minArity, int maxArity) {
    this.string = string;
    this.function = function;
    this.minArity = minArity;
    this.maxArity = maxArity;
  }

  @Override
  public double applyAsDouble(double... input) {
    return function.applyAsDouble(input);
  }

  public int maxArity() {
    return maxArity;
  }

  public int minArity() {
    return minArity;
  }

  @Override
  public String toString() {
    return string;
  }
}
