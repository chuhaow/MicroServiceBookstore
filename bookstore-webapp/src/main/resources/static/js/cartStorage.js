//TODO: This should store to a db instead of localStorage
const BOOKSTORE_STATE_KEY = "BOOKSTORE_STATE"

const getCart = function(){
    let cart = localStorage.getItem(BOOKSTORE_STATE_KEY)
    if(!cart){
        cart = JSON.stringify({items:[], totalAmount:0});
        localStorage.setItem(BOOKSTORE_STATE_KEY, cart)
    }
    return JSON.parse(cart)
}

const addProductToCart = function(product){
    let cart = getCart();
    let cartItem = cart.items.find(itemModel => itemModel.code === product.code);
    if(cartItem){
        cartItem.quantity = parseInt(cartItem.quantity) + 1;
    }else{
        cart.items.push(Object.assign({}, product,{quantity:1}))
    }
    localStorage.setItem(BOOKSTORE_STATE_KEY, JSON.stringify(cart))
    updateCartItemCount();
}

function updateCartItemCount(){
    let cart = getCart();
    let count = 0;
    cart.items.forEach(item =>{
        count = count + item.quantity
    })
    $('#cart-item-count').text('(' + count + ')');
}

const updateProductQuantity = function(code, quantity){
    let cart = getCart();
    if(quantity < 1){
        cart.items = cart.items.filter(itemModel => itemModel.code !== code )
    }else{
        let cartItem = cart.items.find(itemModel => itemModel.code = code);
        if(cartItem){
            cartItem.quantity = parseInt(quantity);
        }else{
            console.log("Product code is not in Cart")
        }
    }
    localStorage.setItem(BOOKSTORE_STATE_KEY, JSON.stringify(cart));
    updateCartItemCount();
}

function getCartTotal() {
    let cart = getCart();
    let totalAmount = 0;
    cart.items.forEach(item => {
        totalAmount = totalAmount + (item.price * item.quantity);
    });
    return totalAmount;
}

const deleteCart = function() {
    localStorage.removeItem(BOOKSTORE_STATE_KEY)
    updateCartItemCount();
}
