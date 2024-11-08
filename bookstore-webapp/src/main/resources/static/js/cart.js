document.addEventListener('alpine:init', () =>{
    Alpine.data('initData', () => ({
        cart: {items: [], totalAmount: 0},
        orderForm:{
            customer:{
                name: "John",
                email: "email@email.com",
                phone: "11111111111"
            },
            address:{
                addressLine1: "123 street",
                addressLine2: "",
                city: "City",
                state: "state",
                zipcode: "2160033",
                country: "Country"
            }
        },

        init(){
            updateCartItemCount();
            this.cart = getCart()
            this.cart.totalAmount = getCartTotal();
        },

        createOrder(){
            let order = Object.assign({}, this.orderForm,{items: this.cart.items});
            console.log("Trying to place an order...")
            console.log("Order data to be sent:", JSON.stringify(order));
            $.ajax({
                url: apiGatewayUrl+'/orders/api/orders',
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data : JSON.stringify(order),
                success: (resp) =>{
                    this.clearCart();
                    alert("Order success")

                    window.location = "/orders/"+resp.orderNum;
                },
                error:(err) =>{
                    console.log("Order Creation Error: ", err)
                }
            })
        },
        clearCart(){
            deleteCart();
        },
        updateItemQuantity(code, quantity) {
            updateProductQuantity(code, quantity);
            this.cart = getCart()
            this.cart.totalAmount = getCartTotal();
        }
    }))
})