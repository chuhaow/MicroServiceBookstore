document.addEventListener('alpine:init', ()=>{
    Alpine.data('initData', (pageNo) => ({
        pageNo: pageNo,
        products:{
            data:[]
        },
        init(){
            this.loadProducts(pageNo)
            updateCartItemCount();
            generateGuestId()
        },
        loadProducts(pageNo){
            console.log("loading products")
            $.getJSON("/api/products?page="+pageNo, (response) =>{
                console.log(response)
                this.products=response;
            })
        },
        addToCart(product){
            console.log("Adding "+ product.name)
            addProductToCart(product)
        },
        addToGuestCart(product){
            console.log("Adding "+ product.name)
            guestAddProductToCart(product)
        }
    }))
});