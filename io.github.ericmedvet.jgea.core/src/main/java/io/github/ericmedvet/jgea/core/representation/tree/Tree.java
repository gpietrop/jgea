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
package io.github.ericmedvet.jgea.core.representation.tree;

import io.github.ericmedvet.jgea.core.util.Sized;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Tree<C> implements Serializable, Sized, Iterable<Tree<C>> {

  private final C content;
  private final List<Tree<C>> children = new ArrayList<>();
  private Tree<C> parent;

  private Tree(C content, Tree<C> parent) {
    this.content = content;
    this.parent = parent;
  }

  public static <K> Tree<K> copyOf(Tree<K> other) {
    Tree<K> t = new Tree<>(other.content, null);
    for (Tree<K> child : other.children) {
      t.addChild(Tree.copyOf(child));
    }
    return t;
  }

  public static <K, H> Tree<H> map(Tree<K> other, Function<K, H> mapper) {
    Tree<H> t = Tree.of(mapper.apply(other.content));
    for (Tree<K> child : other.children) {
      t.addChild(Tree.map(child, mapper));
    }
    return t;
  }

  public static <K> Tree<K> of(K content) {
    return new Tree<>(content, null);
  }

  public static <K> Tree<K> of(K content, List<Tree<K>> children) {
    Tree<K> t = new Tree<>(content, null);
    for (Tree<K> child : children) {
      t.addChild(child);
    }
    return t;
  }

  private static <K> void prettyPrint(Tree<K> t, int d, PrintStream ps) {
    ps.printf(
        "%s (h=%2d d=%2d #c=%2d) %s",
        String.join("", Collections.nCopies(d, "  ")), t.height(), t.depth(), t.nChildren(), t.content());
    ps.println();
    t.forEach(c -> prettyPrint(c, d + 1, ps));
  }

  public void addChild(Tree<C> child) {
    children.add(child);
    child.parent = this;
  }

  public Tree<C> child(int i) {
    return children.get(i);
  }

  public Stream<Tree<C>> childStream() {
    return StreamSupport.stream(spliterator(), false);
  }

  public void clearChildren() {
    children.clear();
  }

  public C content() {
    return content;
  }

  public int depth() {
    if (parent == null) {
      return 0;
    }
    return parent.depth() + 1;
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, children);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tree<?> tree = (Tree<?>) o;
    return Objects.equals(content, tree.content) && children.equals(tree.children);
  }

  @Override
  public String toString() {
    return content.toString()
        + (children.isEmpty()
            ? ""
            : ("(" + children.stream().map(Tree::toString).collect(Collectors.joining(",")) + ")"));
  }

  public int height() {
    return 1 + children.stream().mapToInt(Tree::height).max().orElse(0);
  }

  public boolean isLeaf() {
    return children.isEmpty();
  }

  @Override
  public Iterator<Tree<C>> iterator() {
    return children.iterator();
  }

  public List<Tree<C>> leaves() {
    if (children.isEmpty()) {
      return List.of(this);
    }
    List<Tree<C>> leaves = new ArrayList<>();
    children.forEach(c -> leaves.addAll(c.leaves()));
    return leaves;
  }

  public int nChildren() {
    return children.size();
  }

  public Tree<C> parent() {
    return parent;
  }

  public void prettyPrint(PrintStream ps) {
    prettyPrint(this, 0, ps);
  }

  public boolean removeChild(Tree<C> child) {
    return children.remove(child);
  }

  @Override
  public int size() {
    return 1 + children.stream().mapToInt(Tree::size).sum();
  }

  public List<Tree<C>> topSubtrees() {
    List<Tree<C>> subtrees = new ArrayList<>();
    subtrees.add(this);
    children.forEach(c -> subtrees.addAll(c.topSubtrees()));
    return subtrees;
  }

  public List<C> visitDepth() {
    List<C> contents = new ArrayList<>(1 + children.size());
    contents.add(content);
    children.forEach(c -> contents.addAll(c.visitDepth()));
    return contents;
  }

  public List<C> visitLeaves() {
    return leaves().stream().map(Tree::content).toList();
  }
}
