/*-
 * ========================LICENSE_START=================================
 * jgea-problem
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

package io.github.ericmedvet.jgea.problem.image;

import io.github.ericmedvet.jsdynsym.core.numerical.UnivariateRealFunction;
import java.awt.image.BufferedImage;

public class ImageUtils {
  private ImageUtils() {}

  public static BufferedImage render(UnivariateRealFunction f, int w, int h, boolean normalize) {
    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        double fOut = f.applyAsDouble(new double[] {(double) x / (double) w, (double) y / (double) h});
        if (normalize) {
          fOut = Math.tanh(fOut) / 2d + 0.5d;
        }
        int gray = (int) Math.round(fOut * 256d);
        int color = (gray << 16) | (gray << 8) | gray;
        bi.setRGB(x, y, color);
      }
    }
    return bi;
  }
}
