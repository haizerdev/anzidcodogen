package com.anzid.annotation;

import kotlinx.coroutines.flow.MutableSharedFlow;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.SharedFlow;

/**
 * Workaround for unresolved reference
 * asSharedFlow and asStateFlow
 */
public class PublicObserverUtils {

    public static <T> SharedFlow<T> asSharedFlow(MutableSharedFlow<T> flow) {
        return kotlinx.coroutines.flow.FlowKt.asSharedFlow(flow);
    }

    public static <T> SharedFlow<T> asStateFlow(MutableStateFlow<T> flow) {
        return kotlinx.coroutines.flow.FlowKt.asStateFlow(flow);
    }
}
