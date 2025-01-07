
const addProductToCart = function(product){
    const cartItemData = {
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
            updateCartItemCount();
        },
        error:(err) =>{
            console.log("Error adding to cart: ", err)
        }
    })

}

const guestAddProductToCart = function(product){
    const guestId = getGuestId();
    const cartItemData = {
        "guestId" : guestId,
        "item": {
            "code": product.code,
            "name": product.name,
            "price": product.price,
            "quantity": 1
        }
    }
    console.log(cartItemData)
    $.ajax({
        url: '/api/carts/guest',
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data : JSON.stringify(cartItemData),
        success: (resp) =>{
            guestUpdateCartItemCount()
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

function guestUpdateCartItemCount(){
    $.getJSON("/api/carts/guest/" + getGuestId())
        .done( (items) =>{
            const count = items.reduce((total,item) => total + item.quantity,0)
            $('#cart-item-count').text('(' + count + ')');
        })
        .fail( (error) =>{
            console.error("Error fetching cart items:", error);
            $('#cart-item-count').text('(' + 0 + ')');
        })
}


function updateCartItemCount(){
    $.getJSON("/api/carts/guest")
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
        items.sort((a, b) => a.name.localeCompare(b.name));
        cart.items = items;
        cart.totalAmount = items.reduce((sum, item) => sum + item.quantity * item.price, 0);
        console.log("Getting cart")
    } catch (error) {
        console.error("Error fetching cart items:", error);
    }

    return cart;
}

const updateProductQuantity = async function(product, quantity){
    console.log(quantity)
    const cartItemData = {
        "itemCode": product.code,
        "quantity": quantity
    }
    console.log(cartItemData)
    return new Promise((resolve, reject) =>{
        $.ajax({
            url: '/api/carts/update/quantity',
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data : JSON.stringify(cartItemData),
            success: (resp) =>{
                console.log("New Quantity Count " + cartItemData.quantity + " items")
                updateCartItemCount()
            },
            error:(err) =>{
                console.log("Error Updating Cart: ", err)
            }
        })
    })


}

const deleteCart = function() {
    $.ajax({
        url: '/api/carts',
        type: "DELETE",
        success: (resp) =>{
            console.log("Emptied cart")
            updateCartItemCount()
        },
        error:(err) =>{
            console.log("Error Emptying Cart: ", err)
        }
    })

}
