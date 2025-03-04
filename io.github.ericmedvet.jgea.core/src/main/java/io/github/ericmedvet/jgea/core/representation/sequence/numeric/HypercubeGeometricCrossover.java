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

package io.github.ericmedvet.jgea.core.representation.sequence.numeric;

import io.github.ericmedvet.jgea.core.representation.sequence.ElementWiseCrossover;
import io.github.ericmedvet.jsdynsym.core.DoubleRange;

public class HypercubeGeometricCrossover extends ElementWiseCrossover<Double> {

  public HypercubeGeometricCrossover(DoubleRange range) {
    super((v1, v2, random) -> v1 + (v2 - v1) * range.denormalize(random.nextDouble()));
  }

  public HypercubeGeometricCrossover() {
    this(DoubleRange.UNIT);
  }
}
