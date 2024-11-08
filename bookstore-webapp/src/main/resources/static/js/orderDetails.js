document.addEventListener('alpine:init', () =>{
    Alpine.data('initData', (orderNumber) =>({
        orderNumber:orderNumber,
        orderDetails:{
            items:[],
            customer: {},
            address:{}
        },
        init(){
            updateCartItemCount();
            this.getOrderDetails(this.orderNumber);
        },
        getOrderDetails(orderNumber){
            $.getJSON(apiGatewayUrl+"/orders/api/orders/"+orderNumber, (data) =>{
                this.orderDetails = data;
            })
        }
    }))
})