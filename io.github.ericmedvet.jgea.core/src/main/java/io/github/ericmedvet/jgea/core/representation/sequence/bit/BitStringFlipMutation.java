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

package io.github.ericmedvet.jgea.core.representation.sequence.bit;

import io.github.ericmedvet.jgea.core.operator.Mutation;
import java.util.Arrays;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public class BitStringFlipMutation implements Mutation<BitString> {

  private final double p;

  public BitStringFlipMutation(double p) {
    this.p = p;
  }

  @Override
  public BitString mutate(BitString parent, RandomGenerator random) {
    boolean[] bits = Arrays.copyOf(parent.bits(), parent.size());
    IntStream.range(0, bits.length).forEach(i -> bits[i] = (random.nextDouble() < p) != bits[i]);
    return new BitString(bits);
  }
}
