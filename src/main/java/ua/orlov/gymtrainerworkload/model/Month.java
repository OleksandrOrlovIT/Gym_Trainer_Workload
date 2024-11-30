package ua.orlov.gymtrainerworkload.model;

import ua.orlov.gymtrainerworkload.exception.BusinessLogicException;

public enum Month {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    private final int order;

    Month(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public static Month fromOrder(int order) {
        for (Month month : values()) {
            if (month.order == order) {
                return month;
            }
        }
        throw new BusinessLogicException("Invalid order for month: " + order);
    }
}
