package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void itemOrder(){
        //given
        Member member = createMember();

        Book book = createBook("JPA", 10000, 10);

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), 5);

        //then
        Order order = orderRepository.findOne(orderId);

        System.out.println(order.getStatus()+ member.getName());

        assertEquals(OrderStatus.ORDER,order.getStatus(),"상품주문시 상태는 ORDER");
        assertEquals(1, order.getOrderItems().size());
        assertEquals(book.getPrice() *5 , order.getTotalPrice());
        assertEquals(10 - 5, book.getStockQuantity());
    }



    @Test
    public void orderCount(){
        //given
        Member member = createMember();

        Item item = createBook("JPA", 10000, 10);

        int orderCount = 11;
//        orderService.order(member.getId(), item.getId(), orderCount);
        //when
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), item.getId(), orderCount));

    }

    @Test
    public void orderCancel(){
        //given
        Member member = createMember();
        Book item = createBook("JPA", 10000, 10);
        int orderCount =2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals(item.getStockQuantity(), 10);
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }


    private Member createMember() {
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "guro", "1129"));
        em.persist(member);
        return member;
    }

}