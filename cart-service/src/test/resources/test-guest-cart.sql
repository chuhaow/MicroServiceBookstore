truncate table guest_carts cascade;
alter sequence cart_id_seq restart with 100;
alter sequence cart_item_id_seq restart with 100;

insert into guest_carts (cart_id, user_id, created_at,updated_at) values
    (1, 'abcd12344321', current_timestamp, current_timestamp);
insert into guest_cart_items(cart_item_id, cart_id, product_code,product_name,product_price,quantity) values
    (1,1,'P100', 'A Book', 30.50,1);