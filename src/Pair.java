// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// Pair is a generic data type that allows for two types in one
// ===========================================================================
public class Pair<T, U> {         
    public T first;
    public U second;
    public Pair(final T t, final U u) {         
        this.first= t;
        this.second= u;
     }
 }