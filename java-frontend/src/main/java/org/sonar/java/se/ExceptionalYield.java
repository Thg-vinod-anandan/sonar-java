/*
 * SonarQube Java
 * Copyright (C) 2012-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.se;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.sonar.java.se.symbolicvalues.SymbolicValue;
import org.sonar.plugins.java.api.semantic.Type;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ExceptionalYield extends MethodYield {

  @Nullable
  private Type exceptionType;

  public ExceptionalYield(int arity, boolean varArgs) {
    super(arity, varArgs);
    this.exceptionType = null;
  }

  public ExceptionalYield(int arity, boolean varArgs, ExplodedGraph.Node node, MethodBehavior behavior) {
    super(arity, varArgs, node, behavior);
    this.exceptionType = null;
  }

  @Override
  public Stream<ProgramState> statesAfterInvocation(List<SymbolicValue> invocationArguments, List<Type> invocationTypes, ProgramState programState,
    Supplier<SymbolicValue> svSupplier) {
    return parametersAfterInvocation(invocationArguments, invocationTypes, programState)
      .map(s -> s.stackValue(svSupplier.get()))
      .distinct();
  }

  public void setExceptionType(Type exceptionType) {
    this.exceptionType = exceptionType;
  }

  @CheckForNull
  public Type exceptionType() {
    return exceptionType;
  }

  @Override
  public String toString() {
    return String.format("{params: %s, exceptional%s}", Arrays.toString(parametersConstraints()), exceptionType == null ? "" : (" (" + exceptionType.fullyQualifiedName() + ")"));
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(3, 1295)
      .appendSuper(super.hashCode())
      .append(exceptionType)
      .hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ExceptionalYield other = (ExceptionalYield) obj;
    return new EqualsBuilder()
      .appendSuper(super.equals(obj))
      .append(exceptionType, other.exceptionType)
      .isEquals();
  }

}
