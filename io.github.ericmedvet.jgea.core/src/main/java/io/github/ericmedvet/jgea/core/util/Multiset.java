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
package io.github.ericmedvet.jgea.core.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author "Eric Medvet" on 2023/11/02 for jgea
 */
public interface Multiset<E> extends Collection<E> {
  int count(E e);

  Set<E> elementSet();

  @SafeVarargs
  static <E> Multiset<E> of(E... es) {
    return new LinkedHashMultiset<>(List.of(es));
  }
}
