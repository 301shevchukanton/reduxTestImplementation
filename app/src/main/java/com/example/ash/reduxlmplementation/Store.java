package com.example.ash.reduxlmplementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shevchuk Anton on 31.05.2016.
 */
public class Store<Action, State> {

    private List<Reducer<State, Action>> reducers = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    private State lastState;


    public Store(State lastState) {
        this.lastState = lastState;
    }

    public void addReducer(Reducer<State, Action> reducer) {
        reducers.add(reducer);
    }

    public void subscribeView(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.stateHasBeenChanged();
        }
    }

    public State getState() {
        return this.lastState;
    }

    public void dispatch(Action action) {
        for (Reducer<State, Action> reducer : reducers) {
            this.lastState = reducer.reduce(this.lastState, action);
        }
        notifyObservers();
    }

}
