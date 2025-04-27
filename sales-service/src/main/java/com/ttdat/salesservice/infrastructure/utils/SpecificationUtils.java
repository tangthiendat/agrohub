package com.ttdat.salesservice.infrastructure.utils;

import com.ttdat.core.api.dto.request.FilterCriteria;
import com.ttdat.core.infrastructure.utils.FilterUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static  <T> Specification<T> buildSpecification(Map<String, String> filterParams, String fieldName, Class<?> fieldType) {
        if (filterParams.containsKey(fieldName)) {
            List<FilterCriteria> criteriaList = FilterUtils.getFilterCriteriaList(filterParams, fieldName);
            return criteriaList.stream()
                    .map(criteria -> (Specification<T>) (root, query, criteriaBuilder) -> {
                        if (fieldType == String.class) {
                            return criteriaBuilder.equal(root.get(fieldName), criteria.getValue());
                        } else if (fieldType == Boolean.class) {
                            return criteriaBuilder.equal(root.get(fieldName), Boolean.parseBoolean((String) criteria.getValue()));
                        } else if (fieldType == Long.class) {
                            return criteriaBuilder.equal(root.get(fieldName), Long.parseLong((String) criteria.getValue()));
                        } else {
                            throw new UnsupportedOperationException("Field type not supported");
                        }
                    })
                    .reduce(Specification::or)
                    .orElse(Specification.where(null));
        }
        return Specification.where(null);
    }

    public static <T> Specification<T> buildJoinSpecification(Map<String, String> filterParams, String joinFieldName, String fieldName, Class<?> fieldType) {
        if (filterParams.containsKey(fieldName)) {
            List<FilterCriteria> criteriaList = FilterUtils.getFilterCriteriaList(filterParams, fieldName);
            return criteriaList.stream()
                    .map(criteria -> (Specification<T>) (root, query, criteriaBuilder) -> {
                        if (fieldType == String.class) {
                            return criteriaBuilder.equal(root.get(joinFieldName).get(fieldName), criteria.getValue());
                        } else if (fieldType == Long.class) {
                            return criteriaBuilder.equal(root.get(joinFieldName).get(fieldName), Long.parseLong((String) criteria.getValue()));
                        } else {
                            throw new UnsupportedOperationException("Field type not supported");
                        }
                    })
                    .reduce(Specification::or)
                    .orElse(Specification.where(null));
        }
        return Specification.where(null);
    }
}
