package top.yueshushu.business.base.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;


/**
 * 分页查询参数
 * @author xin.liu
 * @date 2021-11-01
 */
@SuppressWarnings("unused")
@Data
public class PageQuery implements Serializable {

    /**
     * 每页大小
     */
    private int pageSize = 10;

    /**
     * 当前是第几页（默认第一页）
     */
    private int currentPage = 1;

    private boolean needTotal = true;

    private List<Order> orders;

    public static PageQuery of(int pageSize, int currentPage) {
        return new PageQuery(pageSize, currentPage, true);
    }

    public static PageQuery of(int pageSize, int currentPage, boolean needTotal) {
        return new PageQuery(pageSize, currentPage, needTotal);
    }

    public PageQuery() {

    }

    public PageQuery(int pageSize, int currentPage, boolean needTotal) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.needTotal = needTotal;
    }

    /**
     * 功能描述: 获取开始的记录位置数
     * @return 偏移量
     */
    public int getOffset() {
        if (currentPage <= 0) {
            currentPage = 1;
        }
        return (this.currentPage - 1) * pageSize;
    }

    public void addOrder(Order order) {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        orders.add(order);
    }
}
