package top.yueshushu.business.base.page;

import lombok.Data;

/**
 * @author xin.liu
 * @date 2022-04-07
 */
@SuppressWarnings("unused")
@Data
public class Order {

    private String field;
    private int direction = Direction.ASC.getValue();

    public static Order asc(String field) {
        return new Order(field, Direction.ASC.getValue());
    }

    public static Order desc(String field) {
        return new Order(field, Direction.DESC.getValue());
    }

    public Order() {

    }

    private Order(String field, int direction) {
        this.field = field;
        this.direction = direction;
    }

    public void setDirection(int direction) {

        if (direction != Direction.ASC.getValue() && direction != Direction.DESC.getValue()) {
            throw new IllegalArgumentException("direction value is illegal");
        }

        this.direction = direction;
    }

    public boolean isAscending() {
        return this.direction == Direction.ASC.getValue();
    }

    public boolean isDescending() {
        return this.direction == Direction.DESC.getValue();
    }

    public enum Direction {
        /** 升序 */
        ASC(0),
        /** 降序 */
        DESC(1);

        private int value;
        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


    }
}
