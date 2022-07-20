package top.yueshushu.business.repository.service.impl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

import top.yueshushu.business.api.model.KeywordCondition;
import top.yueshushu.business.api.model.user.User;
import top.yueshushu.business.api.model.user.UserQuery;
import top.yueshushu.business.api.service.UserService;
import top.yueshushu.business.base.page.PageResult;
import top.yueshushu.business.repository.facade.UserRepository;
import top.yueshushu.business.repository.facade.impl.BaseRepositoryImpl;
import top.yueshushu.business.repository.factory.UserFactory;
import top.yueshushu.business.repository.model.UserDo;
import top.yueshushu.business.util.SelectConditionUtil;

/**
 * 用户服务接口实现
 * @author yuejianli
 * @date 2022-07-19
 */
@Service
public class UserServiceImpl extends BaseRepositoryImpl<UserDo, User>
		implements UserService {
	
	/**
	 * {@link top.yueshushu.business.repository.model.UserDo}
	 */
	private static final String COLLECTION_NAME = "test_user1";
	private static final Criteria CRITERIA_ALL_TRUE = Criteria.where("_id").ne(null);
	
	@Resource
	private UserRepository userRepository;
	@Resource
	private UserFactory userFactory;
	
	
	@Override
	public void saveOrUpdate(List<User> userList) {
		 userRepository.saveAll(userFactory.toModel(userList));
	}
	
	@Override
	public void deleteById(Integer id) {
	     userRepository.deleteById(id);
	}
	
	@Override
	public PageResult<User> pageQuery(UserQuery userQuery){
		userQuery.validate();
		
		//创建 Query 对象
		Query query = buildQuery(userQuery);
		
		//查询
		PageResult<User> userPageResult = simplePagingQuery(query, userQuery.getPage(), COLLECTION_NAME, userFactory);
		
		// 重新构建分页对象后进行返回
		PageResult<User> result = new PageResult<>(userQuery.getPage());
		result.setTotalElements(userPageResult.getTotalElements());
		result.setList(userPageResult.getList());
		return result;
	}
	
	private Query buildQuery(UserQuery userQuery) {
		Query query = new Query();
		
		UserQuery.FilterCondition filterCondition = userQuery.getFilterCondition();
		if (filterCondition != null) {
			if (!SelectConditionUtil.stringIsNullOrEmpty(filterCondition.getName())) {
				query.addCriteria(
						Criteria.where("name").is(filterCondition.getName())
				);
			}
			if (!SelectConditionUtil.stringIsNullOrEmpty(filterCondition.getSex())) {
				query.addCriteria(
						Criteria.where("sex").is(filterCondition.getSex())
				);
			}
			if (!SelectConditionUtil.stringIsNullOrEmpty(filterCondition.getDescription())) {
				query.addCriteria(
						Criteria.where("description").is(filterCondition.getDescription())
				);
			}
			if (filterCondition.getAge() != null) {
				query.addCriteria(
						Criteria.where("age").gte(filterCondition.getAge())
				);
			}
		}
		
		UserQuery.QueryCondition queryCondition = userQuery.getQueryCondition();
		if (queryCondition != null) {
			KeywordCondition keywordCondition = queryCondition.getKeywordCondition();
			if (keywordCondition.isFuzzy()) {
				// 当做id的字段需做特殊处理
				Criteria idCriteria = Criteria.where("name").regex(".*?" + keywordCondition.getKeyword() + ".*");
				Criteria nameCriteria = Criteria.where("description").regex(".*?" + keywordCondition.getKeyword() + ".*");
				Criteria keyWordCriteria = new Criteria();
				keyWordCriteria.orOperator(idCriteria, nameCriteria);
				query.addCriteria(
						keyWordCriteria
				);
			} else {
				Criteria idCriteria = Criteria.where("name").is(keywordCondition.getKeyword());
				Criteria nameCriteria = Criteria.where("description").is(keywordCondition.getKeyword());
				Criteria keyWordCriteria = new Criteria();
				keyWordCriteria.orOperator(idCriteria, nameCriteria);
				query.addCriteria(
						keyWordCriteria
				);
				
			}
		}
		return query;
	}
}
