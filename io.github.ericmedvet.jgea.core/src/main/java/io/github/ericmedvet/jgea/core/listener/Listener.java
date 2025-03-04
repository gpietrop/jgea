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
package io.github.ericmedvet.jgea.core.listener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.logging.Logger;

@FunctionalInterface
public interface Listener<E> {

  void listen(E e);

  static <E> Listener<E> all(List<Listener<? super E>> listeners) {
    return new Listener<>() {
      @Override
      public void listen(E e) {
        listeners.forEach(l -> l.listen(e));
      }

      @Override
      public void done() {
        listeners.forEach(Listener::done);
      }
    };
  }

  static <E> Listener<E> deaf() {
    return e -> {};
  }

  default Listener<E> and(Listener<? super E> other) {
    Listener<E> inner = this;
    return new Listener<>() {
      @Override
      public void listen(E e) {
        inner.listen(e);
        other.listen(e);
      }

      @Override
      public void done() {
        inner.done();
        other.done();
      }
    };
  }

  default Listener<E> deferred(ExecutorService executorService) {
    Listener<E> thisListener = this;
    final Logger L = Logger.getLogger(Listener.class.getName());
    return new Listener<>() {
      @Override
      public void listen(E e) {
        executorService.submit(() -> {
          try {
            thisListener.listen(e);
          } catch (RuntimeException ex) {
            L.warning(String.format(
                "Listener %s cannot listen() event: %s",
                thisListener.getClass().getSimpleName(), ex));
          }
        });
      }

      @Override
      public void done() {
        executorService.submit(() -> {
          try {
            thisListener.done();
          } catch (RuntimeException ex) {
            L.warning(String.format(
                "Listener %s cannot done() event: %s",
                thisListener.getClass().getSimpleName(), ex));
          }
        });
      }
    };
  }

  default void done() {}

  default <F> Listener<F> forEach(Function<F, Collection<E>> splitter) {
    Listener<E> thisListener = this;
    return new Listener<>() {
      @Override
      public void listen(F f) {
        splitter.apply(f).forEach(thisListener::listen);
      }

      @Override
      public void done() {
        thisListener.done();
      }
    };
  }

  default <F> Listener<F> on(Function<F, E> function) {
    Listener<E> inner = this;
    return new Listener<>() {
      @Override
      public void listen(F f) {
        inner.listen(function.apply(f));
      }

      @Override
      public void done() {
        inner.done();
      }
    };
  }

  default Listener<E> onLast() {
    Listener<E> thisListener = this;
    return new Listener<>() {
      E lastE;

      @Override
      public void listen(E e) {
        lastE = e;
      }

      @Override
      public void done() {
        thisListener.listen(lastE);
        thisListener.done();
      }
    };
  }
}
