package com.newbie.core.persistent.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;

public final class SpecificationsFactory {
    public static Specification contains(String propertyName, String value) {
        return StringUtils.isEmpty(propertyName) ? (root, query, cb) -> cb.like(root.get(propertyName), "%" + value + "%") : null;
    }
    public static Specification startWith(String propertyName, String value) {
        return StringUtils.isEmpty(propertyName) ? (root, query, cb) -> cb.like(root.get(propertyName), value + "%") : null;
    }

    public static Specification endWith(String propertyName, String value) {
        return StringUtils.isEmpty(propertyName) ? (root, query, cb) -> cb.like(root.get(propertyName),  "%" + value) : null;
    }

    public static Specification matchPattern(String propertyName, String pattern) {
        return StringUtils.isEmpty(propertyName) ? (root, query, cb) -> cb.like(root.get(propertyName),  pattern) : null;
    }

    public static Specification ne(String propertyName, Object value) {
        return (root, query, cb) -> cb.notEqual(root.get(propertyName),value);
    }

    public static Specification eq(String propertyName, String value) {
        return StringUtils.isEmpty(propertyName) ? (root, query, cb) -> cb.equal(root.get(propertyName),  value) : null;
    }

    public static Specification lt(String propertyName, Number value) {
        return (root, query, cb) -> cb.lt(root.get(propertyName),  value);
    }

    public static Specification le(String propertyName, Number value) {
        return (root, query, cb) -> cb.le(root.get(propertyName),  value);
    }

    public static Specification gt(String propertyName, Number value) {
        return (root, query, cb) -> cb.gt(root.get(propertyName),  value);
    }

    public static Specification ge(String propertyName, Number value) {
        return (root, query, cb) -> cb.ge(root.get(propertyName),  value);
    }

    public static Specification between(String propertyName, int min,int max) {
        return (root, query, cb) -> cb.between(root.get(propertyName),min,max);
    }

    public static Specification between(String propertyName, double min,double max) {
        return (root, query, cb) -> cb.between(root.get(propertyName),min,max);
    }

    public static Specification between(String propertyName, Date min, Date max) {
        return (root, query, cb) -> cb.between(root.get(propertyName),min,max);
    }

    public static Specification in(String propertyName, Collection c) {
        return (root, query, cb) -> root.get(propertyName).in(c);
    }

    public static Specification notIn(String propertyName, Collection c) {
        return (root, query, cb) -> root.get(propertyName).in(c).not();
    }

    public static Specification isNull(String propertyName) {
        return (root, query, cb) -> root.get(propertyName).isNull();
    }

    public static Specification isNotNull(String propertyName) {
        return (root, query, cb) -> root.get(propertyName).isNotNull();
    }
}
