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

package io.github.ericmedvet.jgea.problem.regression;

import io.github.ericmedvet.jgea.core.representation.tree.Tree;
import io.github.ericmedvet.jgea.core.representation.tree.numeric.Element;
import java.util.function.Function;

public class FormulaMapper implements Function<Tree<String>, Tree<Element>> {

  private static Element fromString(String string) {
    for (Element.Operator operator : Element.Operator.values()) {
      if (operator.toString().equals(string)) {
        return operator;
      }
    }
    try {
      double value = Double.parseDouble(string);
      return new Element.Constant(value);
    } catch (NumberFormatException ex) {
      // just ignore
    }
    if (string.matches("[a-zA-Z]\\w*")) {
      return new Element.Variable(string);
    }
    return new Element.Decoration(string);
  }

  @Override
  public Tree<Element> apply(Tree<String> stringTree) {
    if (stringTree.isLeaf()) {
      return Tree.of(fromString(stringTree.content()));
    }
    if (stringTree.nChildren() == 1) {
      return apply(stringTree.child(0));
    }
    Tree<Element> tree = apply(stringTree.child(0));
    for (int i = 1; i < stringTree.nChildren(); i++) {
      tree.addChild(apply(stringTree.child(i)));
    }
    return tree;
  }
}
