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

package io.github.ericmedvet.jgea.problem.booleanfunction;

import io.github.ericmedvet.jgea.core.representation.tree.Tree;
import io.github.ericmedvet.jgea.core.representation.tree.booleanfunction.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class FormulaMapper implements Function<Tree<String>, List<Tree<Element>>> {

  public static final String MULTIPLE_OUTPUT_NON_TERMINAL = "<o>";

  private static Element fromString(String string) {
    for (Element.Operator operator : Element.Operator.values()) {
      if (operator.toString().equals(string)) {
        return operator;
      }
    }
    if (string.equals("0")) {
      return new Element.Constant(false);
    }
    if (string.equals("1")) {
      return new Element.Constant(true);
    }
    if (string.matches("[a-zA-Z]+[0-9.]+")) {
      return new Element.Variable(string);
    }
    return new Element.Decoration(string);
  }

  @Override
  public List<Tree<Element>> apply(Tree<String> stringTree) {
    if (stringTree.content().equals(MULTIPLE_OUTPUT_NON_TERMINAL)) {
      List<Tree<Element>> trees = new ArrayList<>();
      for (Tree<String> child : stringTree) {
        trees.add(singleMap(child));
      }
      return trees;
    } else {
      return Collections.singletonList(singleMap(stringTree));
    }
  }

  public Tree<Element> singleMap(Tree<String> stringTree) {
    if (stringTree.isLeaf()) {
      return Tree.of(fromString(stringTree.content()));
    }
    if (stringTree.nChildren() == 1) {
      return singleMap(stringTree.child(0));
    }
    Tree<Element> tree = singleMap(stringTree.child(0));
    for (int i = 1; i < stringTree.nChildren(); i++) {
      tree.addChild(singleMap(stringTree.child(i)));
    }
    return tree;
  }
}
