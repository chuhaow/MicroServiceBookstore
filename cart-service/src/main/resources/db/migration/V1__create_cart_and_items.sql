create sequence cart_id_seq start with 1 increment by 50;

create sequence cart_item_id_seq start with 1 increment by 50;

create table carts (
                       cart_id bigint default nextval('cart_id_seq') primary key,
                       user_id text not null,
                       created_at timestamp default current_timestamp,
                       updated_at timestamp
);

create table cart_items (
                            cart_item_id bigint default nextval('cart_item_id_seq') primary key,
                            cart_id bigint not null,
                            product_code text not null,
                            product_name text not null,
                            product_price numeric not null,
                            quantity int default 1,
                            foreign key (cart_id) references carts(cart_id) on delete cascade
);
