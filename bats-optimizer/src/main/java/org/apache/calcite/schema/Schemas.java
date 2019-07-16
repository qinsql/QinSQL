/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.schema;

import org.apache.calcite.DataContext;
import org.apache.calcite.materialize.Lattice;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.sql.type.SqlTypeUtil;
import org.apache.calcite.util.Enumerable;
import org.apache.calcite.util.Pair;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import static org.apache.calcite.schema.CalciteSchema.LatticeEntry;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Utility functions for schemas.
 */
public final class Schemas {

  private Schemas() {
    throw new AssertionError("no instances!");
  }

  public static CalciteSchema.FunctionEntry resolve(
      RelDataTypeFactory typeFactory,
      String name,
      Collection<CalciteSchema.FunctionEntry> functionEntries,
      List<RelDataType> argumentTypes) {
    final List<CalciteSchema.FunctionEntry> matches = new ArrayList<>();
    for (CalciteSchema.FunctionEntry entry : functionEntries) {
      if (matches(typeFactory, entry.getFunction(), argumentTypes)) {
        matches.add(entry);
      }
    }
    switch (matches.size()) {
    case 0:
      return null;
    case 1:
      return matches.get(0);
    default:
      throw new RuntimeException("More than one match for " + name
          + " with arguments " + argumentTypes);
    }
  }

  private static boolean matches(RelDataTypeFactory typeFactory,
      Function member, List<RelDataType> argumentTypes) {
    List<FunctionParameter> parameters = member.getParameters();
    if (parameters.size() != argumentTypes.size()) {
      return false;
    }
    for (int i = 0; i < argumentTypes.size(); i++) {
      RelDataType argumentType = argumentTypes.get(i);
      FunctionParameter parameter = parameters.get(i);
      if (!canConvert(argumentType, parameter.getType(typeFactory))) {
        return false;
      }
    }
    return true;
  }

  private static boolean canConvert(RelDataType fromType, RelDataType toType) {
    return SqlTypeUtil.canAssignFrom(toType, fromType);
  }

  /** Returns an {@link org.apache.calcite.util.Enumerable} over the rows of
   * a given table, representing each row as an object array. */
  public static Enumerable<Object[]> enumerable(final ScannableTable table,
      final DataContext root) {
    return table.scan(root);
  }

  /** Returns an {@link org.apache.calcite.util.Enumerable} over the rows of
   * a given table, not applying any filters, representing each row as an object
   * array. */
  public static Enumerable<Object[]> enumerable(final FilterableTable table,
      final DataContext root) {
    return table.scan(root, ImmutableList.of());
  }
 
  /** Returns an {@link org.apache.calcite.util.Enumerable} over object
   * arrays, given a fully-qualified table name which leads to a
   * {@link ScannableTable}. */
  public static Table table(DataContext root, String... names) {
    SchemaPlus schema = root.getRootSchema();
    final List<String> nameList = Arrays.asList(names);
    for (Iterator<? extends String> iterator = nameList.iterator();;) {
      String name = iterator.next();
      if (iterator.hasNext()) {
        schema = schema.getSubSchema(name);
      } else {
        return schema.getTable(name);
      }
    }
  }

  /** Returns an implementation of
   * {@link RelProtoDataType}
   * that asks a given table for its row type with a given type factory. */
  public static RelProtoDataType proto(final Table table) {
    return table::getRowType;
  }

  /** Returns an implementation of {@link RelProtoDataType}
   * that asks a given scalar function for its return type with a given type
   * factory. */
  public static RelProtoDataType proto(final ScalarFunction function) {
    return function::getReturnType;
  }

  /** Returns the star tables defined in a schema.
   *
   * @param schema Schema */
  public static List<CalciteSchema.TableEntry> getStarTables(
      CalciteSchema schema) {
    final List<CalciteSchema.LatticeEntry> list = getLatticeEntries(schema);
    return Lists.transform(list, entry -> {
      final CalciteSchema.TableEntry starTable =
          Objects.requireNonNull(entry).getStarTable();
      assert starTable.getTable().getJdbcTableType()
          == Schema.TableType.STAR;
      return entry.getStarTable();
    });
  }

  /** Returns the lattices defined in a schema.
   *
   * @param schema Schema */
  public static List<Lattice> getLattices(CalciteSchema schema) {
    final List<CalciteSchema.LatticeEntry> list = getLatticeEntries(schema);
    return Lists.transform(list, CalciteSchema.LatticeEntry::getLattice);
  }

  /** Returns the lattices defined in a schema.
   *
   * @param schema Schema */
  public static List<CalciteSchema.LatticeEntry> getLatticeEntries(
      CalciteSchema schema) {
    final List<LatticeEntry> list = new ArrayList<>();
    gatherLattices(schema, list);
    return list;
  }

  private static void gatherLattices(CalciteSchema schema,
      List<CalciteSchema.LatticeEntry> list) {
    list.addAll(schema.getLatticeMap().values());
    for (CalciteSchema subSchema : schema.getSubSchemaMap().values()) {
      gatherLattices(subSchema, list);
    }
  }

  /** Returns a sub-schema of a given schema obtained by following a sequence
   * of names.
   *
   * <p>The result is null if the initial schema is null or any sub-schema does
   * not exist.
   */
  public static CalciteSchema subSchema(CalciteSchema schema,
      Iterable<String> names) {
    for (String string : names) {
      if (schema == null) {
        return null;
      }
      schema = schema.getSubSchema(string, false);
    }
    return schema;
  }

  /** Generates a table name that is unique within the given schema. */
  public static String uniqueTableName(CalciteSchema schema, String base) {
    String t = Objects.requireNonNull(base);
    for (int x = 0; schema.getTable(t, true) != null; x++) {
      t = base + x;
    }
    return t;
  }

  /** Creates a path with a given list of names starting from a given root
   * schema. */
  public static Path path(CalciteSchema rootSchema, Iterable<String> names) {
    final ImmutableList.Builder<Pair<String, Schema>> builder =
        ImmutableList.builder();
    Schema schema = rootSchema.plus();
    final Iterator<String> iterator = names.iterator();
    if (!iterator.hasNext()) {
      return PathImpl.EMPTY;
    }
    if (!rootSchema.name.isEmpty()) {
      Preconditions.checkState(rootSchema.name.equals(iterator.next()));
    }
    for (;;) {
      final String name = iterator.next();
      builder.add(Pair.of(name, schema));
      if (!iterator.hasNext()) {
        return path(builder.build());
      }
      schema = schema.getSubSchema(name);
    }
  }

  public static PathImpl path(ImmutableList<Pair<String, Schema>> build) {
    return new PathImpl(build);
  }

  /** Returns the path to get to a schema from its root. */
  public static Path path(SchemaPlus schema) {
    List<Pair<String, Schema>> list = new ArrayList<>();
    for (SchemaPlus s = schema; s != null; s = s.getParentSchema()) {
      list.add(Pair.of(s.getName(), s));
    }
    return new PathImpl(ImmutableList.copyOf(Lists.reverse(list)));
  }

//  /** Dummy data context that has no variables. */
//  private static class DummyDataContext implements DataContext {
//    private final CalciteConnection connection;
//    private final SchemaPlus rootSchema;
//    private final ImmutableMap<String, Object> map;
//
//    DummyDataContext(CalciteConnection connection, SchemaPlus rootSchema) {
//      this.connection = connection;
//      this.rootSchema = rootSchema;
//      this.map = ImmutableMap.of();
//    }
//
//    public SchemaPlus getRootSchema() {
//      return rootSchema;
//    }
//
//    public JavaTypeFactory getTypeFactory() {
//      return connection.getTypeFactory();
//    }
//
//    public QueryProvider getQueryProvider() {
//      return connection;
//    }
//
//    public Object get(String name) {
//      return map.get(name);
//    }
//  }

  /** Implementation of {@link Path}. */
  private static class PathImpl
      extends AbstractList<Pair<String, Schema>> implements Path {
    private final ImmutableList<Pair<String, Schema>> pairs;

    private static final PathImpl EMPTY =
        new PathImpl(ImmutableList.of());

    PathImpl(ImmutableList<Pair<String, Schema>> pairs) {
      this.pairs = pairs;
    }

    @Override public boolean equals(Object o) {
      return this == o
          || o instanceof PathImpl
          && pairs.equals(((PathImpl) o).pairs);
    }

    @Override public int hashCode() {
      return pairs.hashCode();
    }

    public Pair<String, Schema> get(int index) {
      return pairs.get(index);
    }

    public int size() {
      return pairs.size();
    }

    public Path parent() {
      if (pairs.isEmpty()) {
        throw new IllegalArgumentException("at root");
      }
      return new PathImpl(pairs.subList(0, pairs.size() - 1));
    }

    public List<String> names() {
      return new AbstractList<String>() {
        public String get(int index) {
          return pairs.get(index + 1).left;
        }

        public int size() {
          return pairs.size() - 1;
        }
      };
    }

    public List<Schema> schemas() {
      return Pair.right(pairs);
    }
  }
}

// End Schemas.java
