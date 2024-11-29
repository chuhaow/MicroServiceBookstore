//TODO: This should store to a db instead of localStorage
let cachedCartItemData = null

function fetchCartItemData(){
    if(cachedCartItemData){
        return Promise.resolve()(cachedCartItemData)
    }
    return $.getJSON("/api/carts").then(response =>{
        cachedCartItemData = response
        return response
    })
}

const addProductToCart = function(product){
    const cartItemData = {
        "userId": "user",
        "item": {
            "code": product.code,
            "name": product.name,
            "price": product.price,
            "quantity": 1
        }
    }
    console.log(cartItemData)
    $.ajax({
        url: '/api/carts',
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data : JSON.stringify(cartItemData),
        success: (resp) =>{

            alert("Added to cart")
            updateCartItemCount();

        },
        error:(err) =>{
            console.log("Error adding to cart: ", err)
        }
    })

}

function updateCartItemCount(){
    $.getJSON("/api/carts")
        .done( (items) =>{
            const count = items.reduce((total,item) => total + item.quantity,0)
            $('#cart-item-count').text('(' + count + ')');
        })
        .fail( (error) =>{
            console.error("Error fetching cart items:", error);
            $('#cart-item-count').text('(' + 0 + ')');
        })
}

async function getCart() {
    const cart = { items: [], totalAmount: 0 };

    try {
        const items = await $.getJSON("/api/carts");
        cart.items = items;
        cart.totalAmount = items.reduce((sum, item) => sum + item.quantity * item.price, 0);
    } catch (error) {
        console.error("Error fetching cart items:", error);
    }

    return cart;
}

const updateProductQuantity = async function(product, quantity){
    const cartItemData = {
        "userId": "user",
        "item": {
            "code": product.code,
            "name": product.name,
            "price": product.price,
            "quantity": quantity
        }
    }
    $.ajax({
        url: '/api/carts',
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data : JSON.stringify(cartItemData),
        success: (resp) =>{

        },
        error:(err) =>{
            console.log("Error adding to cart: ", err)
        }
    })
}

const deleteCart = function() {
    localStorage.removeItem(BOOKSTORE_STATE_KEY)
    updateCartItemCount();
}
