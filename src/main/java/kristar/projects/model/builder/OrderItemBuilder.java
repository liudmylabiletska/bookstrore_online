package kristar.projects.model.builder;

import kristar.projects.model.Book;
import kristar.projects.model.Order;
import kristar.projects.model.OrderItem;

public class OrderItemBuilder {
    private final OrderItem item;

    public OrderItemBuilder() {
        item = new OrderItem();
    }

    public OrderItemBuilder withBook(Book book) {
        item.setBook(book);
        item.setPrice(book.getPrice());
        return this;
    }

    public OrderItemBuilder withQuantity(int quantity) {
        item.setQuantity(quantity);
        return this;
    }

    public OrderItemBuilder forOrder(Order order) {
        item.setOrder(order);
        return this;
    }

    public OrderItem build() {
        return item;
    }
}
