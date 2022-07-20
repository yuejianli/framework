package top.yueshushu.business.repository.facade.impl;
import org.bson.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import top.yueshushu.business.base.page.PageQuery;
import top.yueshushu.business.base.page.PageResult;
import top.yueshushu.business.repository.factory.BaseFactory;

/**
 * @author xin.liu
 * @date 2022-01-14
 */
public abstract class BaseRepositoryImpl<T,E> {

    @Resource
    public MongoTemplate mongoTemplate;

    protected Class<T> DOClass = this.getDOClazz();
    protected Class<E> entityClass = this.getEntityClazz();

    protected Class<T> getDOClazz() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    protected Class<E> getEntityClazz() {
        return (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * 根据筛选条件进行聚合，获取总数
     *
     * @param mongoTemplate
     * @param criteria
     * @return
     */
    public int aggregateCount(MongoTemplate mongoTemplate, Criteria criteria, String collectionName) {

        TypedAggregation<T> countAggr =
                Aggregation.newAggregation(DOClass, Aggregation.match(criteria),
                        Aggregation.group().count().as("count"));
        AggregationResults<Document> aggregate = mongoTemplate.aggregate(countAggr, collectionName, Document.class);
        List<Document> mappedResults = aggregate.getMappedResults();
        if (CollectionUtils.isEmpty(mappedResults)) {
            return 0;
        }

        Document document = mappedResults.get(0);
        return document.getInteger("count");
    }

    public int aggregateCount(Criteria criteria, String collectionName) {
        return aggregateCount(mongoTemplate, criteria, collectionName);
    }

    public PageResult<E> simplePagingQuery(Query query, PageQuery pageQuery, String collectionName, BaseFactory<T,E> factory) {

        PageResult<E> pageResult = new PageResult<>();
        if (pageQuery != null && pageQuery.isNeedTotal()) {
            long total = mongoTemplate.count(query, collectionName);
            pageResult.setTotalElements(total);
            if (total == 0) {
                return pageResult;
            }
        }

        List<T> results = mongoTemplate.find(appendPageable(query, pageQuery), DOClass, collectionName);
        pageResult.setList(factory.toDTO(results));
        return pageResult;
    }

    private Query appendPageable(Query query, PageQuery pageQuery) {

        if (pageQuery == null) {
            return query.with(Pageable.unpaged());
        }

        if (!CollectionUtils.isEmpty(pageQuery.getOrders())) {
            String fieldName = getPrimaryKeyFiledName();
            List<Sort.Order> orders = pageQuery.getOrders().stream().map(order -> {
                if (order.isAscending()) {
                    return order.getField().equals(fieldName) ? Sort.Order.asc("_id") : Sort.Order.asc(order.getField());
                } else {
                    return order.getField().equals(fieldName) ? Sort.Order.desc("_id") : Sort.Order.desc(order.getField());
                }
            }).collect(Collectors.toList());
            query.with(Sort.by(orders));
        }
//        Sort.by(Sort.Order.)
        return query.with(PageRequest.of(pageQuery.getCurrentPage() - 1, pageQuery.getPageSize()));
    }

    private String getPrimaryKeyFiledName() {

        Field[] fields = DOClass.getDeclaredFields();

        for (Field field : fields) {
            Id annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                return field.getName();
            }
        }

        return "";
    }

}
