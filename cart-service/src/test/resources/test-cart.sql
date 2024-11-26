truncate table carts cascade;
alter sequence cart_id_seq restart with 100;
alter sequence cart_item_id_seq restart with 100;

insert into carts (cart_id, user_id, created_at,updated_at) values
                                                                (1, 'user', current_timestamp, current_timestamp);
insert into cart_items(cart_item_id, cart_id, product_code,product_name,product_price,quantity) values
                                                                                                    (1,1,'P100', 'A Book', 30.50,1);