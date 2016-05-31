package com.example.ash.reduxlmplementation;

/**
 * Created by Shevchuk Anton on 31.05.2016.
 */
public interface Reducer<State, Action> {
    public State reduce(State oldState, Action action);
}
