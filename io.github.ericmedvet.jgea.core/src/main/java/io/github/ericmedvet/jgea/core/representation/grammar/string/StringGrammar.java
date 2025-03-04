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

package io.github.ericmedvet.jgea.core.representation.grammar.string;

import io.github.ericmedvet.jgea.core.representation.grammar.Grammar;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class StringGrammar<T> implements Serializable, Grammar<T, List<T>> {

  public static final String RULE_ASSIGNMENT_STRING = "::=";
  public static final String RULE_OPTION_SEPARATOR_STRING = "|";
  private final Map<T, List<List<T>>> rules;
  private T startingSymbol;

  public StringGrammar() {
    rules = new LinkedHashMap<>();
  }

  public static StringGrammar<String> load(InputStream inputStream) throws IOException {
    return load(inputStream, "UTF-8");
  }

  public static StringGrammar<String> load(InputStream inputStream, String charset) throws IOException {
    StringGrammar<String> grammar = new StringGrammar<>();
    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset));
    String line;
    while ((line = br.readLine()) != null) {
      String[] components = line.split(Pattern.quote(RULE_ASSIGNMENT_STRING));
      String toReplaceSymbol = components[0].trim();
      String[] optionStrings = components[1].split(Pattern.quote(RULE_OPTION_SEPARATOR_STRING));
      if (grammar.startingSymbol() == null) {
        grammar.setStartingSymbol(toReplaceSymbol);
      }
      List<List<String>> options = new ArrayList<>();
      for (String optionString : optionStrings) {
        List<String> symbols = new ArrayList<>();
        for (String symbol : optionString.split("\\s+")) {
          if (!symbol.trim().isEmpty()) {
            symbols.add(symbol.trim());
          }
        }
        if (!symbols.isEmpty()) {
          options.add(symbols);
        }
      }
      grammar.rules().put(toReplaceSymbol, options);
    }
    br.close();
    return grammar;
  }

  public Map<T, List<List<T>>> rules() {
    return rules;
  }

  public T startingSymbol() {
    return startingSymbol;
  }

  @Override
  public Collection<T> usedSymbols(List<T> ts) {
    return ts;
  }

  public void setStartingSymbol(T startingSymbol) {
    this.startingSymbol = startingSymbol;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<T, List<List<T>>> rule : rules.entrySet()) {
      sb.append(rule.getKey())
          .append(" ")
          .append(rule.getKey().equals(startingSymbol) ? "*" : "")
          .append(RULE_ASSIGNMENT_STRING + " ");
      for (List<T> option : rule.getValue()) {
        for (T symbol : option) {
          sb.append(symbol).append(" ");
        }
        sb.append(RULE_OPTION_SEPARATOR_STRING + " ");
      }
      sb.delete(sb.length() - 2 - RULE_OPTION_SEPARATOR_STRING.length(), sb.length());
      sb.append("\n");
    }
    return sb.toString();
  }
}
