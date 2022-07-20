package top.yueshushu.business.api.model.user;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import top.yueshushu.business.api.model.Base;
import top.yueshushu.business.api.model.KeywordCondition;
import top.yueshushu.business.base.page.PageQuery;

/**
 * 用户查询实体
 *
 * @author yuejianli
 * @date 2022-07-19
 */
@Data
public class UserQuery implements Base,Serializable {
	
	private PageQuery page;
	
	/**
	 * 查询条件. query DSL, 计算匹配度
	 */
	private UserQuery.QueryCondition queryCondition;
	
	/**
	 * 过滤条件. filter DSL，是否匹配
	 */
	private UserQuery.FilterCondition filterCondition;
	
	public void validate() {
		
		if (null == page && queryCondition == null && filterCondition == null) {
			throw new RuntimeException("error ");
		}
		// TODO 暂未处理，不进行相关验证
//        filterCondition.validate();
//        queryCondition.validate();
	
	}
	
	
	@Data
	public static class QueryCondition implements Base {
		private KeywordCondition keywordCondition;
		
		QueryCondition() {
		
		}
		
		QueryCondition(KeywordCondition keywordCondition) {
			this.keywordCondition = keywordCondition;
		}
		
		/**
		 * keyword 查询
		 *
		 * @param keyword keyword
		 * @param fuzzy 模糊匹配
		 * @return 查询条件
		 */
		public static UserQuery.QueryCondition of(String keyword, boolean fuzzy) {
//            if (fuzzy) {
//                throw new UnsupportedOperationException("fuzzy query unsupported");
//            }
			return new UserQuery.QueryCondition(KeywordCondition.buildWithFuzzy(keyword));
		}
	}
	
	@Data
	@Builder
	public static class FilterCondition implements Base {
		private String name;
		private String sex;
		private Integer age;
		private String description;
	}

}
