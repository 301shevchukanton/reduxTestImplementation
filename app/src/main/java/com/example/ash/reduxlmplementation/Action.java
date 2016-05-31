package com.example.ash.reduxlmplementation;

/**
 * Created by Shevchuk Anton on 31.05.2016.
 */
public class Action<ActionType extends Enum, Value> {
    public final ActionType type;
    public final Value value;

    public Action(ActionType type, Value value) {
        this.type = type;
        this.value = value;
    }

}
