package org.startit.objectservice.utils;

public interface LinkUtils<T> {

    T addOperationWithLink(T clazz);

    T getOperationWithLink(T clazz);
}
