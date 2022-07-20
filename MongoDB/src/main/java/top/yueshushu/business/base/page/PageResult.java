package top.yueshushu.business.base.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.Data;

/**
 * 分页查询返回结果
 * @author xin.liu
 * @date 2021-10-29
 */
@SuppressWarnings("all")
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    @SuppressWarnings("rawtypes")
    private static final PageResult EMPTY_PAGE_RESULT = new EmptyPageResult<>();

    /**
     * 每页显示个数
     */
    private int pageSize = 1;

    /**
     * 当前页码
     */
    private int currentPage = 1;

    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 总记录数
     */
    private long totalElements;

    /**
     * 结果列表
     */
    private List<T> list;

    @SuppressWarnings("unchecked")
    public static <T> PageResult<T> emptyResult() {
        return EMPTY_PAGE_RESULT;
    }

    public static PageResult of(PageQuery pageQuery) {
        return new PageResult(pageQuery);
    }

    public static PageResult of(int currentPage, int pageSize) {
        return new PageResult(currentPage, pageSize);
    }

    public PageResult() {
    }

    public PageResult(int currentPage, int pageSize) {
        setCurrentPage(currentPage);
        setPageSize(pageSize);
    }

    public PageResult(PageQuery pageQuery) {
        Objects.requireNonNull(pageQuery);
        setCurrentPage(pageQuery.getCurrentPage());
        setPageSize(pageQuery.getPageSize());
    }

    public void setTotalElements(long totalElements) {
        totalElements = totalElements<0 ? 0 : totalElements;
        //设置了totalCount就可以计算出总totalPage
        this.totalElements = totalElements;
        long totalPages = totalElements % pageSize == 0 ? totalElements / pageSize : (totalElements / pageSize + 1);
        setTotalPage(totalPages);
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage<0 ? 0 : totalPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize<=0 ? 1 : pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage<=0 ? 1 : currentPage;
    }

    private static class EmptyPageResult<E> extends PageResult<E> {

        EmptyPageResult() {
            super.setPageSize(1);
            super.setCurrentPage(1);
            super.setTotalElements(0);
            super.setList(Collections.emptyList());
        }

        @Override
        public int getPageSize() {
            return 1;
        }

        @Override
        public int getCurrentPage() {
            return 1;
        }

        @Override
        public long getTotalPage() {
            return 0;
        }

        @Override
        public long getTotalElements() {
            return 0;
        }

        @Override
        public List<E> getList() {
            return Collections.emptyList();
        }
    }

}