document.addEventListener('alpine:init', () =>{
    Alpine.data('initData', () => ({
        cart: {items: [], totalAmount: 0},
        orderForm:{
            customer:{
                name: "",
                email: "",
                phone: ""
            },
            address:{
                addressLine1: "",
                addressLine2: "",
                city: "",
                state: "",
                zipcode: "",
                country: ""
            }
        },
        formValidationData: {deliverableCountries:["Canada", "Japan", "USA", "UK"]},


        init(){
            updateCartItemCount();
            getCart().then((cartData) => {
                this.cart = cartData;
            }).catch((error) => {
                console.error("Failed to initialize cart:", error);
            });
        },

        createOrder(){
            let order = Object.assign({}, this.orderForm,{items: this.cart.items});
            console.log("Trying to place an order...")
            console.log("Order data to be sent:", JSON.stringify(order));
            $.ajax({
                url: '/api/orders',
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
        async updateItemQuantity(product, targetQuantity) {
            try {
                await updateProductQuantity(product, targetQuantity<0 ? 0 : targetQuantity);
                this.cart = await getCart();

            } catch (error) {
                console.error("Failed to update item quantity and fetch cart:", error);
            }
        }
    }))
})