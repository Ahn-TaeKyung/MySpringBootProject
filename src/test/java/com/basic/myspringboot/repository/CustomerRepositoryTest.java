package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Customer;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.id.enhanced.CustomOptimizerDescriptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Rollback(value = false)
    void testDeleteCustomer(){
        Customer customer = customerRepository.findById(1L).orElseThrow(()->new RuntimeException("Customer Not Found"));
        customerRepository.delete(customer);
    }

    @Test
    @Rollback(value = false)
    @Disabled
    void testUpdateCustomer(){
        Customer customer = customerRepository.findById(1L).orElseThrow(() -> new RuntimeException("Customer Not Found"));
        //수정하려면 Entitiy의 Setter method를 호출한다.
        //update customers set customer_id=?,customer_name=? where id=? (@DynamicUpdate 적용전)
        //update customers set customer_name=? where id=? (@DynamicUpdate 적용후)
        customer.setCustomerName("홍길동");
//        customerRepository.save(customer);
        assertThat(customer.getCustomerName()).isEqualTo("홍길동");
    }

    @Test
    @Disabled
    void testByNotFoundException(){
        //<x extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)
        //Supplier 의 추상메서드 T get()
        Customer customer = customerRepository.findByCustomerId("A004").orElseThrow(() -> new RuntimeException("Customer Not Found"));
//        assertThat(customer.getCustomerId()).isEqualTo("A001");
    }

    @Test
    @Disabled
    void testFindBy(){
        Optional<Customer> optionalCustomer = customerRepository.findById(1L);
//        assertThat(optionalCustomer).isNotEmpty();
        if(optionalCustomer.isPresent()){
            Customer existcustomer = optionalCustomer.get();
            assertThat(existcustomer.getId()).isEqualTo(1L);
        }
        //Optional 의 orElseGet(Supplier<? extends T> supplier)
        //Supplier 의 추상메서드 T get()
        Optional<Customer> optionalCustomer2 = customerRepository.findByCustomerId("A002");
        Customer a001customer = optionalCustomer2.orElseGet(() -> new Customer());
        assertThat(a001customer.getCustomerName()).isEqualTo("스프링2");

        Customer notFoundCustomer = customerRepository.findByCustomerId("A004").orElseGet(() -> new Customer());
        assertThat(notFoundCustomer.getCustomerName()).isNull();
    }
    @Test
    @Rollback(value = false)
    @Disabled
    void testCreateCustomer() {
        //Given (준비 단계)
        Customer customer = new Customer();
        customer.setCustomerId("A002");
        customer.setCustomerName("스프링2");
        //When (실행 단계)
        Customer addCustomer = customerRepository.save(customer);
        //Then (검증 단계)
        assertThat(addCustomer).isNotNull();
        assertThat(addCustomer.getCustomerName()).isEqualTo("스프링2");
    }
}