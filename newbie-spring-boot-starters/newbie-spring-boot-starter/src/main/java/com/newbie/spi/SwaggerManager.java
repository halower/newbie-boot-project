package com.newbie.spi;

public interface SwaggerManager<T> {
     default Boolean show(T path, T description, T requestMethod) {
         return false;
     };

    default int order() {
        return 10;
    }
}
