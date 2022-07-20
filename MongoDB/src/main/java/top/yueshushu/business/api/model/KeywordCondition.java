package top.yueshushu.business.api.model;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.ToString;

/**
 * keyword condition
 * @author Yue Jianli
 * @date 2022-04-16
 */
@ToString
@Getter
@SuppressWarnings("unused")
public class KeywordCondition implements Base {

    /**
     * keyword
     */
    private String keyword;

    /**
     * 是否模糊匹配, 默认为false
     */
    private boolean fuzzy = false;

    public static KeywordCondition buildWithoutFuzzy(String keyword) {
        return new KeywordCondition(keyword, false);
    }

    // 为模糊查询
    public static KeywordCondition buildWithFuzzy(String keyword) {
        return new KeywordCondition(keyword, true);
    }

    public KeywordCondition() {

    }

    private KeywordCondition(String keyword, boolean fuzzy) {
        this.fuzzy = fuzzy;
        this.keyword = keyword;
    }

    public boolean isValid() {
        return !StringUtils.isEmpty(keyword);
    }

}
