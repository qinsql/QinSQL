/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.sql.vector;

import org.lealone.db.value.Value;

public class SingleValueVector extends ValueVector {

    private Value value;

    public SingleValueVector(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public ValueVector convertTo(int targetType) {
        value = value.convertTo(targetType);
        return this;
    }
}
