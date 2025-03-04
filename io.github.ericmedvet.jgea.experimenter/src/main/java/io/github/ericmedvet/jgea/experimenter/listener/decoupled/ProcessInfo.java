/*-
 * ========================LICENSE_START=================================
 * jgea-experimenter
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
package io.github.ericmedvet.jgea.experimenter.listener.decoupled;

import io.github.ericmedvet.jgea.experimenter.listener.net.NetUtils;
import java.io.Serializable;

/**
 * @author "Eric Medvet" on 2023/11/03 for jgea
 */
public record ProcessInfo(String processName, String username, long usedMemory, long maxMemory)
    implements Serializable {
  public static ProcessInfo local() {
    return new ProcessInfo(
        NetUtils.getProcessName(),
        NetUtils.getUserName(),
        NetUtils.getProcessUsedMemory(),
        NetUtils.getProcessMaxMemory());
  }
}
