package io.github.ericmedvet.jgea.experimenter.builders;

import io.github.ericmedvet.jgea.core.representation.NamedUnivariateRealFunction;
import io.github.ericmedvet.jgea.core.representation.tree.Tree;
import io.github.ericmedvet.jgea.experimenter.InvertibleMapper;
import io.github.ericmedvet.jgea.problem.regression.symbolic.Element;
import io.github.ericmedvet.jgea.problem.regression.symbolic.TreeBasedUnivariateRealFunction;
import io.github.ericmedvet.jgea.problem.regression.univariate.UnivariateRegressionFitness;
import io.github.ericmedvet.jgea.problem.regression.univariate.UnivariateRegressionProblem;
import io.github.ericmedvet.jnb.core.Param;

import java.util.List;

/**
 * @author "Eric Medvet" on 2023/05/01 for jgea
 */
public class Mappers {
  private Mappers() {
  }

  @SuppressWarnings("unused")
  public static InvertibleMapper<Tree<Element>, NamedUnivariateRealFunction> treeUnivariateRealFunctionFromNames(
      @Param("xVarNames") List<String> xVarNames,
      @Param("yVarName") String yVarName
  ) {
    return new InvertibleMapper<>() {
      @Override
      public NamedUnivariateRealFunction apply(Tree<Element> t) {
        return new TreeBasedUnivariateRealFunction(t, xVarNames, yVarName);
      }

      @Override
      public Tree<Element> exampleInput() {
        List<Tree<Element.Variable>> children = xVarNames.stream()
            .map(s -> Tree.of(new Element.Variable(s)))
            .toList();
        //noinspection unchecked,rawtypes
        return Tree.of(
            Element.Operator.ADDITION,
            (List) children
        );
      }
    };
  }

  @SuppressWarnings("unused")
  public static InvertibleMapper<Tree<Element>, NamedUnivariateRealFunction> treeUnivariateRealFunctionFromProblem(
      @Param("problem") UnivariateRegressionProblem<UnivariateRegressionFitness> problem
  ) {
    if (problem.qualityFunction().getDataset().yVarNames().size() != 1) {
      throw new IllegalArgumentException(
          "Problem has %d y variables, instead of just one: not suitable for univariate regression".formatted(
              problem.qualityFunction()
                  .getDataset()
                  .yVarNames()
                  .size())
      );
    }
    return treeUnivariateRealFunctionFromNames(
        problem.qualityFunction().getDataset().xVarNames(),
        problem.qualityFunction().getDataset().yVarNames().get(0)
    );
  }
}
