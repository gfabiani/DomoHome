package com.fabiani.domohome.app;

/**
 * Created by Giovanni on 26/05/2015.
 * Java 8-like predicate
 */

public interface CommandPredicate {
    boolean test(Command command);
}

