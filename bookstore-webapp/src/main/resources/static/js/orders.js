document.addEventListener("alpine:init", () =>{
    Alpine.data("initData", () => ({
        orders:[],
        init(){
            this.loadOrders();
            updateCartItemCount();
        },
        loadOrders(){
            $.getJSON(apiGatewayUrl+"/orders/api/orders", (data) =>{
                this.orders = data
            })
        }
    }))
})