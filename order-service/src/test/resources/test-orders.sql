truncate table orders cascade;
alter sequence order_id_seq restart with 100;
alter sequence order_item_id_seq restart with 100;

insert into orders (id,order_number,username,
                    customer_name,customer_email,customer_phone,
                    delivery_address_line1,delivery_address_line2,delivery_address_city,
                    delivery_address_state,delivery_address_zip_code,delivery_address_country,
                    status,comments) values
                                         (1, 'asdf-1234', 'test', 'test', 'test@gmail.com', '123456789', '190 Kingfisher St', 'Room 204B', 'Vancouver', 'BC', 'R3Y0B9', 'Canada', 'NEW', null),
                                         (2, 'asdf-5678', 'johnuser', 'john', 'john@gmail.com', '123456789', '123 John St', '', 'San Francisco', 'CA', '2140033', 'Japan', 'NEW', null)
;

insert into order_items(order_id, code, name, price, quantity) values
                                                                   (1, 'P100', 'The Hunger Games', 34.0, 2),
                                                                   (1, 'P101', 'To Kill a Mockingbird', 45.40, 1),
                                                                   (2, 'P102', 'The Chronicles of Narnia', 44.50, 1)
;